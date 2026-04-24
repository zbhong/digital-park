package com.digitalpark.modules.enterprise.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

/**
 * 企业信息DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "企业信息请求")
public class EnterpriseInfoDTO {

    @Schema(description = "企业ID")
    private Long id;

    @NotBlank(message = "企业名称不能为空")
    @Schema(description = "企业名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "企业编码")
    private String code;

    @Schema(description = "所属行业")
    private String industry;

    @Schema(description = "企业规模")
    private String scale;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "区域ID")
    private Long areaId;

    @Schema(description = "楼栋ID")
    private Long buildingId;

    @Schema(description = "楼层")
    private String floor;

    @Schema(description = "房间号")
    private String room;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "入驻日期")
    private LocalDate enterDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "合同到期日")
    private LocalDate contractExpire;

    @Schema(description = "状态(在营/注销/迁出)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
