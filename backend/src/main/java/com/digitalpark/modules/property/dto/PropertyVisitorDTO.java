package com.digitalpark.modules.property.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * 访客DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "访客请求")
public class PropertyVisitorDTO {

    @Schema(description = "访客ID")
    private Long id;

    @NotBlank(message = "访客姓名不能为空")
    @Schema(description = "访客姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "拜访企业")
    private String visitEnterprise;

    @Schema(description = "拜访人")
    private String visitPerson;

    @Schema(description = "来访目的")
    private String purpose;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "来访日期")
    private LocalDate visitDate;

    @Schema(description = "状态(待审核/已通过/已拒绝/已离园)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
