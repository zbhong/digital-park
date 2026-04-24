package com.digitalpark.modules.investment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 招商政策实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("investment_policy")
@Schema(description = "招商政策")
public class InvestmentPolicy extends BaseEntity {

    @Schema(description = "政策名称")
    private String name;

    @Schema(description = "政策类型(产业扶持/税收优惠/租金减免/装修补贴)")
    private String type;

    @Schema(description = "适用行业范围")
    private String industryScope;

    @Schema(description = "适用企业规模")
    private String enterpriseScale;

    @Schema(description = "政策内容")
    private String content;

    @Schema(description = "状态(启用/禁用)")
    private String status;
}
