package com.digitalpark.modules.energy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 配电网实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("energy_grid")
@Schema(description = "配电网")
public class EnergyGrid extends BaseEntity {

    @Schema(description = "配电网名称")
    private String name;

    @Schema(description = "配电网类型")
    private String type;

    @Schema(description = "电压等级(kV)")
    private String voltageLevel;

    @Schema(description = "变压器容量(kVA)")
    private BigDecimal transformerCapacity;

    @Schema(description = "负载率(%)")
    private BigDecimal loadRate;

    @Schema(description = "状态(0-停用 1-正常)")
    private Integer status;

    @Schema(description = "区域ID")
    private Long areaId;
}
