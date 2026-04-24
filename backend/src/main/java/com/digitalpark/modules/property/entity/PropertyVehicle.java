package com.digitalpark.modules.property.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 车辆实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("property_vehicle")
@Schema(description = "车辆")
public class PropertyVehicle extends BaseEntity {

    @Schema(description = "车牌号")
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
}
