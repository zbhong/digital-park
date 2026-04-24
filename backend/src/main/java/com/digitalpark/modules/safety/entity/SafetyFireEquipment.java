package com.digitalpark.modules.safety.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 消防设备实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("safety_fire_equipment")
@Schema(description = "消防设备")
public class SafetyFireEquipment extends BaseEntity {

    /** 设备名称 */
    @Schema(description = "设备名称")
    private String name;

    /** 设备类型：灭火器/消防栓/喷淋/烟感/防火门 */
    @Schema(description = "设备类型")
    private String type;

    /** 安装位置 */
    @Schema(description = "安装位置")
    private String location;

    /** 区域ID */
    @Schema(description = "区域ID")
    private Long areaId;

    /** 过期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "过期日期")
    private LocalDate expireDate;

    /** 状态：0-停用 1-正常 */
    @Schema(description = "状态")
    private Integer status;

    /** 上次检查日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "上次检查日期")
    private LocalDate lastCheckDate;
}
