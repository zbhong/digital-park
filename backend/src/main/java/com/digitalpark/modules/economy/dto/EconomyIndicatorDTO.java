package com.digitalpark.modules.economy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 经济指标DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "经济指标请求")
public class EconomyIndicatorDTO {

    @Schema(description = "指标ID")
    private Long id;

    @NotBlank(message = "指标名称不能为空")
    @Schema(description = "指标名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "指标类型不能为空")
    @Schema(description = "指标类型(产值/税收/就业/产业集聚度)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @Schema(description = "单位")
    private String unit;

    @NotNull(message = "指标值不能为空")
    @Schema(description = "指标值", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal value;

    @NotBlank(message = "统计周期不能为空")
    @Schema(description = "统计周期(月/季/年)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String period;

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "区域ID")
    private Long areaId;

    @Schema(description = "备注")
    private String remark;
}
