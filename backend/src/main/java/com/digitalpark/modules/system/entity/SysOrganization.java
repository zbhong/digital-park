package com.digitalpark.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统组织实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_organization")
@Schema(description = "系统组织实体")
public class SysOrganization extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父组织ID
     */
    @Schema(description = "父组织ID")
    private Long parentId;

    /**
     * 组织名称
     */
    @Schema(description = "组织名称")
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
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 状态（0-正常 1-禁用）
     */
    @Schema(description = "状态（0-正常 1-禁用）")
    private Integer status;

    /**
     * 子组织列表（非数据库字段）
     */
    @Schema(description = "子组织列表")
    @TableField(exist = false)
    private List<SysOrganization> children;
}
