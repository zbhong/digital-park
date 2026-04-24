package com.digitalpark.modules.safety.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 安全巡检实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("safety_patrol")
@Schema(description = "安全巡检")
public class SafetyPatrol extends BaseEntity {

    /** 巡检路线ID */
    @Schema(description = "巡检路线ID")
    private Long routeId;

    /** 巡检类型 */
    @Schema(description = "巡检类型")
    private String patrolType;

    /** 巡检人 */
    @Schema(description = "巡检人")
    private String patrolPerson;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    /** 巡检状态 */
    @Schema(description = "巡检状态")
    private String status;

    /** 巡检结果 */
    @Schema(description = "巡检结果")
    private String result;

    /** 异常数量 */
    @Schema(description = "异常数量")
    private Integer abnormalCount;
}
