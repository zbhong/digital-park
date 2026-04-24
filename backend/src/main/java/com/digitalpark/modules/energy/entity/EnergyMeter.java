package com.digitalpark.modules.energy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 能源计量表实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("energy_meter")
@Schema(description = "能源计量表")
public class EnergyMeter extends BaseEntity {

    @Schema(description = "计量表名称")
    private String name;

    @Schema(description = "计量表编号")
    private String meterNo;

    @Schema(description = "计量表类型(ELECTRIC/WATER/GAS/STEAM/COMPRESSED_AIR/HEAT/COOL)")
    private String type;

    @Schema(description = "能源类型")
    private String energyType;

    @Schema(description = "计量单位")
    private String unit;

    @Schema(description = "精度等级")
    private String accuracyClass;

    @Schema(description = "最大量程")
    private String maxRange;

    @Schema(description = "安装位置")
    private String location;

    @Schema(description = "区域ID")
    private Long areaId;

    @Schema(description = "建筑ID")
    private Long buildingId;

    @Schema(description = "楼层")
    private String floor;

    @Schema(description = "状态(0-停用 1-正常)")
    private Integer status;

    @Schema(description = "通信协议")
    private String protocol;

    @Schema(description = "IP地址")
    private String ipAddress;
}
