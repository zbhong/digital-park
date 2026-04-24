package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 配置操作日志实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_config_log")
@Schema(description = "配置操作日志")
public class TwinConfigLog extends BaseEntity {

    @Schema(description = "功能模块(basemap/icon/point/style/api/alertrule/version)")
    private String module;

    @Schema(description = "操作类型(create/update/delete/publish/rollback/import/export)")
    private String actionType;

    @Schema(description = "操作详情")
    private String detail;

    @Schema(description = "操作对象ID")
    private Long targetId;

    @Schema(description = "操作对象名称")
    private String targetName;

    @Schema(description = "操作人")
    private String operator;

    @Schema(description = "操作IP")
    private String ip;
}
