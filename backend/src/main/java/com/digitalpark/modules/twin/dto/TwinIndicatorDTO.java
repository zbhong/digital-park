package com.digitalpark.modules.twin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 核心指标配置DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "核心指标配置请求")
public class TwinIndicatorDTO {

    @Schema(description = "指标ID")
    private Long id;

    @NotBlank(message = "指标名称不能为空")
    @Schema(description = "指标名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "指标编码不能为空")
    @Schema(description = "指标编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @NotBlank(message = "指标分类不能为空")
    @Schema(description = "指标分类(安全/能源/环境/经济/设备)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String category;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "目标值")
    private BigDecimal targetValue;

    @Schema(description = "预警值")
    private BigDecimal warningValue;

    @Schema(description = "危险值")
    private BigDecimal dangerValue;

    @Schema(description = "当前值")
    private BigDecimal currentValue;

    @Schema(description = "图表类型(折线/柱状/饼图/仪表盘/数字)")
    private String chartType;

    @Schema(description = "2D地图绑定标识")
    private String map2dBind;

    @Schema(description = "刷新间隔(秒)")
    private Integer refreshInterval;

    @Schema(description = "备注")
    private String remark;
}
