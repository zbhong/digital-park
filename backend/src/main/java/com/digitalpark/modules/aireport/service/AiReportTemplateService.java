package com.digitalpark.modules.aireport.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.aireport.entity.AiReportTemplate;

import java.util.List;

/**
 * 报告模板Service接口
 *
 * @author digitalpark
 */
public interface AiReportTemplateService {

    PageResult<AiReportTemplate> selectPage(AiReportTemplate query, int pageNum, int pageSize);

    AiReportTemplate selectById(Long id);

    List<AiReportTemplate> selectAll();

    List<AiReportTemplate> selectByModule(String module);

    List<AiReportTemplate> selectByType(String type);

    List<AiReportTemplate> selectEnabled();

    int insert(AiReportTemplate template);

    int update(AiReportTemplate template);

    int deleteById(Long id);
}
