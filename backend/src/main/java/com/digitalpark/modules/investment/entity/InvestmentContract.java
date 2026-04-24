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
 * 合同实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("investment_contract")
@Schema(description = "合同")
public class InvestmentContract extends BaseEntity {

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "房源ID")
    private Long assetId;

    @Schema(description = "合同编号")
    private String contractNo;

    @Schema(description = "开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(description = "租金(元/月)")
    private BigDecimal rentAmount;

    @Schema(description = "押金")
    private BigDecimal deposit;

    @Schema(description = "状态(草稿/审批中/生效中/已终止)")
    private String status;
}
