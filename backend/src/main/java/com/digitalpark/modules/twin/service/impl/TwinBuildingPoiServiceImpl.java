package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinBuildingPoi;
import com.digitalpark.modules.twin.mapper.TwinBuildingPoiMapper;
import com.digitalpark.modules.twin.service.TwinBuildingPoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 楼栋POIService实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class TwinBuildingPoiServiceImpl implements TwinBuildingPoiService {

    private final TwinBuildingPoiMapper buildingPoiMapper;

    @Override
    public PageResult<TwinBuildingPoi> selectPage(TwinBuildingPoi query, int pageNum, int pageSize) {
        LambdaQueryWrapper<TwinBuildingPoi> wrapper = buildQueryWrapper(query);
        Page<TwinBuildingPoi> page = buildingPoiMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public TwinBuildingPoi selectById(Long id) {
        return buildingPoiMapper.selectById(id);
    }

    @Override
    public List<TwinBuildingPoi> selectByBuildingId(Long buildingId) {
        LambdaQueryWrapper<TwinBuildingPoi> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TwinBuildingPoi::getBuildingId, buildingId);
        return buildingPoiMapper.selectList(wrapper);
    }

    @Override
    public List<TwinBuildingPoi> selectAll() {
        return buildingPoiMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(TwinBuildingPoi poi) {
        return buildingPoiMapper.insert(poi);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TwinBuildingPoi poi) {
        return buildingPoiMapper.updateById(poi);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return buildingPoiMapper.deleteById(id);
    }

    private LambdaQueryWrapper<TwinBuildingPoi> buildQueryWrapper(TwinBuildingPoi query) {
        LambdaQueryWrapper<TwinBuildingPoi> wrapper = new LambdaQueryWrapper<>();
        if (query.getBuildingId() != null) {
            wrapper.eq(TwinBuildingPoi::getBuildingId, query.getBuildingId());
        }
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(TwinBuildingPoi::getName, query.getName());
        }
        return wrapper;
    }
}
