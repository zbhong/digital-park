package com.digitalpark.modules.safety.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 网络安全事件DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "网络安全事件请求")
public class SafetyNetworkDTO {

    @Schema(description = "ID")
    private Long id;

    /** 类型：IP管理/威胁诱捕/设备监控/防私接 */
    @NotBlank(message = "类型不能为空")
    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    /** 事件类型 */
    @Schema(description = "事件类型")
    private String eventType;

    /** 源IP */
    @Schema(description = "源IP")
    private String sourceIp;

    /** 目标IP */
    @Schema(description = "目标IP")
    private String targetIp;

    /** 端口 */
    @Schema(description = "端口")
    private Integer port;

    /** 协议 */
    @Schema(description = "协议")
    private String protocol;

    /** 严重程度 */
    @Schema(description = "严重程度")
    private String severity;

    /** 描述 */
    @Schema(description = "描述")
    private String description;

    /** 处理结果 */
    @Schema(description = "处理结果")
    private String handleResult;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
