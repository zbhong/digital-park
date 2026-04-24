package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 告警记录实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_alert")
@Schema(description = "告警记录")
public class TwinAlert extends BaseEntity {

    @Schema(description = "告警标题")
    private String title;

    @Schema(description = "告警类型(安全/设备/环境/能源/消防)")
    private String type;

    @Schema(description = "告警级别(提示/一般/较重/严重/紧急)")
    private String level;

    @Schema(description = "告警来源")
    private String source;

    @Schema(description = "告警描述")
    private String description;

    @Schema(description = "告警位置")
    private String location;

    @Schema(description = "处理状态(待处理/处理中/已处理/已关闭)")
    private String status;

    @Schema(description = "处理人ID")
    private Long handlerId;

    @Schema(description = "处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;

    @Schema(description = "处理结果")
    private String handleResult;
}
