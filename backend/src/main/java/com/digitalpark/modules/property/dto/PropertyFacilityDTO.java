package com.digitalpark.modules.property.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * 设施运维DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "设施运维请求")
public class PropertyFacilityDTO {

    @Schema(description = "设施ID")
    private Long id;

    @NotBlank(message = "设施名称不能为空")
    @Schema(description = "设施名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "设施类型不能为空")
    @Schema(description = "设施类型(电梯/空调/给排水/照明/环卫)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @Schema(description = "所在位置")
    private String location;

    @Schema(description = "区域ID")
    private Long areaId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "上次巡检日期")
    private LocalDate lastCheckDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "下次巡检日期")
    private LocalDate nextCheckDate;

    @Schema(description = "状态(正常/维修/报废)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
