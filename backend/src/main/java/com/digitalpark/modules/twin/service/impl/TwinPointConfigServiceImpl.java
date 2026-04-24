package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.digitalpark.modules.twin.entity.TwinPointConfig;
import com.digitalpark.modules.twin.mapper.TwinPointConfigMapper;
import com.digitalpark.modules.twin.service.TwinPointConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TwinPointConfigServiceImpl implements TwinPointConfigService {

    private final TwinPointConfigMapper pointConfigMapper;

    @Override
    public List<TwinPointConfig> list(Long mapId, Long groupId) {
        LambdaQueryWrapper<TwinPointConfig> wrapper = new LambdaQueryWrapper<>();
        if (mapId != null) {
            wrapper.eq(TwinPointConfig::getMapId, mapId);
        }
        if (groupId != null) {
            wrapper.eq(TwinPointConfig::getPointGroup, groupId);
        }
        wrapper.orderByAsc(TwinPointConfig::getZIndex);
        return pointConfigMapper.selectList(wrapper);
    }

    @Override
    public TwinPointConfig save(TwinPointConfig point) {
        pointConfigMapper.insert(point);
        return point;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSave(List<TwinPointConfig> points) {
        int count = 0;
        for (TwinPointConfig point : points) {
            if (point.getId() != null) {
                pointConfigMapper.updateById(point);
            } else {
                pointConfigMapper.insert(point);
            }
            count++;
        }
        return count;
    }

    @Override
    public TwinPointConfig update(TwinPointConfig point) {
        pointConfigMapper.updateById(point);
        return point;
    }

    @Override
    public void delete(Long id) {
        pointConfigMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        pointConfigMapper.deleteBatchIds(ids);
    }

    @Override
    public void updatePosition(Long id, Double x, Double y, Double rotation, Integer zIndex) {
        LambdaUpdateWrapper<TwinPointConfig> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TwinPointConfig::getId, id)
               .set(TwinPointConfig::getX, x)
               .set(TwinPointConfig::getY, y)
               .set(TwinPointConfig::getRotation, rotation)
               .set(TwinPointConfig::getZIndex, zIndex);
        pointConfigMapper.update(null, wrapper);
    }

    @Override
    public void bindDevice(Long id, Long deviceId, String deviceCode, String deviceName, String bindType) {
        LambdaUpdateWrapper<TwinPointConfig> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TwinPointConfig::getId, id)
               .set(TwinPointConfig::getDeviceId, deviceId)
               .set(TwinPointConfig::getDeviceCode, deviceCode)
               .set(TwinPointConfig::getDeviceName, deviceName)
               .set(TwinPointConfig::getBindType, bindType);
        pointConfigMapper.update(null, wrapper);
    }

    @Override
    public void unbindDevice(Long id) {
        LambdaUpdateWrapper<TwinPointConfig> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TwinPointConfig::getId, id)
               .set(TwinPointConfig::getDeviceId, null)
               .set(TwinPointConfig::getDeviceCode, null)
               .set(TwinPointConfig::getDeviceName, null)
               .set(TwinPointConfig::getBindType, null);
        pointConfigMapper.update(null, wrapper);
    }
}
