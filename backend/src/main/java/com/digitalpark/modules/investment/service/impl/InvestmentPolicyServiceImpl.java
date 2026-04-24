package com.digitalpark.modules.investment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.investment.entity.InvestmentPolicy;
import com.digitalpark.modules.investment.mapper.InvestmentPolicyMapper;
import com.digitalpark.modules.investment.service.InvestmentPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 招商政策Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class InvestmentPolicyServiceImpl implements InvestmentPolicyService {

    private final InvestmentPolicyMapper policyMapper;

    @Override
    public PageResult<InvestmentPolicy> selectPage(InvestmentPolicy query, int pageNum, int pageSize) {
        LambdaQueryWrapper<InvestmentPolicy> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(InvestmentPolicy::getCreateTime);
        Page<InvestmentPolicy> page = policyMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public InvestmentPolicy selectById(Long id) {
        return policyMapper.selectById(id);
    }

    @Override
    public List<InvestmentPolicy> selectEnabled() {
        LambdaQueryWrapper<InvestmentPolicy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvestmentPolicy::getStatus, "启用");
        return policyMapper.selectList(wrapper);
    }

    @Override
    public List<InvestmentPolicy> selectByType(String type) {
        LambdaQueryWrapper<InvestmentPolicy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(InvestmentPolicy::getType, type);
        wrapper.eq(InvestmentPolicy::getStatus, "启用");
        return policyMapper.selectList(wrapper);
    }

    @Override
    public List<InvestmentPolicy> matchPolicies(String industry, String scale) {
        List<InvestmentPolicy> all = selectEnabled();
        List<InvestmentPolicy> matched = new ArrayList<>();
        for (InvestmentPolicy policy : all) {
            boolean industryMatch = !StringUtils.hasText(policy.getIndustryScope())
                    || policy.getIndustryScope().contains(industry);
            boolean scaleMatch = !StringUtils.hasText(policy.getEnterpriseScale())
                    || policy.getEnterpriseScale().contains(scale);
            if (industryMatch && scaleMatch) {
                matched.add(policy);
            }
        }
        return matched;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(InvestmentPolicy policy) {
        if (!StringUtils.hasText(policy.getStatus())) {
            policy.setStatus("启用");
        }
        return policyMapper.insert(policy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(InvestmentPolicy policy) {
        return policyMapper.updateById(policy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int enable(Long id) {
        LambdaUpdateWrapper<InvestmentPolicy> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InvestmentPolicy::getId, id).set(InvestmentPolicy::getStatus, "启用");
        return policyMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int disable(Long id) {
        LambdaUpdateWrapper<InvestmentPolicy> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(InvestmentPolicy::getId, id).set(InvestmentPolicy::getStatus, "禁用");
        return policyMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return policyMapper.deleteById(id);
    }

    private LambdaQueryWrapper<InvestmentPolicy> buildQueryWrapper(InvestmentPolicy query) {
        LambdaQueryWrapper<InvestmentPolicy> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(InvestmentPolicy::getName, query.getName());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(InvestmentPolicy::getType, query.getType());
        }
        if (StringUtils.hasText(query.getIndustryScope())) {
            wrapper.like(InvestmentPolicy::getIndustryScope, query.getIndustryScope());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(InvestmentPolicy::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
