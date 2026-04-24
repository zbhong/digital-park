package com.digitalpark.modules.energy.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.energy.dto.EnergyRecordDTO;
import com.digitalpark.modules.energy.entity.EnergyRecord;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 能源计量记录Service接口
 *
 * @author digitalpark
 */
public interface EnergyRecordService {

    /**
     * 分页查询记录
     */
    PageResult<EnergyRecord> selectPage(Long meterId, LocalDateTime startTime, LocalDateTime endTime, int pageNum, int pageSize);

    /**
     * 查询记录列表
     */
    List<EnergyRecord> selectList(EnergyRecord query);

    /**
     * 新增记录
     */
    int insert(EnergyRecord record);

    /**
     * 批量导入记录
     */
    int batchImport(List<EnergyRecord> records);

    /**
     * 按时间/区域/类型统计分析
     */
    Map<String, Object> getConsumptionStats(EnergyRecordDTO dto);

    /**
     * 费用统计
     */
    Map<String, Object> getCostStats(EnergyRecordDTO dto);

    /**
     * 碳排放统计
     */
    Map<String, Object> getCarbonStats(EnergyRecordDTO dto);

    /**
     * 能耗对标分析
     */
    Map<String, Object> getBenchmark(EnergyRecordDTO dto);

    /**
     * 峰谷平统计
     */
    Map<String, Object> getPeakValley(Long meterId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 趋势分析
     */
    Map<String, Object> getTrend(Long meterId, String period, LocalDateTime startTime, LocalDateTime endTime);
}
