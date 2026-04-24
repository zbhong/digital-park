package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 核心指标实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_indicator")
@Schema(description = "核心指标")
public class TwinIndicator extends BaseEntity {

    @Schema(description = "指标名称")
    private String name;

    @Schema(description = "指标编码")
    private String code;

    @Schema(description = "指标分类(安全/能源/环境/经济/设备)")
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
}
