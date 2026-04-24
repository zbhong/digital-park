package com.digitalpark.modules.energy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 负荷管理实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("energy_load")
@Schema(description = "负荷管理")
public class EnergyLoad extends BaseEntity {

    @Schema(description = "负荷名称")
    private String name;

    @Schema(description = "负荷类型(LIGHTING/HVAC/PUMP/FAN/PRODUCTION/OTHER)")
    private String type;

    @Schema(description = "额定功率(kW)")
    private BigDecimal ratedPower;

    @Schema(description = "当前功率(kW)")
    private BigDecimal currentPower;

    @Schema(description = "状态(0-停用 1-正常)")
    private Integer status;

    @Schema(description = "是否启用移峰填谷(0-否 1-是)")
    private Integer shiftEnabled;

    @Schema(description = "区域ID")
    private Long areaId;

    @Schema(description = "建筑ID")
    private Long buildingId;
}
