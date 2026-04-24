package com.digitalpark.modules.energy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.modules.energy.entity.EnergyGrid;
import com.digitalpark.modules.energy.mapper.EnergyGridMapper;
import com.digitalpark.modules.energy.service.EnergyGridService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配电网Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class EnergyGridServiceImpl implements EnergyGridService {

    private final EnergyGridMapper energyGridMapper;

    @Override
    public List<EnergyGrid> selectList(EnergyGrid query) {
        LambdaQueryWrapper<EnergyGrid> wrapper = new LambdaQueryWrapper<>();
        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(EnergyGrid::getName, query.getName());
        }
        if (query.getType() != null && !query.getType().isEmpty()) {
            wrapper.eq(EnergyGrid::getType, query.getType());
        }
        if (query.getVoltageLevel() != null && !query.getVoltageLevel().isEmpty()) {
            wrapper.eq(EnergyGrid::getVoltageLevel, query.getVoltageLevel());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(EnergyGrid::getAreaId, query.getAreaId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(EnergyGrid::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(EnergyGrid::getCreateTime);
        return energyGridMapper.selectList(wrapper);
    }

    @Override
    public EnergyGrid selectById(Long id) {
        return energyGridMapper.selectById(id);
    }

    @Override
    public int insert(EnergyGrid grid) {
        if (grid.getName() == null || grid.getName().isEmpty()) {
            throw new BusinessException("配电网名称不能为空");
        }
        return energyGridMapper.insert(grid);
    }

    @Override
    public int update(EnergyGrid grid) {
        if (grid.getId() == null) {
            throw new BusinessException("ID不能为空");
        }
        return energyGridMapper.updateById(grid);
    }

    @Override
    public int deleteById(Long id) {
        return energyGridMapper.deleteById(id);
    }
}
