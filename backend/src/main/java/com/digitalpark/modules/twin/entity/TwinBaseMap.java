package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 底图管理实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_base_map")
@Schema(description = "底图管理")
public class TwinBaseMap extends BaseEntity {

    @Schema(description = "底图名称")
    private String mapName;

    @Schema(description = "底图类型(campus/building/floor)")
    private String mapType;

    @Schema(description = "底图文件URL")
    @TableField("file_url")
    private String mapUrl;

    @Schema(description = "缩略图URL")
    private String thumbnailUrl;

    @Schema(description = "文件大小(字节)")
    private Long fileSize;

    @Schema(description = "分辨率宽度")
    private Integer resolutionX;

    @Schema(description = "分辨率高度")
    private Integer resolutionY;

    @Schema(description = "绑定对象ID")
    private Long bindId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否显示网格(0-否 1-是)")
    @TableField("show_grid")
    private Integer showGrid;

    @Schema(description = "状态(0-禁用 1-启用)")
    private Integer status;
}
