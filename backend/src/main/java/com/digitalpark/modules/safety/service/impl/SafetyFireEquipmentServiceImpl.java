package com.digitalpark.modules.safety.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.safety.entity.SafetyFireEquipment;
import com.digitalpark.modules.safety.mapper.SafetyFireEquipmentMapper;
import com.digitalpark.modules.safety.service.SafetyFireEquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * 消防设备Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class SafetyFireEquipmentServiceImpl implements SafetyFireEquipmentService {

    private final SafetyFireEquipmentMapper safetyFireEquipmentMapper;

    @Override
    public PageResult<SafetyFireEquipment> selectPage(SafetyFireEquipment query, int pageNum, int pageSize) {
        LambdaQueryWrapper<SafetyFireEquipment> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(SafetyFireEquipment::getCreateTime);
        Page<SafetyFireEquipment> page = safetyFireEquipmentMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<SafetyFireEquipment> selectList(SafetyFireEquipment query) {
        LambdaQueryWrapper<SafetyFireEquipment> wrapper = buildQueryWrapper(query);
        return safetyFireEquipmentMapper.selectList(wrapper);
    }

    @Override
    public SafetyFireEquipment selectById(Long id) {
        return safetyFireEquipmentMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SafetyFireEquipment equipment) {
        return safetyFireEquipmentMapper.insert(equipment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SafetyFireEquipment equipment) {
        return safetyFireEquipmentMapper.updateById(equipment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return safetyFireEquipmentMapper.deleteById(id);
    }

    @Override
    public List<SafetyFireEquipment> selectExpiring(int days) {
        LocalDate threshold = LocalDate.now().plusDays(days);
        LambdaQueryWrapper<SafetyFireEquipment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SafetyFireEquipment::getStatus, 1);
        wrapper.le(SafetyFireEquipment::getExpireDate, threshold);
        wrapper.orderByAsc(SafetyFireEquipment::getExpireDate);
        return safetyFireEquipmentMapper.selectList(wrapper);
    }

    private LambdaQueryWrapper<SafetyFireEquipment> buildQueryWrapper(SafetyFireEquipment query) {
        LambdaQueryWrapper<SafetyFireEquipment> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(SafetyFireEquipment::getName, query.getName());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(SafetyFireEquipment::getType, query.getType());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(SafetyFireEquipment::getAreaId, query.getAreaId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(SafetyFireEquipment::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
