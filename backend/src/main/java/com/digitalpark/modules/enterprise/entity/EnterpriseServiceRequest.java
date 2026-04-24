package com.digitalpark.modules.enterprise.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 服务请求实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enterprise_service_request")
@Schema(description = "服务请求")
public class EnterpriseServiceRequest extends BaseEntity {

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "服务类型(政策申报/法律咨询/金融服务/物业报修/能耗咨询/环境指导)")
    private String type;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "状态(待处理/处理中/已完成/已关闭)")
    private String status;

    @Schema(description = "处理人ID")
    private Long handlerId;

    @Schema(description = "处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;

    @Schema(description = "处理结果")
    private String handleResult;
}
