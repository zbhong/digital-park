package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备POI实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_device_poi")
@Schema(description = "设备POI")
public class TwinDevicePoi extends BaseEntity {

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "设备类型")
    private String deviceType;

    @Schema(description = "POI名称")
    private String name;

    @Schema(description = "X坐标")
    private Double x;

    @Schema(description = "Y坐标")
    private Double y;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "状态(在线/离线/故障)")
    private String status;

    @Schema(description = "弹窗配置JSON")
    private String popupConfigJson;
}
