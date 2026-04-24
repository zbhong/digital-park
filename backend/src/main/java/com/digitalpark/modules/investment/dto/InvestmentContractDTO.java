package com.digitalpark.modules.investment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 合同DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "合同请求")
public class InvestmentContractDTO {

    @Schema(description = "合同ID")
    private Long id;

    @NotNull(message = "客户ID不能为空")
    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long customerId;

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "房源ID")
    private Long assetId;

    @NotBlank(message = "合同编号不能为空")
    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractNo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "开始日期")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "结束日期")
    private LocalDate endDate;

    @Schema(description = "租金(元/月)")
    private BigDecimal rentAmount;

    @Schema(description = "押金")
    private BigDecimal deposit;

    @Schema(description = "状态(草稿/审批中/生效中/已终止)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
