package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.ParkArea;
import com.digitalpark.modules.twin.mapper.ParkAreaMapper;
import com.digitalpark.modules.twin.service.ParkAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 园区区域Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class ParkAreaServiceImpl implements ParkAreaService {

    private final ParkAreaMapper areaMapper;

    @Override
    public PageResult<ParkArea> selectPage(ParkArea query, int pageNum, int pageSize) {
        LambdaQueryWrapper<ParkArea> wrapper = buildQueryWrapper(query);
        wrapper.orderByAsc(ParkArea::getCode);
        Page<ParkArea> page = areaMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public ParkArea selectById(Long id) {
        return areaMapper.selectById(id);
    }

    @Override
    public List<ParkArea> selectTree() {
        LambdaQueryWrapper<ParkArea> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(ParkArea::getCode);
        return areaMapper.selectList(wrapper);
    }

    @Override
    public List<ParkArea> selectByParentId(Long parentId) {
        LambdaQueryWrapper<ParkArea> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ParkArea::getParentId, parentId);
        wrapper.orderByAsc(ParkArea::getCode);
        return areaMapper.selectList(wrapper);
    }

    @Override
    public List<ParkArea> selectAll() {
        return areaMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(ParkArea area) {
        if (area.getParentId() == null) {
            area.setParentId(0L);
        }
        return areaMapper.insert(area);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(ParkArea area) {
        return areaMapper.updateById(area);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return areaMapper.deleteById(id);
    }

    private LambdaQueryWrapper<ParkArea> buildQueryWrapper(ParkArea query) {
        LambdaQueryWrapper<ParkArea> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(ParkArea::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCode())) {
            wrapper.eq(ParkArea::getCode, query.getCode());
        }
        if (query.getParentId() != null) {
            wrapper.eq(ParkArea::getParentId, query.getParentId());
        }
        if (StringUtils.hasText(query.getAreaType())) {
            wrapper.eq(ParkArea::getAreaType, query.getAreaType());
        }
        return wrapper;
    }
}
