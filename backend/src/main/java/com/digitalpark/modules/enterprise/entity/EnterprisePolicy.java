package com.digitalpark.modules.enterprise.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 政策信息实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("enterprise_policy")
@Schema(description = "政策信息")
public class EnterprisePolicy extends BaseEntity {

    @Schema(description = "政策标题")
    private String title;

    @Schema(description = "政策类型(产业扶持/税收优惠/人才政策/科技创新)")
    private String type;

    @Schema(description = "适用行业范围")
    private String industryScope;

    @Schema(description = "适用企业规模")
    private String enterpriseScale;

    @Schema(description = "政策内容")
    private String content;

    @Schema(description = "附件")
    private String attachment;

    @Schema(description = "发布日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    @Schema(description = "过期日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expireDate;

    @Schema(description = "状态(草稿/已发布/已过期)")
    private String status;
}
