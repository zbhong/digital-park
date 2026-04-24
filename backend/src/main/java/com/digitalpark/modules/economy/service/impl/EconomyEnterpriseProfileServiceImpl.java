package com.digitalpark.modules.economy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.economy.entity.EconomyEnterpriseProfile;
import com.digitalpark.modules.economy.mapper.EconomyEnterpriseProfileMapper;
import com.digitalpark.modules.economy.service.EconomyEnterpriseProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业画像Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EconomyEnterpriseProfileServiceImpl implements EconomyEnterpriseProfileService {

    private final EconomyEnterpriseProfileMapper profileMapper;

    @Override
    public PageResult<EconomyEnterpriseProfile> selectPage(EconomyEnterpriseProfile query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EconomyEnterpriseProfile> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EconomyEnterpriseProfile::getCreateTime);
        Page<EconomyEnterpriseProfile> page = profileMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public EconomyEnterpriseProfile selectById(Long id) {
        return profileMapper.selectById(id);
    }

    @Override
    public EconomyEnterpriseProfile selectByEnterpriseId(Long enterpriseId) {
        LambdaQueryWrapper<EconomyEnterpriseProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EconomyEnterpriseProfile::getEnterpriseId, enterpriseId);
        return profileMapper.selectOne(wrapper);
    }

    @Override
    public List<EconomyEnterpriseProfile> selectByIndustry(String industry) {
        LambdaQueryWrapper<EconomyEnterpriseProfile> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EconomyEnterpriseProfile::getIndustry, industry);
        return profileMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectIndustryDistribution() {
        List<EconomyEnterpriseProfile> list = profileMapper.selectList(null);
        Map<String, Integer> distribution = new HashMap<>();
        for (EconomyEnterpriseProfile profile : list) {
            String industry = StringUtils.hasText(profile.getIndustry()) ? profile.getIndustry() : "未分类";
            distribution.merge(industry, 1, Integer::sum);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("industry", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectScaleDistribution() {
        List<EconomyEnterpriseProfile> list = profileMapper.selectList(null);
        Map<String, Integer> distribution = new HashMap<>();
        for (EconomyEnterpriseProfile profile : list) {
            String scale = StringUtils.hasText(profile.getScale()) ? profile.getScale() : "未分类";
            distribution.merge(scale, 1, Integer::sum);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("scale", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(EconomyEnterpriseProfile profile) {
        return profileMapper.insert(profile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EconomyEnterpriseProfile profile) {
        return profileMapper.updateById(profile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return profileMapper.deleteById(id);
    }

    private LambdaQueryWrapper<EconomyEnterpriseProfile> buildQueryWrapper(EconomyEnterpriseProfile query) {
        LambdaQueryWrapper<EconomyEnterpriseProfile> wrapper = new LambdaQueryWrapper<>();
        if (query.getEnterpriseId() != null) {
            wrapper.eq(EconomyEnterpriseProfile::getEnterpriseId, query.getEnterpriseId());
        }
        if (StringUtils.hasText(query.getIndustry())) {
            wrapper.eq(EconomyEnterpriseProfile::getIndustry, query.getIndustry());
        }
        if (StringUtils.hasText(query.getScale())) {
            wrapper.eq(EconomyEnterpriseProfile::getScale, query.getScale());
        }
        if (StringUtils.hasText(query.getCreditRating())) {
            wrapper.eq(EconomyEnterpriseProfile::getCreditRating, query.getCreditRating());
        }
        if (StringUtils.hasText(query.getRiskLevel())) {
            wrapper.eq(EconomyEnterpriseProfile::getRiskLevel, query.getRiskLevel());
        }
        return wrapper;
    }
}
