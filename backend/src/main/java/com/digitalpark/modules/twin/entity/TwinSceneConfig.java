package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 场景配置实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_scene_config")
@Schema(description = "场景配置")
public class TwinSceneConfig extends BaseEntity {

    @Schema(description = "场景名称")
    private String name;

    @Schema(description = "场景类型(园区总览/楼层视图/能源流向/安全态势/环境监测)")
    private String type;

    @Schema(description = "背景图URL")
    private String background;

    @Schema(description = "默认缩放级别")
    private Double zoom;

    @Schema(description = "中心点X")
    private Double centerX;

    @Schema(description = "中心点Y")
    private Double centerY;

    @Schema(description = "画布宽度")
    private Integer width;

    @Schema(description = "画布高度")
    private Integer height;

    @Schema(description = "图层配置JSON")
    private String layersJson;
}
