package com.digitalpark.modules.investment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 跟进记录DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "跟进记录请求")
public class InvestmentFollowDTO {

    @Schema(description = "跟进记录ID")
    private Long id;

    @NotNull(message = "客户ID不能为空")
    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long customerId;

    @Schema(description = "商机ID")
    private Long opportunityId;

    @NotBlank(message = "跟进内容不能为空")
    @Schema(description = "跟进内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "跟进方式(电话/拜访/微信/邮件)")
    private String followType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "下次跟进日期")
    private LocalDate nextFollowDate;

    @Schema(description = "备注")
    private String remark;
}
