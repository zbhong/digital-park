package com.digitalpark.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统菜单实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
@Schema(description = "系统菜单实体")
public class SysMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父菜单ID
     */
    @Schema(description = "父菜单ID")
    private Long parentId;

    /**
     * 菜单名称
     */
    @Schema(description = "菜单名称")
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
    @Schema(description = "菜单类型（C-目录 M-菜单 F-按钮）")
    private String menuType;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @TableField("sort_order")
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

    /**
     * 子菜单列表（非数据库字段）
     */
    @Schema(description = "子菜单列表")
    @TableField(exist = false)
    private List<SysMenu> children;
}
