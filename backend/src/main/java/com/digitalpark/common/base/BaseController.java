package com.digitalpark.common.base;

import com.digitalpark.common.result.R;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 基础控制器
 *
 * @author digitalpark
 */
public class BaseController {

    /**
     * 获取当前请求对象
     */
    protected HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 获取客户端真实IP
     */
    protected String getClientIp() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多次反向代理后取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 成功返回
     */
    protected <T> R<T> success() {
        return R.ok();
    }

    /**
     * 成功返回（带数据）
     */
    protected <T> R<T> success(T data) {
        return R.ok(data);
    }

    /**
     * 成功返回（自定义消息）
     */
    protected <T> R<T> success(String msg, T data) {
        return R.ok(msg, data);
    }

    /**
     * 失败返回
     */
    protected <T> R<T> fail() {
        return R.fail();
    }

    /**
     * 失败返回（自定义消息）
     */
    protected <T> R<T> fail(String msg) {
        return R.fail(msg);
    }

    /**
     * 失败返回（自定义状态码和消息）
     */
    protected <T> R<T> fail(int code, String msg) {
        return R.fail(code, msg);
    }
}
