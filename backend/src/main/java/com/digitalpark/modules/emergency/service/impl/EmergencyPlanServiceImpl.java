package com.digitalpark.modules.emergency.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.emergency.entity.EmergencyPlan;
import com.digitalpark.modules.emergency.mapper.EmergencyPlanMapper;
import com.digitalpark.modules.emergency.service.EmergencyPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 应急预案Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EmergencyPlanServiceImpl implements EmergencyPlanService {

    private final EmergencyPlanMapper emergencyPlanMapper;

    @Override
    public PageResult<EmergencyPlan> selectPage(EmergencyPlan query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EmergencyPlan> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EmergencyPlan::getCreateTime);
        Page<EmergencyPlan> page = emergencyPlanMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public EmergencyPlan selectById(Long id) {
        return emergencyPlanMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(EmergencyPlan plan) {
        plan.setStatus("草稿");
        return emergencyPlanMapper.insert(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EmergencyPlan plan) {
        return emergencyPlanMapper.updateById(plan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return emergencyPlanMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int publish(Long id) {
        EmergencyPlan plan = emergencyPlanMapper.selectById(id);
        if (plan == null) {
            throw new BusinessException("应急预案不存在");
        }
        EmergencyPlan update = new EmergencyPlan();
        update.setId(id);
        update.setStatus("已发布");
        return emergencyPlanMapper.updateById(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int revoke(Long id) {
        EmergencyPlan plan = emergencyPlanMapper.selectById(id);
        if (plan == null) {
            throw new BusinessException("应急预案不存在");
        }
        EmergencyPlan update = new EmergencyPlan();
        update.setId(id);
        update.setStatus("已废止");
        return emergencyPlanMapper.updateById(update);
    }

    private LambdaQueryWrapper<EmergencyPlan> buildQueryWrapper(EmergencyPlan query) {
        LambdaQueryWrapper<EmergencyPlan> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(EmergencyPlan::getName, query.getName());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(EmergencyPlan::getType, query.getType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(EmergencyPlan::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
