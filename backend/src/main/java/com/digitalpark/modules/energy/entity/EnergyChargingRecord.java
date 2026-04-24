package com.digitalpark.modules.energy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充电记录实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("energy_charging_record")
@Schema(description = "充电记录")
public class EnergyChargingRecord extends BaseEntity {

    @Schema(description = "充电桩ID")
    private Long chargingId;

    @Schema(description = "车牌号")
    private String plateNumber;

    @Schema(description = "车辆识别码")
    private String vin;

    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description = "充电时长(分钟)")
    private Integer duration;

    @Schema(description = "充电量(kWh)")
    private BigDecimal energy;

    @Schema(description = "充电金额(元)")
    private BigDecimal cost;

    @Schema(description = "状态(0-充电中 1-已完成 2-异常结束)")
    private Integer status;

    @Schema(description = "支付方式(WECHAT/ALIPAY/CARD/BALANCE)")
    private String paymentMethod;
}
