package com.digitalpark.modules.energy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.modules.energy.entity.EnergyMeter;
import com.digitalpark.modules.energy.mapper.EnergyMeterMapper;
import com.digitalpark.modules.energy.service.EnergyMeterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 能源计量表Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnergyMeterServiceImpl implements EnergyMeterService {

    private final EnergyMeterMapper energyMeterMapper;

    @Override
    public List<EnergyMeter> selectList(EnergyMeter query) {
        LambdaQueryWrapper<EnergyMeter> wrapper = new LambdaQueryWrapper<>();
        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(EnergyMeter::getName, query.getName());
        }
        if (query.getMeterNo() != null && !query.getMeterNo().isEmpty()) {
            wrapper.eq(EnergyMeter::getMeterNo, query.getMeterNo());
        }
        if (query.getType() != null && !query.getType().isEmpty()) {
            wrapper.eq(EnergyMeter::getType, query.getType());
        }
        if (query.getEnergyType() != null && !query.getEnergyType().isEmpty()) {
            wrapper.eq(EnergyMeter::getEnergyType, query.getEnergyType());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(EnergyMeter::getAreaId, query.getAreaId());
        }
        if (query.getBuildingId() != null) {
            wrapper.eq(EnergyMeter::getBuildingId, query.getBuildingId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(EnergyMeter::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(EnergyMeter::getCreateTime);
        return energyMeterMapper.selectList(wrapper);
    }

    @Override
    public EnergyMeter selectById(Long id) {
        return energyMeterMapper.selectById(id);
    }

    @Override
    public int insert(EnergyMeter meter) {
        if (meter.getName() == null || meter.getName().isEmpty()) {
            throw new BusinessException("计量表名称不能为空");
        }
        if (meter.getMeterNo() == null || meter.getMeterNo().isEmpty()) {
            throw new BusinessException("计量表编号不能为空");
        }
        return energyMeterMapper.insert(meter);
    }

    @Override
    public int update(EnergyMeter meter) {
        if (meter.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return energyMeterMapper.updateById(meter);
    }

    @Override
    public int deleteById(Long id) {
        return energyMeterMapper.deleteById(id);
    }
}
