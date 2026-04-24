package com.digitalpark.modules.safety.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyPatrol;
import com.digitalpark.modules.safety.mapper.SafetyPatrolMapper;
import com.digitalpark.modules.safety.service.SafetyPatrolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 安全巡检Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class SafetyPatrolServiceImpl implements SafetyPatrolService {

    private final SafetyPatrolMapper safetyPatrolMapper;

    @Override
    public PageResult<SafetyPatrol> selectPage(SafetyPatrol query, int pageNum, int pageSize) {
        LambdaQueryWrapper<SafetyPatrol> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(SafetyPatrol::getCreateTime);
        Page<SafetyPatrol> page = safetyPatrolMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public SafetyPatrol selectById(Long id) {
        return safetyPatrolMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SafetyPatrol patrol) {
        patrol.setStatus("进行中");
        return safetyPatrolMapper.insert(patrol);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SafetyPatrol patrol) {
        return safetyPatrolMapper.updateById(patrol);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return safetyPatrolMapper.deleteById(id);
    }

    private LambdaQueryWrapper<SafetyPatrol> buildQueryWrapper(SafetyPatrol query) {
        LambdaQueryWrapper<SafetyPatrol> wrapper = new LambdaQueryWrapper<>();
        if (query.getRouteId() != null) {
            wrapper.eq(SafetyPatrol::getRouteId, query.getRouteId());
        }
        if (StringUtils.hasText(query.getPatrolType())) {
            wrapper.eq(SafetyPatrol::getPatrolType, query.getPatrolType());
        }
        if (StringUtils.hasText(query.getPatrolPerson())) {
            wrapper.like(SafetyPatrol::getPatrolPerson, query.getPatrolPerson());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(SafetyPatrol::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
