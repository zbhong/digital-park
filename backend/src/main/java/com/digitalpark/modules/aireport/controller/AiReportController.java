package com.digitalpark.modules.aireport.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.aireport.dto.AiReportGenerateDTO;
import com.digitalpark.modules.aireport.entity.AiReportInstance;
import com.digitalpark.modules.aireport.entity.AiReportTemplate;
import com.digitalpark.modules.aireport.service.AiReportInstanceService;
import com.digitalpark.modules.aireport.service.AiReportTemplateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AI智能报告Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/ai-report")
@RequiredArgsConstructor
public class AiReportController {

    private final AiReportTemplateService templateService;
    private final AiReportInstanceService instanceService;

    // ========== 模板管理 ==========

    /**
     * 模板列表
     */
    @GetMapping("/templates")
    public R<PageResult<AiReportTemplate>> templatePage(AiReportTemplate query,
                                                         @RequestParam(defaultValue = "1") int pageNum,
                                                         @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(templateService.selectPage(query, pageNum, pageSize));
    }

    /**
     * 创建模板
     */
    @PostMapping("/templates")
    public R<AiReportTemplate> addTemplate(@RequestBody @Valid AiReportTemplate template) {
        templateService.insert(template);
        return R.ok(template);
    }

    /**
     * 更新模板
     */
    @PutMapping("/templates/{id}")
    public R<AiReportTemplate> editTemplate(@PathVariable Long id,
                                            @RequestBody @Valid AiReportTemplate template) {
        template.setId(id);
        templateService.update(template);
        return R.ok(template);
    }

    /**
     * 删除模板
     */
    @DeleteMapping("/templates/{id}")
    public R<Void> removeTemplate(@PathVariable Long id) {
        templateService.deleteById(id);
        return R.ok();
    }

    // ========== 报告实例管理 ==========

    /**
     * 报告实例列表(分页)
     */
    @GetMapping("/instances")
    public R<PageResult<AiReportInstance>> instancePage(AiReportInstance query,
                                                         @RequestParam(defaultValue = "1") int pageNum,
                                                         @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(instanceService.selectPage(query, pageNum, pageSize));
    }

    /**
     * 生成报告
     */
    @PostMapping("/instances/generate")
    public R<AiReportInstance> generateReport(@RequestBody @Valid AiReportGenerateDTO dto) {
        AiReportInstance instance = instanceService.generateReport(
                dto.getTemplateId(), dto.getPeriod(), dto.getCustomParams());
        return R.ok(instance);
    }

    /**
     * 获取报告详情
     */
    @GetMapping("/instances/{id}")
    public R<AiReportInstance> instanceInfo(@PathVariable Long id) {
        return R.ok(instanceService.selectById(id));
    }

    /**
     * 删除报告
     */
    @DeleteMapping("/instances/{id}")
    public R<Void> removeInstance(@PathVariable Long id) {
        instanceService.deleteById(id);
        return R.ok();
    }

    /**
     * 按模块统计报告数量
     */
    @GetMapping("/statistics")
    public R<Map<String, Object>> statistics() {
        return R.ok(instanceService.selectStatisticsByModule());
    }
}
