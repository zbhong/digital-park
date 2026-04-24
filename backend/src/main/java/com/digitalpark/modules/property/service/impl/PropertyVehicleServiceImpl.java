package com.digitalpark.modules.property.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.property.entity.PropertyVehicle;
import com.digitalpark.modules.property.mapper.PropertyVehicleMapper;
import com.digitalpark.modules.property.service.PropertyVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 车辆Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class PropertyVehicleServiceImpl implements PropertyVehicleService {

    private final PropertyVehicleMapper vehicleMapper;

    @Override
    public PageResult<PropertyVehicle> selectPage(PropertyVehicle query, int pageNum, int pageSize) {
        LambdaQueryWrapper<PropertyVehicle> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(PropertyVehicle::getCreateTime);
        Page<PropertyVehicle> page = vehicleMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public PropertyVehicle selectById(Long id) {
        return vehicleMapper.selectById(id);
    }

    @Override
    public PropertyVehicle selectByPlateNumber(String plateNumber) {
        LambdaQueryWrapper<PropertyVehicle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PropertyVehicle::getPlateNumber, plateNumber);
        return vehicleMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(PropertyVehicle vehicle) {
        if (!StringUtils.hasText(vehicle.getStatus())) {
            vehicle.setStatus("正常");
        }
        return vehicleMapper.insert(vehicle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(PropertyVehicle vehicle) {
        return vehicleMapper.updateById(vehicle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int disable(Long id) {
        LambdaUpdateWrapper<PropertyVehicle> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PropertyVehicle::getId, id).set(PropertyVehicle::getStatus, "禁用");
        return vehicleMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int enable(Long id) {
        LambdaUpdateWrapper<PropertyVehicle> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(PropertyVehicle::getId, id).set(PropertyVehicle::getStatus, "正常");
        return vehicleMapper.update(null, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return vehicleMapper.deleteById(id);
    }

    private LambdaQueryWrapper<PropertyVehicle> buildQueryWrapper(PropertyVehicle query) {
        LambdaQueryWrapper<PropertyVehicle> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getPlateNumber())) {
            wrapper.like(PropertyVehicle::getPlateNumber, query.getPlateNumber());
        }
        if (StringUtils.hasText(query.getOwnerName())) {
            wrapper.like(PropertyVehicle::getOwnerName, query.getOwnerName());
        }
        if (StringUtils.hasText(query.getOwnerType())) {
            wrapper.eq(PropertyVehicle::getOwnerType, query.getOwnerType());
        }
        if (query.getEnterpriseId() != null) {
            wrapper.eq(PropertyVehicle::getEnterpriseId, query.getEnterpriseId());
        }
        if (StringUtils.hasText(query.getCardType())) {
            wrapper.eq(PropertyVehicle::getCardType, query.getCardType());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(PropertyVehicle::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
