package com.digitalpark.modules.asset.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资产文档实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("asset_document")
@Schema(description = "资产文档")
public class AssetDocument extends BaseEntity {

    /** 资产ID */
    @Schema(description = "资产ID")
    private Long assetId;

    /** 文档名称 */
    @Schema(description = "文档名称")
    private String name;

    /** 文档类型：CAD图纸/产权证明/说明书/检测报告/竣工资料 */
    @Schema(description = "文档类型")
    private String type;

    /** 文件地址 */
    @Schema(description = "文件地址")
    private String fileUrl;

    /** 版本号 */
    @Schema(description = "版本号")
    private String version;
}
