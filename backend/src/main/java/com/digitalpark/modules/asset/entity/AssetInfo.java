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
 * 资产信息实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("asset_info")
@Schema(description = "资产信息")
public class AssetInfo extends BaseEntity {

    /** 资产名称 */
    @Schema(description = "资产名称")
    private String name;

    /** 分类ID */
    @Schema(description = "分类ID")
    private Long categoryId;

    /** 资产编码 */
    @Schema(description = "资产编码")
    private String assetCode;

    /** 资产类型：不动产/智能化设备/公共设施/管网/电力/安防/应急/环境监测 */
    @Schema(description = "资产类型")
    private String type;

    /** 品牌 */
    @Schema(description = "品牌")
    private String brand;

    /** 型号 */
    @Schema(description = "型号")
    private String model;

    /** 采购日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "采购日期")
    private LocalDate purchaseDate;

    /** 采购价格 */
    @Schema(description = "采购价格")
    private BigDecimal purchasePrice;

    /** 供应商 */
    @Schema(description = "供应商")
    private String supplier;

    /** 存放位置 */
    @Schema(description = "存放位置")
    private String location;

    /** 区域ID */
    @Schema(description = "区域ID")
    private Long areaId;

    /** 建筑ID */
    @Schema(description = "建筑ID")
    private Long buildingId;

    /** 楼层 */
    @Schema(description = "楼层")
    private String floor;

    /** 房间 */
    @Schema(description = "房间")
    private String room;

    /** 状态：在用/闲置/维修/报废 */
    @Schema(description = "状态")
    private String status;

    /** 保修到期日 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "保修到期日")
    private LocalDate warrantyExpire;
}
