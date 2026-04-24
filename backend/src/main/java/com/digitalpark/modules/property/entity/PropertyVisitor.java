package com.digitalpark.modules.property.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 访客实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("property_visitor")
@Schema(description = "访客")
public class PropertyVisitor extends BaseEntity {

    @Schema(description = "访客姓名")
    private String name;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "拜访企业")
    private String visitEnterprise;

    @Schema(description = "拜访人")
    private String visitPerson;

    @Schema(description = "来访目的")
    private String purpose;

    @Schema(description = "来访日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate visitDate;

    @Schema(description = "状态(待审核/已通过/已拒绝/已离园)")
    private String status;
}
