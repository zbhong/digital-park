package com.digitalpark.modules.environment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 环境监测点位实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("env_monitor_point")
@Schema(description = "环境监测点位")
public class EnvMonitorPoint extends BaseEntity {

    /** 点位名称 */
    @Schema(description = "点位名称")
    private String name;

    /** 点位类型：大气/水质/噪音/扬尘/土壤 */
    @Schema(description = "点位类型")
    private String type;

    /** 点位位置 */
    @Schema(description = "点位位置")
    private String location;

    /** 区域ID */
    @Schema(description = "区域ID")
    private Long areaId;

    /** 建筑ID */
    @Schema(description = "建筑ID")
    private Long buildingId;

    /** 室内/室外 */
    @Schema(description = "室内/室外")
    private String indoorOutdoor;

    /** 经度 */
    @Schema(description = "经度")
    private String lng;

    /** 纬度 */
    @Schema(description = "纬度")
    private String lat;

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 2D地图绑定ID */
    @Schema(description = "2D地图绑定ID")
    private String map2dBind;
}
