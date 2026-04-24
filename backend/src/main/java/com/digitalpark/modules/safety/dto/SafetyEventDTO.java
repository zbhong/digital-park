package com.digitalpark.modules.safety.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 安全事件创建/更新DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "安全事件创建/更新请求")
public class SafetyEventDTO {

    @Schema(description = "事件ID")
    private Long id;

    /** 事件类型：闯入/翻越/违规逗留/异常聚集/未戴安全帽/烟火/违规用电/堵塞消防通道 */
    @NotBlank(message = "事件类型不能为空")
    @Schema(description = "事件类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    /** 事件等级：一般/较大/重大/特大 */
    @NotBlank(message = "事件等级不能为空")
    @Schema(description = "事件等级", requiredMode = Schema.RequiredMode.REQUIRED)
    private String level;

    /** 事件来源 */
    @Schema(description = "事件来源")
    private String source;

    /** 事件描述 */
    @Schema(description = "事件描述")
    private String description;

    /** 事件位置 */
    @Schema(description = "事件位置")
    private String location;

    /** 关联监测点位ID */
    @Schema(description = "监测点位ID")
    private Long monitorPointId;

    /** 事件图片，多个逗号分隔 */
    @Schema(description = "事件图片")
    private String images;

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
