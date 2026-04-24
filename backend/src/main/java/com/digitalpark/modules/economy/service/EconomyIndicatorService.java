package com.digitalpark.modules.economy.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.economy.entity.EconomyIndicator;

import java.util.List;
import java.util.Map;

/**
 * 经济指标Service接口
 *
 * @author digitalpark
 */
public interface EconomyIndicatorService {

    PageResult<EconomyIndicator> selectPage(EconomyIndicator query, int pageNum, int pageSize);

    EconomyIndicator selectById(Long id);

    List<EconomyIndicator> selectByType(String type);

    List<EconomyIndicator> selectByPeriod(String period);

    List<Map<String, Object>> selectStatistics(String type, String period);

    List<Map<String, Object>> selectTrend(String name, String period);

    /**
     * 获取园区经济概览数据（产值、税收、就业等汇总）
     */
    Map<String, Object> selectOverview();

    int insert(EconomyIndicator indicator);

    int update(EconomyIndicator indicator);

    int deleteById(Long id);
}
