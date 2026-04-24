package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 点位配置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_point_config")
@Schema(description = "点位配置")
public class TwinPointConfig extends BaseEntity {

    @Schema(description = "点位名称")
    private String pointName;

    @Schema(description = "点位编码")
    private String pointCode;

    @Schema(description = "所属底图ID")
    private Long mapId;

    @Schema(description = "图标ID")
    private Long iconId;

    @Schema(description = "X坐标(百分比)")
    @TableField("pos_x")
    private Double x;

    @Schema(description = "Y坐标(百分比)")
    @TableField("pos_y")
    private Double y;

    @Schema(description = "旋转角度")
    @TableField("pos_rotation")
    private Integer rotation;

    @Schema(description = "层级")
    @TableField("pos_z_index")
    private Integer zIndex;

    @Schema(description = "分组")
    @TableField("point_group")
    private String pointGroup;

    @Schema(description = "绑定设备ID")
    private Long deviceId;

    @Schema(description = "绑定设备编码")
    private String deviceCode;

    @Schema(description = "绑定设备名称")
    private String deviceName;

    @Schema(description = "绑定类型")
    private String bindType;

    @Schema(description = "单击动作")
    @TableField("click_action")
    private String clickAction;

    @Schema(description = "双击动作")
    @TableField("dbl_click_action")
    private String dblClickAction;

    @Schema(description = "是否显示标签")
    @TableField("show_label")
    private Integer showLabel;

    @Schema(description = "标签内容")
    @TableField("label_content")
    private String labelContent;

    @Schema(description = "状态(0-禁用 1-启用)")
    private Integer status;
}
