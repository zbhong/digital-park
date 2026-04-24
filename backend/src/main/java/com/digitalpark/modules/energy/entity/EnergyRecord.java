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
 * 能源计量记录实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("energy_record")
@Schema(description = "能源计量记录")
public class EnergyRecord extends BaseEntity {

    @Schema(description = "计量表ID")
    private Long meterId;

    @Schema(description = "计量表编号")
    private String meterNo;

    @Schema(description = "表码值")
    private BigDecimal value;

    @Schema(description = "消耗量")
    private BigDecimal consumption;

    @Schema(description = "峰时段消耗量")
    private BigDecimal peakConsumption;

    @Schema(description = "平时段消耗量")
    private BigDecimal flatConsumption;

    @Schema(description = "谷时段消耗量")
    private BigDecimal valleyConsumption;

    @Schema(description = "费用(元)")
    private BigDecimal cost;

    @Schema(description = "碳排放(tCO2)")
    private BigDecimal carbonEmission;

    @Schema(description = "记录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordTime;
}
