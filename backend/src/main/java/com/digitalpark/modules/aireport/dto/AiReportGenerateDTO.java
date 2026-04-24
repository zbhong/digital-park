package com.digitalpark.modules.aireport.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 生成报告请求DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "生成报告请求")
public class AiReportGenerateDTO {

    @NotNull(message = "模板ID不能为空")
    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long templateId;

    @Schema(description = "报告周期(如: 2026-04)")
    private String period;

    @Schema(description = "自定义参数")
    private Map<String, Object> customParams;
}
