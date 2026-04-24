package com.digitalpark.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.digitalpark.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录日志实体
 *
 * @author digitalpark
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_login_log")
@Schema(description = "登录日志实体")
public class SysLoginLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 登录IP
     */
    @Schema(description = "登录IP")
    private String ip;

    /**
     * 登录地点
     */
    @Schema(description = "登录地点")
    private String location;

    /**
     * 浏览器
     */
    @Schema(description = "浏览器")
    private String browser;

    /**
     * 操作系统
     */
    @Schema(description = "操作系统")
    private String os;

    /**
     * 登录状态（0-成功 1-失败）
     */
    @Schema(description = "登录状态（0-成功 1-失败）")
    private Integer status;

    /**
     * 提示消息
     */
    @Schema(description = "提示消息")
    private String message;
}
