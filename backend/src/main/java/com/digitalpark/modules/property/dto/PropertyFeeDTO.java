package com.digitalpark.modules.property.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 物业费DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "物业费请求")
public class PropertyFeeDTO {

    @Schema(description = "费用ID")
    private Long id;

    @NotNull(message = "企业ID不能为空")
    @Schema(description = "企业ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long enterpriseId;

    @NotBlank(message = "费用类型不能为空")
    @Schema(description = "费用类型(物业费/水电费/停车费/租赁费)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String feeType;

    @NotNull(message = "金额不能为空")
    @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;

    @Schema(description = "账单周期")
    private String period;

    @Schema(description = "状态(待缴/已缴/逾期)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
