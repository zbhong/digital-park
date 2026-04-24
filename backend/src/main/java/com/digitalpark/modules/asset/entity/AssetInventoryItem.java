package com.digitalpark.modules.asset.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 盘点明细实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("asset_inventory_item")
@Schema(description = "盘点明细")
public class AssetInventoryItem extends BaseEntity {

    /** 盘点ID */
    @Schema(description = "盘点ID")
    private Long inventoryId;

    /** 资产ID */
    @Schema(description = "资产ID")
    private Long assetId;

    /** 实际数量 */
    @Schema(description = "实际数量")
    private Integer actualQuantity;

    /** 状态：正常/盘盈/盘亏/异常 */
    @Schema(description = "状态")
    private String status;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
