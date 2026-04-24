package com.digitalpark.modules.economy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 企业画像DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "企业画像请求")
public class EconomyProfileDTO {

    @Schema(description = "画像ID")
    private Long id;

    @NotNull(message = "企业ID不能为空")
    @Schema(description = "企业ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long enterpriseId;

    @Schema(description = "所属行业")
    private String industry;

    @Schema(description = "企业规模")
    private String scale;

    @Schema(description = "营收(万元)")
    private BigDecimal revenue;

    @Schema(description = "税收(万元)")
    private BigDecimal tax;

    @Schema(description = "员工人数")
    private Integer employees;

    @Schema(description = "信用评级")
    private String creditRating;

    @Schema(description = "风险等级")
    private String riskLevel;

    @Schema(description = "标签JSON")
    private String tagsJson;

    @Schema(description = "备注")
    private String remark;
}
