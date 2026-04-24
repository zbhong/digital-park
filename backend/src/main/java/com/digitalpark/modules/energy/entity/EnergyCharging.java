package com.digitalpark.modules.energy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 充电桩实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("energy_charging")
@Schema(description = "充电桩")
public class EnergyCharging extends BaseEntity {

    @Schema(description = "充电桩名称")
    private String name;

    @Schema(description = "充电桩类型(AC_SLOW/DC_FAST/ORDERED)")
    private String type;

    @Schema(description = "接口类型")
    private String connectorType;

    @Schema(description = "额定功率(kW)")
    private BigDecimal ratedPower;

    @Schema(description = "当前功率(kW)")
    private BigDecimal currentPower;

    @Schema(description = "状态(IDLE/CHARGING/FAULT/OFFLINE)")
    private String status;

    @Schema(description = "安装位置")
    private String location;

    @Schema(description = "区域ID")
    private Long areaId;

    @Schema(description = "价格类型(1-统一 2-分时)")
    private Integer priceType;

    @Schema(description = "峰时电价(元/kWh)")
    private BigDecimal peakPrice;

    @Schema(description = "平时电价(元/kWh)")
    private BigDecimal flatPrice;

    @Schema(description = "谷时电价(元/kWh)")
    private BigDecimal valleyPrice;
}
