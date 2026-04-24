package com.digitalpark.modules.investment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 招商房源实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("investment_asset")
@Schema(description = "招商房源")
public class InvestmentAsset extends BaseEntity {

    @Schema(description = "楼栋ID")
    private Long buildingId;

    @Schema(description = "楼层")
    private String floor;

    @Schema(description = "房间号")
    private String roomNumber;

    @Schema(description = "面积(平方米)")
    private BigDecimal area;

    @Schema(description = "单价(元/平方米)")
    private BigDecimal pricePerSqm;

    @Schema(description = "状态(可租/已租/预留/不可租)")
    private String status;

    @Schema(description = "企业ID")
    private Long enterpriseId;
}
