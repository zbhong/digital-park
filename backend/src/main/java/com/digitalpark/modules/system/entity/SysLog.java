package com.digitalpark.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作日志实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_log")
@Schema(description = "操作日志实体")
public class SysLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 模块名称
     */
    @Schema(description = "模块名称")
    private String module;

    /**
     * 操作类型
     */
    @Schema(description = "操作类型")
    private String operationType;

    /**
     * 操作方法
     */
    @Schema(description = "操作方法")
    private String method;

    /**
     * 请求URL
     */
    @Schema(description = "请求URL")
    private String requestUrl;

    /**
     * 请求方式
     */
    @Schema(description = "请求方式")
    private String requestMethod;

    /**
     * 请求参数
     */
    @Schema(description = "请求参数")
    private String requestParams;

    /**
     * 返回结果
     */
    @Schema(description = "返回结果")
    private String responseResult;

    /**
     * 操作IP
     */
    @Schema(description = "操作IP")
    private String ip;

    /**
     * 用户代理
     */
    @Schema(description = "用户代理")
    private String userAgent;

    /**
     * 耗时（毫秒）
     */
    @Schema(description = "耗时（毫秒）")
    private Long duration;

    /**
     * 操作状态（0-正常 1-异常）
     */
    @Schema(description = "操作状态（0-正常 1-异常）")
    private Integer status;

    /**
     * 错误消息
     */
    @Schema(description = "错误消息")
    private String errorMsg;
}
