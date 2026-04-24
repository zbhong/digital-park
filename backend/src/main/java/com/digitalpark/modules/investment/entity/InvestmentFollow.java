package com.digitalpark.modules.investment.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 跟进记录实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("investment_follow")
@Schema(description = "跟进记录")
public class InvestmentFollow extends BaseEntity {

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "商机ID")
    private Long opportunityId;

    @Schema(description = "跟进内容")
    private String content;

    @Schema(description = "跟进方式(电话/拜访/微信/邮件)")
    private String followType;

    @Schema(description = "下次跟进日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nextFollowDate;
}
