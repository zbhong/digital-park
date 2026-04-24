package com.digitalpark.modules.energy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 源侧设备实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("energy_source")
@Schema(description = "源侧设备")
public class EnergySource extends BaseEntity {

    @Schema(description = "设备名称")
    private String name;

    @Schema(description = "设备类型(SOLAR/WIND/STORAGE/CHARGING)")
    private String type;

    @Schema(description = "当前容量(kW)")
    private BigDecimal capacity;

    @Schema(description = "装机容量(kW)")
    private BigDecimal installedCapacity;

    @Schema(description = "安装位置")
    private String location;

    @Schema(description = "区域ID")
    private Long areaId;

    @Schema(description = "状态(0-停用 1-正常)")
    private Integer status;

    @Schema(description = "年发电量(kWh)")
    private BigDecimal annualOutput;

    @Schema(description = "转换效率(%)")
    private BigDecimal efficiency;

    @Schema(description = "日供电量(kWh)")
    private BigDecimal dailySupply;

    @Schema(description = "日收益(元)")
    private BigDecimal dailyIncome;
}
