package com.digitalpark.modules.investment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 客户实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("investment_customer")
@Schema(description = "客户")
public class InvestmentCustomer extends BaseEntity {

    @Schema(description = "客户名称")
    private String name;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "公司")
    private String company;

    @Schema(description = "行业")
    private String industry;

    @Schema(description = "需求面积(平方米)")
    private BigDecimal demandArea;

    @Schema(description = "预算范围")
    private String demandBudget;

    @Schema(description = "来源")
    private String source;

    @Schema(description = "状态(公海/跟进中/已签约/已流失)")
    private String status;

    @Schema(description = "跟进人ID")
    private Long followUserId;
}
