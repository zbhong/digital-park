package com.digitalpark.modules.environment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 环境监测数据实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("env_monitor_data")
@Schema(description = "环境监测数据")
public class EnvMonitorData extends BaseEntity {

    /** 监测点位ID */
    @Schema(description = "监测点位ID")
    private Long pointId;

    /** 点位名称 */
    @Schema(description = "点位名称")
    private String pointName;

    /** PM2.5 */
    @Schema(description = "PM2.5")
    private BigDecimal pm25;

    /** PM10 */
    @Schema(description = "PM10")
    private BigDecimal pm10;

    /** SO2 */
    @Schema(description = "SO2")
    private BigDecimal so2;

    /** NO2 */
    @Schema(description = "NO2")
    private BigDecimal no2;

    /** CO */
    @Schema(description = "CO")
    private BigDecimal co;

    /** O3 */
    @Schema(description = "O3")
    private BigDecimal o3;

    /** VOCs */
    @Schema(description = "VOCs")
    private BigDecimal vocs;

    /** 水质pH */
    @Schema(description = "水质pH")
    private BigDecimal waterPh;

    /** 水质COD */
    @Schema(description = "水质COD")
    private BigDecimal waterCod;

    /** 水质氨氮 */
    @Schema(description = "水质氨氮")
    private BigDecimal waterAmmoniaNitrogen;

    /** 水质总磷 */
    @Schema(description = "水质总磷")
    private BigDecimal waterTotalPhosphorus;

    /** 水质总氮 */
    @Schema(description = "水质总氮")
    private BigDecimal waterTotalNitrogen;

    /** 水质浊度 */
    @Schema(description = "水质浊度")
    private BigDecimal waterTurbidity;

    /** 昼间噪音 */
    @Schema(description = "昼间噪音")
    private BigDecimal noiseDay;

    /** 夜间噪音 */
    @Schema(description = "夜间噪音")
    private BigDecimal noiseNight;

    /** 扬尘浓度 */
    @Schema(description = "扬尘浓度")
    private BigDecimal dustConcentration;

    /** 固废产生量 */
    @Schema(description = "固废产生量")
    private BigDecimal solidWasteGenerated;

    /** 固废处置量 */
    @Schema(description = "固废处置量")
    private BigDecimal solidWasteDisposed;

    /** 土壤重金属 */
    @Schema(description = "土壤重金属")
    private BigDecimal soilHeavyMetal;

    /** 记录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "记录时间")
    private LocalDateTime recordTime;
}
