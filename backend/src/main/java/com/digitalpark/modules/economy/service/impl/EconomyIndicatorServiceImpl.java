package com.digitalpark.modules.economy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.economy.entity.EconomyIndicator;
import com.digitalpark.modules.economy.mapper.EconomyIndicatorMapper;
import com.digitalpark.modules.economy.service.EconomyIndicatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 经济指标Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EconomyIndicatorServiceImpl implements EconomyIndicatorService {

    private final EconomyIndicatorMapper indicatorMapper;

    @Override
    public PageResult<EconomyIndicator> selectPage(EconomyIndicator query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EconomyIndicator> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EconomyIndicator::getCreateTime);
        Page<EconomyIndicator> page = indicatorMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public EconomyIndicator selectById(Long id) {
        return indicatorMapper.selectById(id);
    }

    @Override
    public List<EconomyIndicator> selectByType(String type) {
        LambdaQueryWrapper<EconomyIndicator> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EconomyIndicator::getType, type);
        wrapper.orderByDesc(EconomyIndicator::getCreateTime);
        return indicatorMapper.selectList(wrapper);
    }

    @Override
    public List<EconomyIndicator> selectByPeriod(String period) {
        LambdaQueryWrapper<EconomyIndicator> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EconomyIndicator::getPeriod, period);
        wrapper.orderByDesc(EconomyIndicator::getCreateTime);
        return indicatorMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectStatistics(String type, String period) {
        LambdaQueryWrapper<EconomyIndicator> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(type)) {
            wrapper.eq(EconomyIndicator::getType, type);
        }
        if (StringUtils.hasText(period)) {
            wrapper.eq(EconomyIndicator::getPeriod, period);
        }
        List<EconomyIndicator> list = indicatorMapper.selectList(wrapper);
        Map<String, BigDecimal> stats = new HashMap<>();
        for (EconomyIndicator indicator : list) {
            String key = indicator.getName();
            stats.merge(key, indicator.getValue(), BigDecimal::add);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : stats.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", entry.getKey());
            item.put("totalValue", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectTrend(String name, String period) {
        LambdaQueryWrapper<EconomyIndicator> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.eq(EconomyIndicator::getName, name);
        }
        if (StringUtils.hasText(period)) {
            wrapper.eq(EconomyIndicator::getPeriod, period);
        }
        wrapper.orderByAsc(EconomyIndicator::getCreateTime);
        List<EconomyIndicator> list = indicatorMapper.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (EconomyIndicator indicator : list) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", indicator.getName());
            item.put("value", indicator.getValue());
            item.put("period", indicator.getPeriod());
            item.put("createTime", indicator.getCreateTime());
            result.add(item);
        }
        return result;
    }

    @Override
    public Map<String, Object> selectOverview() {
        List<EconomyIndicator> allIndicators = indicatorMapper.selectList(null);
        Map<String, Object> overview = new HashMap<>();
        BigDecimal totalOutput = BigDecimal.ZERO;
        BigDecimal totalTax = BigDecimal.ZERO;
        BigDecimal totalEmployment = BigDecimal.ZERO;
        for (EconomyIndicator indicator : allIndicators) {
            String type = indicator.getType();
            if ("产值".equals(type)) {
                totalOutput = totalOutput.add(indicator.getValue() != null ? indicator.getValue() : BigDecimal.ZERO);
            } else if ("税收".equals(type)) {
                totalTax = totalTax.add(indicator.getValue() != null ? indicator.getValue() : BigDecimal.ZERO);
            } else if ("就业".equals(type)) {
                totalEmployment = totalEmployment.add(indicator.getValue() != null ? indicator.getValue() : BigDecimal.ZERO);
            }
        }
        overview.put("totalOutput", totalOutput);
        overview.put("totalTax", totalTax);
        overview.put("totalEmployment", totalEmployment);
        overview.put("enterpriseCount", allIndicators.stream()
                .filter(i -> i.getEnterpriseId() != null)
                .map(EconomyIndicator::getEnterpriseId)
                .distinct()
                .count());
        return overview;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(EconomyIndicator indicator) {
        return indicatorMapper.insert(indicator);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EconomyIndicator indicator) {
        return indicatorMapper.updateById(indicator);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return indicatorMapper.deleteById(id);
    }

    private LambdaQueryWrapper<EconomyIndicator> buildQueryWrapper(EconomyIndicator query) {
        LambdaQueryWrapper<EconomyIndicator> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(EconomyIndicator::getName, query.getName());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(EconomyIndicator::getType, query.getType());
        }
        if (StringUtils.hasText(query.getPeriod())) {
            wrapper.eq(EconomyIndicator::getPeriod, query.getPeriod());
        }
        if (query.getEnterpriseId() != null) {
            wrapper.eq(EconomyIndicator::getEnterpriseId, query.getEnterpriseId());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(EconomyIndicator::getAreaId, query.getAreaId());
        }
        return wrapper;
    }
}
