package com.digitalpark.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.enterprise.entity.EnterprisePolicy;
import com.digitalpark.modules.enterprise.mapper.EnterprisePolicyMapper;
import com.digitalpark.modules.enterprise.service.EnterprisePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 政策信息Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnterprisePolicyServiceImpl implements EnterprisePolicyService {

    private final EnterprisePolicyMapper policyMapper;

    @Override
    public PageResult<EnterprisePolicy> selectPage(EnterprisePolicy query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EnterprisePolicy> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EnterprisePolicy::getCreateTime);
        Page<EnterprisePolicy> page = policyMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public EnterprisePolicy selectById(Long id) {
        return policyMapper.selectById(id);
    }

    @Override
    public List<EnterprisePolicy> selectPublished() {
        LambdaQueryWrapper<EnterprisePolicy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterprisePolicy::getStatus, "已发布");
        wrapper.orderByDesc(EnterprisePolicy::getPublishDate);
        return policyMapper.selectList(wrapper);
    }

    @Override
    public List<EnterprisePolicy> selectByType(String type) {
        LambdaQueryWrapper<EnterprisePolicy> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterprisePolicy::getType, type);
        wrapper.eq(EnterprisePolicy::getStatus, "已发布");
        wrapper.orderByDesc(EnterprisePolicy::getPublishDate);
        return policyMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(EnterprisePolicy policy) {
        if (!StringUtils.hasText(policy.getStatus())) {
            policy.setStatus("草稿");
        }
        return policyMapper.insert(policy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EnterprisePolicy policy) {
        return policyMapper.updateById(policy);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int publish(Long id) {
        LambdaUpdateWrapper<EnterprisePolicy> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(EnterprisePolicy::getId, id)
               .set(EnterprisePolicy::getStatus, "已发布");
        return policyMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int expire(Long id) {
        LambdaUpdateWrapper<EnterprisePolicy> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(EnterprisePolicy::getId, id)
               .set(EnterprisePolicy::getStatus, "已过期");
        return policyMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return policyMapper.deleteById(id);
    }

    private LambdaQueryWrapper<EnterprisePolicy> buildQueryWrapper(EnterprisePolicy query) {
        LambdaQueryWrapper<EnterprisePolicy> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getTitle())) {
            wrapper.like(EnterprisePolicy::getTitle, query.getTitle());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(EnterprisePolicy::getType, query.getType());
        }
        if (StringUtils.hasText(query.getIndustryScope())) {
            wrapper.like(EnterprisePolicy::getIndustryScope, query.getIndustryScope());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(EnterprisePolicy::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
