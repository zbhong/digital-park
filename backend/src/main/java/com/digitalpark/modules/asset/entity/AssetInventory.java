package com.digitalpark.modules.asset.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 资产盘点实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("asset_inventory")
@Schema(description = "资产盘点")
public class AssetInventory extends BaseEntity {

    /** 盘点标题 */
    @Schema(description = "盘点标题")
    private String title;

    /** 状态：计划中/进行中/已完成 */
    @Schema(description = "状态")
    private String status;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "开始日期")
    private LocalDate startDate;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "结束日期")
    private LocalDate endDate;

    /** 创建人ID */
    @Schema(description = "创建人ID")
    private Long creatorId;

    /** 盘点结果 */
    @Schema(description = "盘点结果")
    private String result;
}
