package com.digitalpark.modules.emergency.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 应急事件实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emergency_event")
@Schema(description = "应急事件")
public class EmergencyEvent extends BaseEntity {

    /** 事件标题 */
    @Schema(description = "事件标题")
    private String title;

    /** 事件类型 */
    @Schema(description = "事件类型")
    private String type;

    /** 事件等级：一般/较大/重大/特大 */
    @Schema(description = "事件等级")
    private String level;

    /** 事件描述 */
    @Schema(description = "事件描述")
    private String description;

    /** 事件位置 */
    @Schema(description = "事件位置")
    private String location;

    /** 处理状态：待响应/响应中/处置中/已完成 */
    @Schema(description = "处理状态")
    private String status;

    /** 处理人ID */
    @Schema(description = "处理人ID")
    private Long handlerId;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "处理时间")
    private LocalDateTime handleTime;

    /** 处理结果 */
    @Schema(description = "处理结果")
    private String handleResult;
}
