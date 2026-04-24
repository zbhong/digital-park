package com.digitalpark.modules.emergency.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.emergency.entity.EmergencyDrill;
import com.digitalpark.modules.emergency.mapper.EmergencyDrillMapper;
import com.digitalpark.modules.emergency.service.EmergencyDrillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 应急演练Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EmergencyDrillServiceImpl implements EmergencyDrillService {

    private final EmergencyDrillMapper emergencyDrillMapper;

    @Override
    public PageResult<EmergencyDrill> selectPage(EmergencyDrill query, int pageNum, int pageSize) {
        LambdaQueryWrapper<EmergencyDrill> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(EmergencyDrill::getCreateTime);
        Page<EmergencyDrill> page = emergencyDrillMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public EmergencyDrill selectById(Long id) {
        return emergencyDrillMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(EmergencyDrill drill) {
        return emergencyDrillMapper.insert(drill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(EmergencyDrill drill) {
        return emergencyDrillMapper.updateById(drill);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return emergencyDrillMapper.deleteById(id);
    }

    private LambdaQueryWrapper<EmergencyDrill> buildQueryWrapper(EmergencyDrill query) {
        LambdaQueryWrapper<EmergencyDrill> wrapper = new LambdaQueryWrapper<>();
        if (query.getPlanId() != null) {
            wrapper.eq(EmergencyDrill::getPlanId, query.getPlanId());
        }
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(EmergencyDrill::getName, query.getName());
        }
        return wrapper;
    }
}
