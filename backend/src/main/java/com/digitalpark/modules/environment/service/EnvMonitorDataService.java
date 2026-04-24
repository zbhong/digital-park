package com.digitalpark.modules.environment.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.environment.dto.EnvMonitorDataDTO;
import com.digitalpark.modules.environment.entity.EnvMonitorData;

import java.util.List;
import java.util.Map;

/**
 * 环境监测数据Service接口
 *
 * @author digitalpark
 */
public interface EnvMonitorDataService {

    PageResult<EnvMonitorData> selectPage(EnvMonitorData query, int pageNum, int pageSize);

    EnvMonitorData selectById(Long id);

    int insert(EnvMonitorData data);

    int batchInsert(List<EnvMonitorData> dataList);

    int update(EnvMonitorData data);

    int deleteById(Long id);

    /**
     * 按时间范围统计分析
     */
    Map<String, Object> selectStatisticsByTime(EnvMonitorDataDTO dto);

    /**
     * 按区域统计分析
     */
    Map<String, Object> selectStatisticsByArea(EnvMonitorDataDTO dto);

    /**
     * 按指标统计分析
     */
    Map<String, Object> selectStatisticsByIndicator(EnvMonitorDataDTO dto);

    /**
     * 超标预警列表
     */
    List<Map<String, Object>> selectOverStandardWarnings(EnvMonitorDataDTO dto);

    /**
     * 污染溯源分析
     */
    Map<String, Object> selectPollutionSourceAnalysis(EnvMonitorDataDTO dto);
}
