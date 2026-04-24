package com.digitalpark.modules.energy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 能源记录查询DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "能源记录查询条件")
public class EnergyRecordDTO {

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "计量表ID列表")
    private List<Long> meterIds;

    @Schema(description = "区域ID")
    private Long areaId;

    @Schema(description = "能源类型")
    private String energyType;

    @Schema(description = "统计维度(time/area/type)")
    private String groupBy;

    @Schema(description = "时间粒度(hour/day/month/year)")
    private String granularity;

    @Schema(description = "页码")
    private Integer pageNum = 1;

    @Schema(description = "每页大小")
    private Integer pageSize = 10;
}
