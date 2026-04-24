package com.digitalpark.modules.emergency.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 应急事件DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "应急事件请求")
public class EmergencyEventDTO {

    @Schema(description = "事件ID")
    private Long id;

    /** 事件标题 */
    @NotBlank(message = "事件标题不能为空")
    @Schema(description = "事件标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    /** 事件类型 */
    @NotBlank(message = "事件类型不能为空")
    @Schema(description = "事件类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    /** 事件等级：一般/较大/重大/特大 */
    @NotBlank(message = "事件等级不能为空")
    @Schema(description = "事件等级", requiredMode = Schema.RequiredMode.REQUIRED)
    private String level;

    /** 事件描述 */
    @Schema(description = "事件描述")
    private String description;

    /** 事件位置 */
    @Schema(description = "事件位置")
    private String location;

    /** 处理人ID */
    @Schema(description = "处理人ID")
    private Long handlerId;

    /** 处理结果 */
    @Schema(description = "处理结果")
    private String handleResult;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
