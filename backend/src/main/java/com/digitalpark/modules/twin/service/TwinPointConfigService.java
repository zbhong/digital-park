package com.digitalpark.modules.twin.service;

import com.digitalpark.modules.twin.entity.TwinPointConfig;

import java.util.List;

public interface TwinPointConfigService {

    List<TwinPointConfig> list(Long mapId, Long groupId);

    TwinPointConfig save(TwinPointConfig point);

    int batchSave(List<TwinPointConfig> points);

    TwinPointConfig update(TwinPointConfig point);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    void updatePosition(Long id, Double x, Double y, Double rotation, Integer zIndex);

    void bindDevice(Long id, Long deviceId, String deviceCode, String deviceName, String bindType);

    void unbindDevice(Long id);
}
