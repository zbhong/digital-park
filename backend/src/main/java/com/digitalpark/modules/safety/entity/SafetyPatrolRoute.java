package com.digitalpark.modules.safety.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡检路线实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("safety_patrol_route")
@Schema(description = "巡检路线")
public class SafetyPatrolRoute extends BaseEntity {

    /** 路线名称 */
    @Schema(description = "路线名称")
    private String name;

    /** 巡检点位JSON */
    @Schema(description = "巡检点位JSON")
    private String pointsJson;

    /** 巡检周期 */
    @Schema(description = "巡检周期")
    private String cycle;
}
