package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 楼栋POI实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_building_poi")
@Schema(description = "楼栋POI")
public class TwinBuildingPoi extends BaseEntity {

    @Schema(description = "建筑ID")
    private Long buildingId;

    @Schema(description = "POI名称")
    private String name;

    @Schema(description = "X坐标")
    private Double x;

    @Schema(description = "Y坐标")
    private Double y;

    @Schema(description = "宽度")
    private Double width;

    @Schema(description = "高度")
    private Double height;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "弹窗配置JSON")
    private String popupConfigJson;
}
