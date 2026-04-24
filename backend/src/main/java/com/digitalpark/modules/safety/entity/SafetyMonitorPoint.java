package com.digitalpark.modules.safety.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 安防监测点位实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("safety_monitor_point")
@Schema(description = "安防监测点位")
public class SafetyMonitorPoint extends BaseEntity {

    /** 点位名称 */
    @Schema(description = "点位名称")
    private String name;

    /** 点位类型：摄像头/烟感/温感/门禁/火焰探测 */
    @Schema(description = "点位类型")
    private String type;

    /** 安装位置 */
    @Schema(description = "安装位置")
    private String location;

    /** 区域ID */
    @Schema(description = "区域ID")
    private Long areaId;

    /** 建筑ID */
    @Schema(description = "建筑ID")
    private Long buildingId;

    /** 状态：0-离线 1-在线 */
    @Schema(description = "状态")
    private Integer status;

    /** IP地址 */
    @Schema(description = "IP地址")
    private String ipAddress;

    /** 设备编码 */
    @Schema(description = "设备编码")
    private String deviceCode;
}
