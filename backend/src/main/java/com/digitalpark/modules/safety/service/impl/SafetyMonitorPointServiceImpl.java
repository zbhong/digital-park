package com.digitalpark.modules.safety.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyMonitorPoint;
import com.digitalpark.modules.safety.mapper.SafetyMonitorPointMapper;
import com.digitalpark.modules.safety.service.SafetyMonitorPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 安防监测点位Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class SafetyMonitorPointServiceImpl implements SafetyMonitorPointService {

    private final SafetyMonitorPointMapper safetyMonitorPointMapper;

    @Override
    public PageResult<SafetyMonitorPoint> selectPage(SafetyMonitorPoint query, int pageNum, int pageSize) {
        LambdaQueryWrapper<SafetyMonitorPoint> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(SafetyMonitorPoint::getCreateTime);
        Page<SafetyMonitorPoint> page = safetyMonitorPointMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<SafetyMonitorPoint> selectList(SafetyMonitorPoint query) {
        LambdaQueryWrapper<SafetyMonitorPoint> wrapper = buildQueryWrapper(query);
        return safetyMonitorPointMapper.selectList(wrapper);
    }

    @Override
    public SafetyMonitorPoint selectById(Long id) {
        return safetyMonitorPointMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SafetyMonitorPoint point) {
        return safetyMonitorPointMapper.insert(point);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SafetyMonitorPoint point) {
        return safetyMonitorPointMapper.updateById(point);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return safetyMonitorPointMapper.deleteById(id);
    }

    private LambdaQueryWrapper<SafetyMonitorPoint> buildQueryWrapper(SafetyMonitorPoint query) {
        LambdaQueryWrapper<SafetyMonitorPoint> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SafetyMonitorPoint::getName, query.getName());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(SafetyMonitorPoint::getType, query.getType());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(SafetyMonitorPoint::getAreaId, query.getAreaId());
        }
        if (query.getBuildingId() != null) {
            wrapper.eq(SafetyMonitorPoint::getBuildingId, query.getBuildingId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(SafetyMonitorPoint::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
