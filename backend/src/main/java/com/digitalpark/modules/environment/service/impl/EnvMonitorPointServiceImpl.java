package com.digitalpark.modules.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.environment.entity.EnvMonitorPoint;
import com.digitalpark.modules.environment.mapper.EnvMonitorPointMapper;
import com.digitalpark.modules.environment.service.EnvMonitorPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 环境监测点位Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnvMonitorPointServiceImpl implements EnvMonitorPointService {

    private final EnvMonitorPointMapper envMonitorPointMapper;

    @Override
    public PageResult<EnvMonitorPoint> selectPage(EnvMonitorPoint query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EnvMonitorPoint> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EnvMonitorPoint::getCreateTime);
        Page<EnvMonitorPoint> page = envMonitorPointMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<EnvMonitorPoint> selectList(EnvMonitorPoint query) {
        LambdaQueryWrapper<EnvMonitorPoint> wrapper = buildQueryWrapper(query);
        return envMonitorPointMapper.selectList(wrapper);
    }

    @Override
    public EnvMonitorPoint selectById(Long id) {
        return envMonitorPointMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(EnvMonitorPoint point) {
        if (point.getStatus() == null) {
            point.setStatus("正常");
        }
        return envMonitorPointMapper.insert(point);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EnvMonitorPoint point) {
        return envMonitorPointMapper.updateById(point);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return envMonitorPointMapper.deleteById(id);
    }

    private LambdaQueryWrapper<EnvMonitorPoint> buildQueryWrapper(EnvMonitorPoint query) {
        LambdaQueryWrapper<EnvMonitorPoint> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(EnvMonitorPoint::getName, query.getName());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(EnvMonitorPoint::getType, query.getType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(EnvMonitorPoint::getStatus, query.getStatus());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(EnvMonitorPoint::getAreaId, query.getAreaId());
        }
        if (query.getBuildingId() != null) {
            wrapper.eq(EnvMonitorPoint::getBuildingId, query.getBuildingId());
        }
        if (StringUtils.hasText(query.getIndoorOutdoor())) {
            wrapper.eq(EnvMonitorPoint::getIndoorOutdoor, query.getIndoorOutdoor());
        }
        return wrapper;
    }
}
