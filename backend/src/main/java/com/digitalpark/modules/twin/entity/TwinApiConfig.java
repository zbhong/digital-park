package com.digitalpark.modules.twin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口配置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("twin_api_config")
@Schema(description = "接口配置")
public class TwinApiConfig extends BaseEntity {

    @Schema(description = "配置名称")
    private String configName;

    @Schema(description = "接口类型(device/alert/environment/energy)")
    private String apiType;

    @Schema(description = "请求方式(GET/POST/PUT/DELETE)")
    private String method;

    @Schema(description = "请求URL")
    private String url;

    @Schema(description = "请求头JSON")
    private String headers;

    @Schema(description = "请求参数JSON")
    private String params;

    @Schema(description = "请求体模板")
    private String bodyTemplate;

    @Schema(description = "数据路径(如 data.list)")
    private String dataPath;

    @Schema(description = "刷新间隔(秒)")
    private Integer refreshInterval;

    @Schema(description = "超时时间(毫秒)")
    private Integer timeout;

    @Schema(description = "状态(0-禁用 1-启用)")
    private Integer status;
}
