package com.digitalpark.modules.aireport.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.aireport.entity.AiReportInstance;

import java.util.List;
import java.util.Map;

/**
 * 报告实例Service接口
 *
 * @author digitalpark
 */
public interface AiReportInstanceService {

    PageResult<AiReportInstance> selectPage(AiReportInstance query, int pageNum, int pageSize);

    AiReportInstance selectById(Long id);

    List<AiReportInstance> selectByTemplateId(Long templateId);

    List<AiReportInstance> selectByModule(String module);

    Map<String, Object> selectStatisticsByModule();

    AiReportInstance generateReport(Long templateId, String period, Map<String, Object> customParams);

    int insert(AiReportInstance instance);

    int update(AiReportInstance instance);

    int deleteById(Long id);
}
