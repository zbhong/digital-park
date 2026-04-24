package com.digitalpark.modules.energy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 储能策略配置DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "储能策略配置")
public class EnergyStorageDTO {

    @Schema(description = "储能ID")
    private Long storageId;

    @Schema(description = "策略类型(PeakShaving谷电峰用/ValleyFilling削峰填谷/TimeOfUse分时调度/Manual手动)")
    private String strategyType;

    @Schema(description = "充电开始SOC(%)")
    private BigDecimal chargeStartSoc;

    @Schema(description = "充电结束SOC(%)")
    private BigDecimal chargeEndSoc;

    @Schema(description = "放电开始SOC(%)")
    private BigDecimal dischargeStartSoc;

    @Schema(description = "放电结束SOC(%)")
    private BigDecimal dischargeEndSoc;

    @Schema(description = "最大充电功率(kW)")
    private BigDecimal maxChargePower;

    @Schema(description = "最大放电功率(kW)")
    private BigDecimal maxDischargePower;

    @Schema(description = "峰时段开始")
    private String peakStartTime;

    @Schema(description = "峰时段结束")
    private String peakEndTime;

    @Schema(description = "谷时段开始")
    private String valleyStartTime;

    @Schema(description = "谷时段结束")
    private String valleyEndTime;

    @Schema(description = "是否启用(0-否 1-是)")
    private Integer enabled;
}
