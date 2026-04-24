package com.digitalpark.modules.economy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 经济报告实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("economy_report")
@Schema(description = "经济报告")
public class EconomyReport extends BaseEntity {

    @Schema(description = "报告标题")
    private String title;

    @Schema(description = "报告类型(月报/季报/年报)")
    private String type;

    @Schema(description = "报告周期")
    private String period;

    @Schema(description = "报告内容")
    private String content;

    @Schema(description = "状态(草稿/已发布)")
    private String status;
}
