package com.digitalpark.modules.aireport.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 报告模板实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_report_template")
@Schema(description = "报告模板")
public class AiReportTemplate extends BaseEntity {

    @Schema(description = "模板名称")
    private String name;

    @Schema(description = "所属模块(安全/应急/能源/环境/经济/企业/物业/招商/设备/综合)")
    private String module;

    @Schema(description = "报告类型(日报/周报/月报/季报/年报/专项)")
    private String type;

    @Schema(description = "内容模板")
    private String contentTemplate;

    @Schema(description = "变量定义JSON")
    private String variables;

    @Schema(description = "生成周期(日/周/月/季/年)")
    private String cycle;

    @Schema(description = "状态(启用/禁用)")
    private String status;
}
