package com.digitalpark.modules.investment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.investment.entity.InvestmentCustomer;
import com.digitalpark.modules.investment.entity.InvestmentFollow;
import com.digitalpark.modules.investment.mapper.InvestmentCustomerMapper;
import com.digitalpark.modules.investment.mapper.InvestmentFollowMapper;
import com.digitalpark.modules.investment.service.InvestmentCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class InvestmentCustomerServiceImpl implements InvestmentCustomerService {

    private final InvestmentCustomerMapper customerMapper;
    private final InvestmentFollowMapper followMapper;

    @Override
    public PageResult<InvestmentCustomer> selectPage(InvestmentCustomer query, int pageNum, int pageSize) {
        LambdaQueryWrapper<InvestmentCustomer> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(InvestmentCustomer::getCreateTime);
        Page<InvestmentCustomer> page = customerMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public InvestmentCustomer selectById(Long id) {
        return customerMapper.selectById(id);
    }

    @Override
    public List<InvestmentCustomer> selectPublicPool() {
        LambdaQueryWrapper<InvestmentCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvestmentCustomer::getStatus, "公海");
        return customerMapper.selectList(wrapper);
    }

    @Override
    public List<InvestmentCustomer> selectByFollowUserId(Long followUserId) {
        LambdaQueryWrapper<InvestmentCustomer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvestmentCustomer::getFollowUserId, followUserId);
        return customerMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectStatusStatistics() {
        List<InvestmentCustomer> list = customerMapper.selectList(null);
        Map<String, Integer> stats = new HashMap<>();
        for (InvestmentCustomer customer : list) {
            String status = StringUtils.hasText(customer.getStatus()) ? customer.getStatus() : "未知";
            stats.merge(status, 1, Integer::sum);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("status", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectSourceStatistics() {
        List<InvestmentCustomer> list = customerMapper.selectList(null);
        Map<String, Integer> stats = new HashMap<>();
        for (InvestmentCustomer customer : list) {
            String source = StringUtils.hasText(customer.getSource()) ? customer.getSource() : "未知";
            stats.merge(source, 1, Integer::sum);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("source", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(InvestmentCustomer customer) {
        if (!StringUtils.hasText(customer.getStatus())) {
            customer.setStatus("公海");
        }
        return customerMapper.insert(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(InvestmentCustomer customer) {
        return customerMapper.updateById(customer);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int assign(Long id, Long followUserId) {
        LambdaUpdateWrapper<InvestmentCustomer> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InvestmentCustomer::getId, id)
               .set(InvestmentCustomer::getFollowUserId, followUserId)
               .set(InvestmentCustomer::getStatus, "跟进中");
        return customerMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int releaseToPublicPool(Long id) {
        LambdaUpdateWrapper<InvestmentCustomer> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InvestmentCustomer::getId, id)
               .set(InvestmentCustomer::getFollowUserId, null)
               .set(InvestmentCustomer::getStatus, "公海");
        return customerMapper.update(null, wrapper);
    }

    @Override
    public List<InvestmentFollow> selectFollowRecords(Long customerId) {
        LambdaQueryWrapper<InvestmentFollow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvestmentFollow::getCustomerId, customerId);
        wrapper.orderByDesc(InvestmentFollow::getCreateTime);
        return followMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addFollowRecord(InvestmentFollow follow) {
        return followMapper.insert(follow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFollowStatus(Long id, String status) {
        LambdaUpdateWrapper<InvestmentCustomer> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InvestmentCustomer::getId, id)
               .set(InvestmentCustomer::getStatus, status);
        return customerMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return customerMapper.deleteById(id);
    }

    private LambdaQueryWrapper<InvestmentCustomer> buildQueryWrapper(InvestmentCustomer query) {
        LambdaQueryWrapper<InvestmentCustomer> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(InvestmentCustomer::getName, query.getName());
        }
        if (StringUtils.hasText(query.getContactPerson())) {
            wrapper.like(InvestmentCustomer::getContactPerson, query.getContactPerson());
        }
        if (StringUtils.hasText(query.getIndustry())) {
            wrapper.eq(InvestmentCustomer::getIndustry, query.getIndustry());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(InvestmentCustomer::getStatus, query.getStatus());
        }
        if (query.getFollowUserId() != null) {
            wrapper.eq(InvestmentCustomer::getFollowUserId, query.getFollowUserId());
        }
        return wrapper;
    }
}
