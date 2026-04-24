package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 园区区域实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("park_area")
@Schema(description = "园区区域")
public class ParkArea extends BaseEntity {

    @Schema(description = "区域名称")
    private String name;

    @Schema(description = "父级区域ID")
    private Long parentId;

    @Schema(description = "区域编码")
    private String code;

    @Schema(description = "区域类型(办公区/生产区/仓储区/生活区/绿化区)")
    private String areaType;

    @Schema(description = "区域面积(m²)")
    private BigDecimal areaSize;

    @Schema(description = "描述")
    private String description;
}
