package com.digitalpark.modules.safety.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网络安全实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("safety_network")
@Schema(description = "网络安全")
public class SafetyNetwork extends BaseEntity {

    /** 类型：IP管理/威胁诱捕/设备监控/防私接 */
    @Schema(description = "类型")
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

    /** 状态 */
    @Schema(description = "状态")
    private String status;

    /** 处理结果 */
    @Schema(description = "处理结果")
    private String handleResult;
}
