package com.digitalpark.modules.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 菜单创建/更新DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "菜单创建/更新请求")
public class MenuDTO {

    /**
     * 菜单ID（更新时必填）
     */
    @Schema(description = "菜单ID")
    private Long id;

    /**
     * 父菜单ID
     */
    @Schema(description = "父菜单ID")
    private Long parentId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    @Schema(description = "菜单名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuName;

    /**
     * 路由地址
     */
    @Schema(description = "路由地址")
    private String path;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径")
    private String component;

    /**
     * 权限标识
     */
    @Schema(description = "权限标识")
    private String perms;

    /**
     * 菜单图标
     */
    @Schema(description = "菜单图标")
    private String icon;

    /**
     * 菜单类型（C-目录 M-菜单 F-按钮）
     */
    @NotBlank(message = "菜单类型不能为空")
    @Schema(description = "菜单类型（C-目录 M-菜单 F-按钮）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String menuType;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sortOrder;

    /**
     * 是否可见（0-可见 1-隐藏）
     */
    @Schema(description = "是否可见（0-可见 1-隐藏）")
    private Integer visible;

    /**
     * 状态（0-正常 1-禁用）
     */
    @Schema(description = "状态（0-正常 1-禁用）")
    private Integer status;
}
