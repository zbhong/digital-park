package com.digitalpark.modules.energy.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.energy.entity.EnergyChargingRecord;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 充电记录Service接口
 *
 * @author digitalpark
 */
public interface EnergyChargingRecordService {

    /**
     * 分页查询充电记录
     */
    PageResult<EnergyChargingRecord> selectPage(Long chargingId, String plateNumber,
                                                 LocalDateTime startTime, LocalDateTime endTime,
                                                 int pageNum, int pageSize);

    /**
     * 新增充电记录
     */
    int insert(EnergyChargingRecord record);

    /**
     * 修改充电记录
     */
    int update(EnergyChargingRecord record);

    /**
     * 收入统计
     */
    Map<String, Object> getIncomeStats(String period);
}
