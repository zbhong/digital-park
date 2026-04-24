package com.digitalpark.modules.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客户DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "客户请求")
public class InvestmentCustomerDTO {

    @Schema(description = "客户ID")
    private Long id;

    @NotBlank(message = "客户名称不能为空")
    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "联系人不能为空")
    @Schema(description = "联系人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contactPerson;

    @NotBlank(message = "联系电话不能为空")
    @Schema(description = "联系电话", requiredMode = Schema.RequiredMode.REQUIRED)
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

    @Schema(description = "备注")
    private String remark;
}
