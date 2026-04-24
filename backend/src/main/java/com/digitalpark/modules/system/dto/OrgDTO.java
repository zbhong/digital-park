package com.digitalpark.modules.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 组织创建/更新DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "组织创建/更新请求")
public class OrgDTO {

    /**
     * 组织ID（更新时必填）
     */
    @Schema(description = "组织ID")
    private Long id;

    /**
     * 父组织ID
     */
    @Schema(description = "父组织ID")
    private Long parentId;

    /**
     * 组织名称
     */
    @NotBlank(message = "组织名称不能为空")
    @Schema(description = "组织名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orgName;

    /**
     * 组织编码
     */
    @Schema(description = "组织编码")
    private String orgCode;

    /**
     * 负责人
     */
    @Schema(description = "负责人")
    private String leader;

    /**
     * 联系电话
     */
    @Schema(description = "联系电话")
    private String phone;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sortOrder;

    /**
     * 状态（0-正常 1-禁用）
     */
    @Schema(description = "状态（0-正常 1-禁用）")
    private Integer status;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
