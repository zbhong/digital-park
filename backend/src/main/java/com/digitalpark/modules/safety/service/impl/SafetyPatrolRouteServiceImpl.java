package com.digitalpark.modules.safety.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyPatrolRoute;
import com.digitalpark.modules.safety.mapper.SafetyPatrolRouteMapper;
import com.digitalpark.modules.safety.service.SafetyPatrolRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 巡检路线Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class SafetyPatrolRouteServiceImpl implements SafetyPatrolRouteService {

    private final SafetyPatrolRouteMapper safetyPatrolRouteMapper;

    @Override
    public PageResult<SafetyPatrolRoute> selectPage(SafetyPatrolRoute query, int pageNum, int pageSize) {
        LambdaQueryWrapper<SafetyPatrolRoute> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(SafetyPatrolRoute::getCreateTime);
        Page<SafetyPatrolRoute> page = safetyPatrolRouteMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<SafetyPatrolRoute> selectList(SafetyPatrolRoute query) {
        LambdaQueryWrapper<SafetyPatrolRoute> wrapper = buildQueryWrapper(query);
        return safetyPatrolRouteMapper.selectList(wrapper);
    }

    @Override
    public SafetyPatrolRoute selectById(Long id) {
        return safetyPatrolRouteMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SafetyPatrolRoute route) {
        return safetyPatrolRouteMapper.insert(route);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SafetyPatrolRoute route) {
        return safetyPatrolRouteMapper.updateById(route);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return safetyPatrolRouteMapper.deleteById(id);
    }

    private LambdaQueryWrapper<SafetyPatrolRoute> buildQueryWrapper(SafetyPatrolRoute query) {
        LambdaQueryWrapper<SafetyPatrolRoute> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SafetyPatrolRoute::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCycle())) {
            wrapper.eq(SafetyPatrolRoute::getCycle, query.getCycle());
        }
        return wrapper;
    }
}
