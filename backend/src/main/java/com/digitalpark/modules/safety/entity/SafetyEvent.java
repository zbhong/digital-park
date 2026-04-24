package com.digitalpark.modules.safety.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 安全事件实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("safety_event")
@Schema(description = "安全事件")
public class SafetyEvent extends BaseEntity {

    /** 事件类型：闯入/翻越/违规逗留/异常聚集/未戴安全帽/烟火/违规用电/堵塞消防通道 */
    @Schema(description = "事件类型")
    private String type;

    /** 事件等级：一般/较大/重大/特大 */
    @Schema(description = "事件等级")
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

    /** 处理状态：待处理/处理中/已处理/已关闭 */
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

    /** 事件图片，多个逗号分隔 */
    @Schema(description = "事件图片")
    private String images;
}
