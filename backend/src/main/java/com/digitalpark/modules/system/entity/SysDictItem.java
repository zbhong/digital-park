package com.digitalpark.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统字典项实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_item")
@Schema(description = "系统字典项实体")
public class SysDictItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 字典ID
     */
    @Schema(description = "字典ID")
    private Long dictId;

    /**
     * 字典类型
     */
    @Schema(description = "字典类型")
    @TableField("dict_type")
    private String dictType;

    /**
     * 字典标签
     */
    @Schema(description = "字典标签")
    @TableField("item_label")
    private String label;

    /**
     * 字典值
     */
    @Schema(description = "字典值")
    @TableField("item_value")
    private String value;

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
}
