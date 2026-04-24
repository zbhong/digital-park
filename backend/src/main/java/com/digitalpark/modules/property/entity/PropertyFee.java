package com.digitalpark.modules.property.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物业费实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("property_fee")
@Schema(description = "物业费")
public class PropertyFee extends BaseEntity {

    @Schema(description = "企业ID")
    private Long enterpriseId;

    @Schema(description = "费用类型(物业费/水电费/停车费/租赁费)")
    private String feeType;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "账单周期")
    private String period;

    @Schema(description = "状态(待缴/已缴/逾期)")
    private String status;

    @Schema(description = "缴费时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;
}
