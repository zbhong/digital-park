package com.digitalpark.modules.enterprise.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.enterprise.entity.EnterpriseInfo;
import com.digitalpark.modules.enterprise.mapper.EnterpriseInfoMapper;
import com.digitalpark.modules.enterprise.service.EnterpriseInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业信息Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnterpriseInfoServiceImpl implements EnterpriseInfoService {

    private final EnterpriseInfoMapper infoMapper;

    @Override
    public PageResult<EnterpriseInfo> selectPage(EnterpriseInfo query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EnterpriseInfo> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EnterpriseInfo::getCreateTime);
        Page<EnterpriseInfo> page = infoMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public EnterpriseInfo selectById(Long id) {
        return infoMapper.selectById(id);
    }

    @Override
    public List<EnterpriseInfo> selectByAreaId(Long areaId) {
        LambdaQueryWrapper<EnterpriseInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseInfo::getAreaId, areaId);
        return infoMapper.selectList(wrapper);
    }

    @Override
    public List<EnterpriseInfo> selectByBuildingId(Long buildingId) {
        LambdaQueryWrapper<EnterpriseInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EnterpriseInfo::getBuildingId, buildingId);
        return infoMapper.selectList(wrapper);
    }

    @Override
    public List<Map<String, Object>> selectIndustryStatistics() {
        List<EnterpriseInfo> list = infoMapper.selectList(null);
        Map<String, Integer> stats = new HashMap<>();
        for (EnterpriseInfo info : list) {
            String industry = StringUtils.hasText(info.getIndustry()) ? info.getIndustry() : "未分类";
            stats.merge(industry, 1, Integer::sum);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("industry", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectStatusStatistics() {
        List<EnterpriseInfo> list = infoMapper.selectList(null);
        Map<String, Integer> stats = new HashMap<>();
        for (EnterpriseInfo info : list) {
            String status = StringUtils.hasText(info.getStatus()) ? info.getStatus() : "未知";
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
    @Transactional(rollbackFor = Exception.class)
    public int insert(EnterpriseInfo info) {
        if (!StringUtils.hasText(info.getStatus())) {
            info.setStatus("在营");
        }
        return infoMapper.insert(info);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EnterpriseInfo info) {
        return infoMapper.updateById(info);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return infoMapper.deleteById(id);
    }

    private LambdaQueryWrapper<EnterpriseInfo> buildQueryWrapper(EnterpriseInfo query) {
        LambdaQueryWrapper<EnterpriseInfo> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(EnterpriseInfo::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCode())) {
            wrapper.eq(EnterpriseInfo::getCode, query.getCode());
        }
        if (StringUtils.hasText(query.getIndustry())) {
            wrapper.eq(EnterpriseInfo::getIndustry, query.getIndustry());
        }
        if (StringUtils.hasText(query.getScale())) {
            wrapper.eq(EnterpriseInfo::getScale, query.getScale());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(EnterpriseInfo::getAreaId, query.getAreaId());
        }
        if (query.getBuildingId() != null) {
            wrapper.eq(EnterpriseInfo::getBuildingId, query.getBuildingId());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(EnterpriseInfo::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
