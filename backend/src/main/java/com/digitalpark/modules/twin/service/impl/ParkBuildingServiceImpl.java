package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.ParkBuilding;
import com.digitalpark.modules.twin.mapper.ParkBuildingMapper;
import com.digitalpark.modules.twin.service.ParkBuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 园区建筑Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class ParkBuildingServiceImpl implements ParkBuildingService {

    private final ParkBuildingMapper buildingMapper;

    @Override
    public PageResult<ParkBuilding> selectPage(ParkBuilding query, int pageNum, int pageSize) {
        LambdaQueryWrapper<ParkBuilding> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(ParkBuilding::getCreateTime);
        Page<ParkBuilding> page = buildingMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public ParkBuilding selectById(Long id) {
        return buildingMapper.selectById(id);
    }

    @Override
    public List<ParkBuilding> selectByAreaId(Long areaId) {
        LambdaQueryWrapper<ParkBuilding> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ParkBuilding::getAreaId, areaId);
        return buildingMapper.selectList(wrapper);
    }

    @Override
    public List<ParkBuilding> selectAll() {
        return buildingMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(ParkBuilding building) {
        if (!StringUtils.hasText(building.getStatus())) {
            building.setStatus("正常");
        }
        return buildingMapper.insert(building);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(ParkBuilding building) {
        return buildingMapper.updateById(building);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return buildingMapper.deleteById(id);
    }

    private LambdaQueryWrapper<ParkBuilding> buildQueryWrapper(ParkBuilding query) {
        LambdaQueryWrapper<ParkBuilding> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(ParkBuilding::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCode())) {
            wrapper.eq(ParkBuilding::getCode, query.getCode());
        }
        if (query.getAreaId() != null) {
            wrapper.eq(ParkBuilding::getAreaId, query.getAreaId());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(ParkBuilding::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
