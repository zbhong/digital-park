package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 点位样式实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_point_style")
@Schema(description = "点位样式")
public class TwinPointStyle extends BaseEntity {

    @Schema(description = "样式名称")
    private String styleName;

    @Schema(description = "适用图标类型")
    private String iconType;

    @Schema(description = "默认颜色")
    private String defaultColor;

    @Schema(description = "告警颜色")
    private String alertColor;

    @Schema(description = "离线颜色")
    private String offlineColor;

    @Schema(description = "图标大小")
    private Integer iconSize;

    @Schema(description = "是否显示标签(0-否 1-是)")
    private Integer showLabel;

    @Schema(description = "标签偏移X")
    private Integer labelOffsetX;

    @Schema(description = "标签偏移Y")
    private Integer labelOffsetY;

    @Schema(description = "动画类型(none/pulse/rotate/breathe)")
    private String animationType;

    @Schema(description = "动画速度(秒)")
    private Double animationSpeed;

    @Schema(description = "是否系统默认(0-否 1-是)")
    private Integer isDefault;
}
