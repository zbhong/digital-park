package com.digitalpark.modules.energy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 充电桩调度DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "充电桩调度配置")
public class EnergyChargingDTO {

    @Schema(description = "充电桩ID列表")
    private List<Long> chargingIds;

    @Schema(description = "调度模式(AUTO自动/MANUAL手动/SMART智能)")
    private String scheduleMode;

    @Schema(description = "最大总功率限制(kW)")
    private BigDecimal maxTotalPower;

    @Schema(description = "优先级策略(FIFO先到先得/PRIORITY优先级/SOC按电量)")
    private String priorityStrategy;

    @Schema(description = "峰时段功率限制(kW)")
    private BigDecimal peakPowerLimit;

    @Schema(description = "平时段功率限制(kW)")
    private BigDecimal flatPowerLimit;

    @Schema(description = "谷时段功率限制(kW)")
    private BigDecimal valleyPowerLimit;

    @Schema(description = "是否启用有序充电(0-否 1-是)")
    private Integer orderedChargingEnabled;

    @Schema(description = "目标完成时间")
    private String targetFinishTime;
}
