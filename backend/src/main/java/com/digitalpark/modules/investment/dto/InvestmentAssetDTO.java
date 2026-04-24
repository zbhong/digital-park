package com.digitalpark.modules.investment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 招商房源DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "招商房源请求")
public class InvestmentAssetDTO {

    @Schema(description = "房源ID")
    private Long id;

    @NotNull(message = "楼栋ID不能为空")
    @Schema(description = "楼栋ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long buildingId;

    @Schema(description = "楼层")
    private String floor;

    @NotBlank(message = "房间号不能为空")
    @Schema(description = "房间号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roomNumber;

    @NotNull(message = "面积不能为空")
    @Schema(description = "面积(平方米)", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal area;

    @Schema(description = "单价(元/平方米)")
    private BigDecimal pricePerSqm;

    @Schema(description = "状态(可租/已租/预留/不可租)")
    private String status;

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "备注")
    private String remark;
}
