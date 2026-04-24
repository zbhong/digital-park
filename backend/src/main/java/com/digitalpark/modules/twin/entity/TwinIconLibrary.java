package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图标库实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_icon_library")
@Schema(description = "图标库")
public class TwinIconLibrary extends BaseEntity {

    @Schema(description = "图标名称")
    private String iconName;

    @Schema(description = "图标类型(device/environment/safety/energy/building/custom)")
    private String iconType;

    @Schema(description = "图标分类")
    private String category;

    @Schema(description = "图标文件URL")
    private String iconUrl;

    @Schema(description = "SVG内容")
    private String svgContent;

    @Schema(description = "图标颜色")
    private String color;

    @Schema(description = "图标大小")
    private Integer size;

    @Schema(description = "是否系统预置(0-否 1-是)")
    private Integer isSystem;

    @Schema(description = "排序")
    private Integer sort;
}
