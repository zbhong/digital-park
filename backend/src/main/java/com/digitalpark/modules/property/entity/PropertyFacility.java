package com.digitalpark.modules.property.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 设施运维实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("property_facility")
@Schema(description = "设施运维")
public class PropertyFacility extends BaseEntity {

    @Schema(description = "设施名称")
    private String name;

    @Schema(description = "设施类型(电梯/空调/给排水/照明/环卫)")
    private String type;

    @Schema(description = "所在位置")
    private String location;

    @Schema(description = "区域ID")
    private Long areaId;

    @Schema(description = "上次巡检日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastCheckDate;

    @Schema(description = "下次巡检日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextCheckDate;

    @Schema(description = "状态(正常/维修/报废)")
    private String status;
}
