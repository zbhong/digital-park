package com.digitalpark.modules.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 招商政策DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "招商政策请求")
public class InvestmentPolicyDTO {

    @Schema(description = "政策ID")
    private Long id;

    @NotBlank(message = "政策名称不能为空")
    @Schema(description = "政策名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "政策类型不能为空")
    @Schema(description = "政策类型(产业扶持/税收优惠/租金减免/装修补贴)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @Schema(description = "适用行业范围")
    private String industryScope;

    @Schema(description = "适用企业规模")
    private String enterpriseScale;

    @Schema(description = "政策内容")
    private String content;

    @Schema(description = "状态(启用/禁用)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
