package com.digitalpark.modules.energy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 储能系统实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("energy_storage")
@Schema(description = "储能系统")
public class EnergyStorage extends BaseEntity {

    @Schema(description = "储能名称")
    private String name;

    @Schema(description = "储能类型(LITHIUM/FLOW/SUPERCAP)")
    private String type;

    @Schema(description = "额定容量(kWh)")
    private BigDecimal capacity;

    @Schema(description = "额定功率(kW)")
    private BigDecimal ratedPower;

    @Schema(description = "荷电状态SOC(%)")
    private BigDecimal soc;

    @Schema(description = "健康状态SOH(%)")
    private BigDecimal soh;

    @Schema(description = "充电功率(kW)")
    private BigDecimal chargePower;

    @Schema(description = "放电功率(kW)")
    private BigDecimal dischargePower;

    @Schema(description = "电压(V)")
    private BigDecimal voltage;

    @Schema(description = "电流(A)")
    private BigDecimal current;

    @Schema(description = "温度(°C)")
    private BigDecimal temperature;

    @Schema(description = "状态(0-停用 1-正常)")
    private Integer status;

    @Schema(description = "循环次数")
    private Integer cycleCount;

    @Schema(description = "日收益(元)")
    private BigDecimal dailyIncome;
}
