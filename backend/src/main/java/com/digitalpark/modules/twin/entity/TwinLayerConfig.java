package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 图层配置实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_layer_config")
@Schema(description = "图层配置")
public class TwinLayerConfig extends BaseEntity {

    @Schema(description = "图层名称")
    private String name;

    @Schema(description = "图层类型(建筑/设备/管线/区域/环境/安防)")
    private String type;

    @Schema(description = "是否可见(0-不可见 1-可见)")
    private Integer visible;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "配置JSON")
    private String configJson;
}
