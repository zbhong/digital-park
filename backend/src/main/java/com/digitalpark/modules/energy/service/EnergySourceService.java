package com.digitalpark.modules.energy.service;

import com.digitalpark.modules.energy.entity.EnergySource;

import java.util.List;
import java.util.Map;

/**
 * 源侧设备Service接口
 *
 * @author digitalpark
 */
public interface EnergySourceService {

    /**
     * 查询源侧设备列表
     */
    List<EnergySource> selectList(EnergySource query);

    /**
     * 根据ID查询
     */
    EnergySource selectById(Long id);

    /**
     * 新增源侧设备
     */
    int insert(EnergySource source);

    /**
     * 修改源侧设备
     */
    int update(EnergySource source);

    /**
     * 删除源侧设备
     */
    int deleteById(Long id);

    /**
     * 发电统计
     */
    Map<String, Object> getGenerationStats(Long sourceId, String period);

    /**
     * 实时数据
     */
    Map<String, Object> getRealtimeData(Long id);
}
