package com.digitalpark.modules.energy.service;

import com.digitalpark.modules.energy.dto.EnergyChargingDTO;
import com.digitalpark.modules.energy.entity.EnergyCharging;

import java.util.List;
import java.util.Map;

/**
 * 充电桩Service接口
 *
 * @author digitalpark
 */
public interface EnergyChargingService {

    /**
     * 查询充电桩列表
     */
    List<EnergyCharging> selectList(EnergyCharging query);

    /**
     * 根据ID查询
     */
    EnergyCharging selectById(Long id);

    /**
     * 新增充电桩
     */
    int insert(EnergyCharging charging);

    /**
     * 修改充电桩
     */
    int update(EnergyCharging charging);

    /**
     * 删除充电桩
     */
    int deleteById(Long id);

    /**
     * 状态监控
     */
    Map<String, Object> getStatusMonitor();

    /**
     * 运营统计
     */
    Map<String, Object> getOperationStats(String period);

    /**
     * 充电桩调度
     */
    int updateSchedule(EnergyChargingDTO dto);
}
