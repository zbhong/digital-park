package com.digitalpark.modules.asset.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 资产盘点DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "资产盘点请求")
public class AssetInventoryDTO {

    @Schema(description = "盘点ID")
    private Long id;

    /** 盘点标题 */
    @NotBlank(message = "盘点标题不能为空")
    @Schema(description = "盘点标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    /** 开始日期 */
    @Schema(description = "开始日期")
    private LocalDate startDate;

    /** 结束日期 */
    @Schema(description = "结束日期")
    private LocalDate endDate;

    /** 盘点资产ID列表 */
    @Schema(description = "盘点资产ID列表")
    private List<Long> assetIds;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;
}
