package com.digitalpark.modules.economy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 经济指标实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("economy_indicator")
@Schema(description = "经济指标")
public class EconomyIndicator extends BaseEntity {

    @Schema(description = "指标名称")
    private String name;

    @Schema(description = "指标类型(产值/税收/就业/产业集聚度)")
    private String type;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "指标值")
    private BigDecimal value;

    @Schema(description = "统计周期(月/季/年)")
    private String period;

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "区域ID")
    private Long areaId;
}
