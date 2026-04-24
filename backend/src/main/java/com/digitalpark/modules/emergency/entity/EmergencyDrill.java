package com.digitalpark.modules.emergency.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 应急演练实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emergency_drill")
@Schema(description = "应急演练")
public class EmergencyDrill extends BaseEntity {

    /** 预案ID */
    @Schema(description = "预案ID")
    private Long planId;

    /** 演练名称 */
    @Schema(description = "演练名称")
    private String name;

    /** 演练日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "演练日期")
    private LocalDate drillDate;

    /** 演练地点 */
    @Schema(description = "演练地点")
    private String location;

    /** 参与人数 */
    @Schema(description = "参与人数")
    private Integer participants;

    /** 演练结果 */
    @Schema(description = "演练结果")
    private String result;

    /** 演练总结 */
    @Schema(description = "演练总结")
    private String summary;
}
