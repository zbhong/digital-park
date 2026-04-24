package com.digitalpark.modules.asset.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 资产维保实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("asset_maintenance")
@Schema(description = "资产维保")
public class AssetMaintenance extends BaseEntity {

    /** 资产ID */
    @Schema(description = "资产ID")
    private Long assetId;

    /** 维保类型：日常/定期/故障 */
    @Schema(description = "维保类型")
    private String type;

    /** 计划日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "计划日期")
    private LocalDate planDate;

    /** 实际日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "实际日期")
    private LocalDate actualDate;

    /** 维保内容 */
    @Schema(description = "维保内容")
    private String content;

    /** 维保费用 */
    @Schema(description = "维保费用")
    private BigDecimal cost;

    /** 执行人 */
    @Schema(description = "执行人")
    private String executor;

    /** 维保结果 */
    @Schema(description = "维保结果")
    private String result;
}
