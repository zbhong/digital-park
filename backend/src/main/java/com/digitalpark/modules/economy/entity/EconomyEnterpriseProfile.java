package com.digitalpark.modules.economy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 企业画像实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("economy_enterprise_profile")
@Schema(description = "企业画像")
public class EconomyEnterpriseProfile extends BaseEntity {

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "所属行业")
    private String industry;

    @Schema(description = "企业规模")
    private String scale;

    @Schema(description = "营收(万元)")
    private BigDecimal revenue;

    @Schema(description = "税收(万元)")
    private BigDecimal tax;

    @Schema(description = "员工人数")
    private Integer employees;

    @Schema(description = "信用评级")
    private String creditRating;

    @Schema(description = "风险等级")
    private String riskLevel;

    @Schema(description = "标签JSON")
    private String tagsJson;
}
