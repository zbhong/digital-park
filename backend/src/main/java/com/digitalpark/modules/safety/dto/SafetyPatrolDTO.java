package com.digitalpark.modules.safety.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 安全巡检创建DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "安全巡检创建请求")
public class SafetyPatrolDTO {

    /** 巡检路线ID */
    @NotNull(message = "巡检路线不能为空")
    @Schema(description = "巡检路线ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long routeId;

    /** 巡检类型 */
    @NotBlank(message = "巡检类型不能为空")
    @Schema(description = "巡检类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String patrolType;

    /** 巡检人 */
    @NotBlank(message = "巡检人不能为空")
    @Schema(description = "巡检人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String patrolPerson;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    /** 巡检结果 */
    @Schema(description = "巡检结果")
    private String result;

    /** 异常数量 */
    @Schema(description = "异常数量")
    private Integer abnormalCount;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
