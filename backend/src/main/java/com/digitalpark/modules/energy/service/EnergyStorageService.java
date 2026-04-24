package com.digitalpark.modules.energy.service;

import com.digitalpark.modules.energy.dto.EnergyStorageDTO;
import com.digitalpark.modules.energy.entity.EnergyStorage;

import java.util.List;
import java.util.Map;

/**
 * 储能系统Service接口
 *
 * @author digitalpark
 */
public interface EnergyStorageService {

    /**
     * 查询储能系统列表
     */
    List<EnergyStorage> selectList(EnergyStorage query);

    /**
     * 根据ID查询
     */
    EnergyStorage selectById(Long id);

    /**
     * 新增储能系统
     */
    int insert(EnergyStorage storage);

    /**
     * 修改储能系统
     */
    int update(EnergyStorage storage);

    /**
     * 删除储能系统
     */
    int deleteById(Long id);

    /**
     * SOC监控数据
     */
    Map<String, Object> getSocMonitor(Long storageId);

    /**
     * 实时状态
     */
    Map<String, Object> getRealtimeStatus(Long id);

    /**
     * 更新储能策略
     */
    int updateStrategy(EnergyStorageDTO dto);

    /**
     * 收益统计
     */
    Map<String, Object> getIncomeStats(Long storageId, String period);
}
