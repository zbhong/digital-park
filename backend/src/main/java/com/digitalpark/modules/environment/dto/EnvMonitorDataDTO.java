package com.digitalpark.modules.environment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 环境监测数据DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "环境监测数据查询条件")
public class EnvMonitorDataDTO {

    /** 监测点位ID列表 */
    @Schema(description = "监测点位ID列表")
    private List<Long> pointIds;

    /** 区域ID */
    @Schema(description = "区域ID")
    private Long areaId;

    /** 点位类型 */
    @Schema(description = "点位类型")
    private String pointType;

    /** 开始时间 */
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    /** 结束时间 */
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    /** 指标名称 */
    @Schema(description = "指标名称")
    private String indicator;
}
