package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 园区建筑实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("park_building")
@Schema(description = "园区建筑")
public class ParkBuilding extends BaseEntity {

    @Schema(description = "建筑名称")
    private String name;

    @Schema(description = "建筑编码")
    private String code;

    @Schema(description = "所属区域ID")
    private Long areaId;

    @Schema(description = "楼层数")
    private Integer floors;

    @Schema(description = "总面积(m²)")
    private BigDecimal totalArea;

    @Schema(description = "已用面积(m²)")
    private BigDecimal usedArea;

    @Schema(description = "详细地址")
    private String address;

    @Schema(description = "经度")
    private BigDecimal lng;

    @Schema(description = "纬度")
    private BigDecimal lat;

    @Schema(description = "2D地图URL")
    private String map2dUrl;

    @Schema(description = "2D地图配置JSON")
    private String map2dConfig;

    @Schema(description = "状态(正常/维修/停用)")
    private String status;

    @Schema(description = "描述")
    private String description;
}
