package com.digitalpark.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回结果类（带code/msg/data）
 *
 * @param <T> 数据类型
 * @author digitalpark
 */
@Data
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;

    public CommonResult() {
        this.timestamp = System.currentTimeMillis();
    }

    public CommonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<>(200, "操作成功", null);
    }

    /**
     * 成功（带数据）
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(200, "操作成功", data);
    }

    /**
     * 成功（自定义消息）
     */
    public static <T> CommonResult<T> success(String msg, T data) {
        return new CommonResult<>(200, msg, data);
    }

    /**
     * 失败
     */
    public static <T> CommonResult<T> error() {
        return new CommonResult<>(500, "操作失败", null);
    }

    /**
     * 失败（自定义消息）
     */
    public static <T> CommonResult<T> error(String msg) {
        return new CommonResult<>(500, msg, null);
    }

    /**
     * 失败（自定义状态码和消息）
     */
    public static <T> CommonResult<T> error(int code, String msg) {
        return new CommonResult<>(code, msg, null);
    }

    /**
     * 未授权
     */
    public static <T> CommonResult<T> unauthorized(String msg) {
        return new CommonResult<>(401, msg, null);
    }

    /**
     * 禁止访问
     */
    public static <T> CommonResult<T> forbidden(String msg) {
        return new CommonResult<>(403, msg, null);
    }

    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return this.code == 200;
    }
}
