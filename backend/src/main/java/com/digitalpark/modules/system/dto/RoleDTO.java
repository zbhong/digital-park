package com.digitalpark.modules.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 角色创建/更新DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "角色创建/更新请求")
public class RoleDTO {

    /**
     * 角色ID（更新时必填）
     */
    @Schema(description = "角色ID")
    private Long id;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleName;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @Schema(description = "角色编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roleCode;

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
     * 菜单ID列表
     */
    @Schema(description = "菜单ID列表")
    private List<Long> menuIds;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
