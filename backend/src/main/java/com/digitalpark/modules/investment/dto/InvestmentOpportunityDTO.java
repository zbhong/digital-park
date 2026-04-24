package com.digitalpark.modules.investment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 商机DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "商机请求")
public class InvestmentOpportunityDTO {

    @Schema(description = "商机ID")
    private Long id;

    @NotNull(message = "客户ID不能为空")
    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long customerId;

    @Schema(description = "阶段(初次接洽/实地考察/价格谈判/签约入驻)")
    private String stage;

    @Schema(description = "需求面积(平方米)")
    private BigDecimal areaDemand;

    @Schema(description = "预算范围")
    private String budgetRange;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "预计入驻日期")
    private LocalDate expectedDate;

    @Schema(description = "成交概率(%)")
    private Integer probability;

    @Schema(description = "备注")
    private String remark;
}
