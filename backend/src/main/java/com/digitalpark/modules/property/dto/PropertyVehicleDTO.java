package com.digitalpark.modules.property.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 车辆DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "车辆请求")
public class PropertyVehicleDTO {

    @Schema(description = "车辆ID")
    private Long id;

    @NotBlank(message = "车牌号不能为空")
    @Schema(description = "车牌号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String plateNumber;

    @Schema(description = "车主姓名")
    private String ownerName;

    @Schema(description = "车主类型(企业/访客/员工)")
    private String ownerType;

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "卡类型(月卡/临时)")
    private String cardType;

    @Schema(description = "状态(正常/过期/禁用)")
    private String status;

    @Schema(description = "备注")
    private String remark;
}
