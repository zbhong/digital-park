package com.digitalpark.modules.emergency.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 应急物资实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emergency_material")
@Schema(description = "应急物资")
public class EmergencyMaterial extends BaseEntity {

    /** 物资名称 */
    @Schema(description = "物资名称")
    private String name;

    /** 物资分类：防护用品/救援工具/医疗物资/通讯设备/其他 */
    @Schema(description = "物资分类")
    private String category;

    /** 数量 */
    @Schema(description = "数量")
    private Integer quantity;

    /** 单位 */
    @Schema(description = "单位")
    private String unit;

    /** 存放位置 */
    @Schema(description = "存放位置")
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
}
