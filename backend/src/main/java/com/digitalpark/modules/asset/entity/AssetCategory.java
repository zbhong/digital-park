package com.digitalpark.modules.asset.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资产分类实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("asset_category")
@Schema(description = "资产分类")
public class AssetCategory extends BaseEntity {

    /** 分类名称 */
    @Schema(description = "分类名称")
    private String name;

    /** 父级ID */
    @Schema(description = "父级ID")
    private Long parentId;

    /** 分类编码 */
    @Schema(description = "分类编码")
    private String code;

    /** 排序 */
    @Schema(description = "排序")
    private Integer sort;
}
