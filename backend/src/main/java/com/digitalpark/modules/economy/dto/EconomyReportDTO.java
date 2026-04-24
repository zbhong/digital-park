package com.digitalpark.modules.economy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 经济报告DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "经济报告请求")
public class EconomyReportDTO {

    @Schema(description = "报告ID")
    private Long id;

    @NotBlank(message = "报告标题不能为空")
    @Schema(description = "报告标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank(message = "报告类型不能为空")
    @Schema(description = "报告类型(月报/季报/年报)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @Schema(description = "报告周期")
    private String period;

    @Schema(description = "报告内容")
    private String content;

    @Schema(description = "状态(草稿/已发布)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
