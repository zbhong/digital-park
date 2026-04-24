package com.digitalpark.modules.investment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 商机实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("investment_opportunity")
@Schema(description = "商机")
public class InvestmentOpportunity extends BaseEntity {

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "阶段(初次接洽/实地考察/价格谈判/签约入驻)")
    private String stage;

    @Schema(description = "需求面积(平方米)")
    private BigDecimal areaDemand;

    @Schema(description = "预算范围")
    private String budgetRange;

    @Schema(description = "预计入驻日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectedDate;

    @Schema(description = "成交概率(%)")
    private Integer probability;

    @Schema(description = "备注")
    private String remark;
}
