package com.digitalpark.modules.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.property.entity.PropertyFee;
import com.digitalpark.modules.property.mapper.PropertyFeeMapper;
import com.digitalpark.modules.property.service.PropertyFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物业费Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class PropertyFeeServiceImpl implements PropertyFeeService {

    private final PropertyFeeMapper feeMapper;

    @Override
    public PageResult<PropertyFee> selectPage(PropertyFee query, int pageNum, int pageSize) {
        LambdaQueryWrapper<PropertyFee> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(PropertyFee::getCreateTime);
        Page<PropertyFee> page = feeMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public PropertyFee selectById(Long id) {
        return feeMapper.selectById(id);
    }

    @Override
    public List<PropertyFee> selectByEnterpriseId(Long enterpriseId) {
        LambdaQueryWrapper<PropertyFee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PropertyFee::getEnterpriseId, enterpriseId);
        wrapper.orderByDesc(PropertyFee::getCreateTime);
        return feeMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectFeeStatistics() {
        List<PropertyFee> list = feeMapper.selectList(null);
        Map<String, Object> stats = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal paidAmount = BigDecimal.ZERO;
        BigDecimal unpaidAmount = BigDecimal.ZERO;
        BigDecimal overdueAmount = BigDecimal.ZERO;
        for (PropertyFee fee : list) {
            BigDecimal amt = fee.getAmount() != null ? fee.getAmount() : BigDecimal.ZERO;
            totalAmount = totalAmount.add(amt);
            if ("已缴".equals(fee.getStatus())) {
                paidAmount = paidAmount.add(amt);
            } else if ("待缴".equals(fee.getStatus())) {
                unpaidAmount = unpaidAmount.add(amt);
            } else if ("逾期".equals(fee.getStatus())) {
                overdueAmount = overdueAmount.add(amt);
            }
        }
        stats.put("totalAmount", totalAmount);
        stats.put("paidAmount", paidAmount);
        stats.put("unpaidAmount", unpaidAmount);
        stats.put("overdueAmount", overdueAmount);
        stats.put("totalCount", list.size());
        List<Map<String, Object>> result = new ArrayList<>();
        result.add(stats);
        return result;
    }

    @Override
    public List<Map<String, Object>> selectFeeTypeStatistics() {
        List<PropertyFee> list = feeMapper.selectList(null);
        Map<String, BigDecimal> typeStats = new HashMap<>();
        for (PropertyFee fee : list) {
            String feeType = StringUtils.hasText(fee.getFeeType()) ? fee.getFeeType() : "未分类";
            typeStats.merge(feeType, fee.getAmount() != null ? fee.getAmount() : BigDecimal.ZERO, BigDecimal::add);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : typeStats.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("feeType", entry.getKey());
            item.put("totalAmount", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    public BigDecimal selectTotalUnpaid(Long enterpriseId) {
        LambdaQueryWrapper<PropertyFee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PropertyFee::getEnterpriseId, enterpriseId);
        wrapper.in(PropertyFee::getStatus, "待缴", "逾期");
        List<PropertyFee> list = feeMapper.selectList(wrapper);
        BigDecimal total = BigDecimal.ZERO;
        for (PropertyFee fee : list) {
            total = total.add(fee.getAmount() != null ? fee.getAmount() : BigDecimal.ZERO);
        }
        return total;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(PropertyFee fee) {
        if (!StringUtils.hasText(fee.getStatus())) {
            fee.setStatus("待缴");
        }
        return feeMapper.insert(fee);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pay(Long id) {
        LambdaUpdateWrapper<PropertyFee> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PropertyFee::getId, id)
               .set(PropertyFee::getStatus, "已缴")
               .set(PropertyFee::getPayTime, LocalDateTime.now());
        return feeMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchPay(List<Long> ids) {
        int count = 0;
        for (Long id : ids) {
            count += pay(id);
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return feeMapper.deleteById(id);
    }

    private LambdaQueryWrapper<PropertyFee> buildQueryWrapper(PropertyFee query) {
        LambdaQueryWrapper<PropertyFee> wrapper = new LambdaQueryWrapper<>();
        if (query.getEnterpriseId() != null) {
            wrapper.eq(PropertyFee::getEnterpriseId, query.getEnterpriseId());
        }
        if (StringUtils.hasText(query.getFeeType())) {
            wrapper.eq(PropertyFee::getFeeType, query.getFeeType());
        }
        if (StringUtils.hasText(query.getPeriod())) {
            wrapper.eq(PropertyFee::getPeriod, query.getPeriod());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(PropertyFee::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
