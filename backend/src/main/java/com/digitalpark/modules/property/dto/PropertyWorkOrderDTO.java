package com.digitalpark.modules.property.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 物业工单DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "物业工单请求")
public class PropertyWorkOrderDTO {

    @Schema(description = "工单ID")
    private Long id;

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @NotBlank(message = "工单类型不能为空")
    @Schema(description = "工单类型(保洁/绿化/安保/会务/维修/其他)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @NotBlank(message = "标题不能为空")
    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "优先级(低/中/高/紧急)")
    private String priority;

    @Schema(description = "状态(待派单/已派单/处理中/已完成/已评价)")
    private String status;

    @Schema(description = "处理人ID")
    private Long handlerId;

    @Schema(description = "满意度评分")
    private Integer satisfaction;

    @Schema(description = "备注")
    private String remark;
}
