package com.digitalpark.modules.economy.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.economy.entity.EconomyReport;

/**
 * 经济报告Service接口
 *
 * @author digitalpark
 */
public interface EconomyReportService {

    PageResult<EconomyReport> selectPage(EconomyReport query, int pageNum, int pageSize);

    EconomyReport selectById(Long id);

    int insert(EconomyReport report);

    int update(EconomyReport report);

    int publish(Long id);

    int generateReport(String type, String period);

    int deleteById(Long id);
}
