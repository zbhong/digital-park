package com.digitalpark.modules.enterprise.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 服务请求DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "服务请求")
public class EnterpriseServiceRequestDTO {

    @Schema(description = "请求ID")
    private Long id;

    @NotNull(message = "企业ID不能为空")
    @Schema(description = "企业ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long enterpriseId;

    @NotBlank(message = "服务类型不能为空")
    @Schema(description = "服务类型(政策申报/法律咨询/金融服务/物业报修/能耗咨询/环境指导)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @NotBlank(message = "标题不能为空")
    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态(待处理/处理中/已完成/已关闭)")
    private String status;

    @Schema(description = "处理人ID")
    private Long handlerId;

    @Schema(description = "处理结果")
    private String handleResult;

    @Schema(description = "备注")
    private String remark;
}
