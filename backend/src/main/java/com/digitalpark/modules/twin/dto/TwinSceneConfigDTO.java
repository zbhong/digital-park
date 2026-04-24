package com.digitalpark.modules.twin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 场景配置DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "场景配置请求")
public class TwinSceneConfigDTO {

    @Schema(description = "场景ID")
    private Long id;

    @NotBlank(message = "场景名称不能为空")
    @Schema(description = "场景名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "场景类型不能为空")
    @Schema(description = "场景类型(园区总览/楼层视图/能源流向/安全态势/环境监测)", requiredMode = Schema.RequiredMode.REQUIRED)
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

    @Schema(description = "备注")
    private String remark;
}
