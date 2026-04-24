package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 告警规则实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_alert_rule")
@Schema(description = "告警规则")
public class TwinAlertRule extends BaseEntity {

    @Schema(description = "规则名称")
    private String ruleName;

    @Schema(description = "规则类型(threshold/range/offline/frequency)")
    private String ruleType;

    @Schema(description = "关联点位类型")
    private String pointType;

    @Schema(description = "关联接口配置ID")
    private Long apiConfigId;

    @Schema(description = "条件表达式JSON")
    private String conditionExpr;

    @Schema(description = "告警级别(info/warning/error/critical)")
    private String alertLevel;

    @Schema(description = "告警颜色")
    private String alertColor;

    @Schema(description = "通知方式JSON")
    private String notifyMethod;

    @Schema(description = "告警持续时间(秒)")
    private Integer duration;

    @Schema(description = "静默期(秒)")
    private Integer silencePeriod;

    @Schema(description = "状态(0-禁用 1-启用)")
    private Integer status;
}
