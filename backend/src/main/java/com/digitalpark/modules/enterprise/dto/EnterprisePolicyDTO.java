package com.digitalpark.modules.enterprise.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * 政策信息DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "政策信息请求")
public class EnterprisePolicyDTO {

    @Schema(description = "政策ID")
    private Long id;

    @NotBlank(message = "政策标题不能为空")
    @Schema(description = "政策标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @NotBlank(message = "政策类型不能为空")
    @Schema(description = "政策类型(产业扶持/税收优惠/人才政策/科技创新)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @Schema(description = "适用行业范围")
    private String industryScope;

    @Schema(description = "适用企业规模")
    private String enterpriseScale;

    @Schema(description = "政策内容")
    private String content;

    @Schema(description = "附件")
    private String attachment;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "发布日期")
    private LocalDate publishDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "过期日期")
    private LocalDate expireDate;

    @Schema(description = "状态(草稿/已发布/已过期)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
