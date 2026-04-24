package com.digitalpark.modules.property.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 物业工单实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("property_work_order")
@Schema(description = "物业工单")
public class PropertyWorkOrder extends BaseEntity {

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "工单类型(保洁/绿化/安保/会务/维修/其他)")
    private String type;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "优先级(低/中/高/紧急)")
    private String priority;

    @Schema(description = "状态(待派单/已派单/处理中/已完成/已评价)")
    private String status;

    @Schema(description = "处理人ID")
    private Long handlerId;

    @Schema(description = "处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;

    @Schema(description = "处理结果")
    private String handleResult;

    @Schema(description = "满意度评分")
    private Integer satisfaction;
}
