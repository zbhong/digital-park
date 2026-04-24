package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinDevicePoi;
import com.digitalpark.modules.twin.mapper.TwinDevicePoiMapper;
import com.digitalpark.modules.twin.service.TwinDevicePoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 设备POIService实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class TwinDevicePoiServiceImpl implements TwinDevicePoiService {

    private final TwinDevicePoiMapper devicePoiMapper;

    @Override
    public PageResult<TwinDevicePoi> selectPage(TwinDevicePoi query, int pageNum, int pageSize) {
        LambdaQueryWrapper<TwinDevicePoi> wrapper = buildQueryWrapper(query);
        Page<TwinDevicePoi> page = devicePoiMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public TwinDevicePoi selectById(Long id) {
        return devicePoiMapper.selectById(id);
    }

    @Override
    public List<TwinDevicePoi> selectByDeviceType(String deviceType) {
        LambdaQueryWrapper<TwinDevicePoi> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TwinDevicePoi::getDeviceType, deviceType);
        return devicePoiMapper.selectList(wrapper);
    }

    @Override
    public List<TwinDevicePoi> selectAll() {
        return devicePoiMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(TwinDevicePoi poi) {
        return devicePoiMapper.insert(poi);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TwinDevicePoi poi) {
        return devicePoiMapper.updateById(poi);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<TwinDevicePoi> list) {
        int count = 0;
        for (TwinDevicePoi poi : list) {
            if (poi.getId() != null) {
                count += devicePoiMapper.updateById(poi);
            } else {
                count += devicePoiMapper.insert(poi);
            }
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return devicePoiMapper.deleteById(id);
    }

    private LambdaQueryWrapper<TwinDevicePoi> buildQueryWrapper(TwinDevicePoi query) {
        LambdaQueryWrapper<TwinDevicePoi> wrapper = new LambdaQueryWrapper<>();
        if (query.getDeviceId() != null) {
            wrapper.eq(TwinDevicePoi::getDeviceId, query.getDeviceId());
        }
        if (StringUtils.hasText(query.getDeviceType())) {
            wrapper.eq(TwinDevicePoi::getDeviceType, query.getDeviceType());
        }
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(TwinDevicePoi::getName, query.getName());
        }
        if (StringUtils.hasText(query.getStatus())) {
            wrapper.eq(TwinDevicePoi::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
