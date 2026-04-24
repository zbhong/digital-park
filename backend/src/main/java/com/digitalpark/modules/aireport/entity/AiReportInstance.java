package com.digitalpark.modules.aireport.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 报告实例实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_report_instance")
@Schema(description = "报告实例")
public class AiReportInstance extends BaseEntity {

    @Schema(description = "模板ID")
    private Long templateId;

    @Schema(description = "报告标题")
    private String title;

    @Schema(description = "所属模块")
    private String module;

    @Schema(description = "报告类型")
    private String type;

    @Schema(description = "报告内容")
    private String content;

    @Schema(description = "生成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime generateTime;

    @Schema(description = "报告周期")
    private String period;

    @Schema(description = "状态(生成中/已完成/失败)")
    private String status;

    @Schema(description = "文件大小(KB)")
    private Long fileSize;
}
