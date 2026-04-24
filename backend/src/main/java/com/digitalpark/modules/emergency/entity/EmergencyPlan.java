package com.digitalpark.modules.emergency.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 应急预案实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("emergency_plan")
@Schema(description = "应急预案")
public class EmergencyPlan extends BaseEntity {

    /** 预案名称 */
    @Schema(description = "预案名称")
    private String name;

    /** 预案类型：火灾/电力故障/极端天气/环境污染/生产安全 */
    @Schema(description = "预案类型")
    private String type;

    /** 预案内容 */
    @Schema(description = "预案内容")
    private String content;

    /** 附件 */
    @Schema(description = "附件")
    private String attachment;

    /** 状态：草稿/已发布/已废止 */
    @Schema(description = "状态")
    private String status;
}
