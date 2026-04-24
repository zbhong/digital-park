package com.digitalpark.common.aspect;

import com.digitalpark.common.annotation.Log;
import com.digitalpark.common.utils.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 操作日志切面
 *
 * @author digitalpark
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final RedisUtils redisUtils;
    private final ObjectMapper objectMapper;

    /**
     * 切点：所有带有 @Log 注解的方法
     */
    @Pointcut("@annotation(com.digitalpark.common.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 环绕通知：记录操作日志
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取注解信息
        MethodSignature signature = (MethodSignature) point.getSignature();
        Log logAnnotation = signature.getMethod().getAnnotation(Log.class);

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        String module = logAnnotation.module();
        String operation = logAnnotation.description();
        String type = logAnnotation.type().name();
        String method = point.getTarget().getClass().getName() + "." + signature.getName() + "()";
        String ip = getClientIp(request);
        String url = request != null ? request.getRequestURI() : "";
        String httpMethod = request != null ? request.getMethod() : "";

        // 记录请求参数
        String params = "";
        if (logAnnotation.recordParams()) {
            params = getParams(point, signature);
        }

        Object result;
        try {
            // 执行目标方法
            result = point.proceed();

            // 计算耗时
            long elapsedTime = System.currentTimeMillis() - startTime;

            // 记录返回结果
            String resultJson = "";
            if (logAnnotation.recordResult() && result != null) {
                try {
                    resultJson = objectMapper.writeValueAsString(result);
                    // 限制日志长度
                    if (resultJson.length() > 2000) {
                        resultJson = resultJson.substring(0, 2000) + "...";
                    }
                } catch (Exception e) {
                    resultJson = "[序列化失败]";
                }
            }

            log.info("操作日志 - 模块: {}, 操作: {}, 类型: {}, 方法: {}, IP: {}, URL: {}, HTTP方法: {}, 参数: {}, 耗时: {}ms, 结果: {}",
                    module, operation, type, method, ip, url, httpMethod, params, elapsedTime, resultJson);

            return result;
        } catch (Exception e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.error("操作日志 - 模块: {}, 操作: {}, 类型: {}, 方法: {}, IP: {}, URL: {}, HTTP方法: {}, 参数: {}, 耗时: {}ms, 异常: {}",
                    module, operation, type, method, ip, url, httpMethod, params, elapsedTime, e.getMessage());
            throw e;
        }
    }

    /**
     * 获取请求参数
     */
    private String getParams(ProceedingJoinPoint point, MethodSignature signature) {
        try {
            String[] paramNames = signature.getParameterNames();
            Object[] args = point.getArgs();
            if (paramNames == null || paramNames.length == 0) {
                return "{}";
            }
            Map<String, Object> paramMap = new HashMap<>(8);
            for (int i = 0; i < paramNames.length; i++) {
                Object arg = args[i];
                if (arg instanceof MultipartFile) {
                    paramMap.put(paramNames[i], "file:" + ((MultipartFile) arg).getOriginalFilename());
                } else if (arg instanceof HttpServletRequest) {
                    paramMap.put(paramNames[i], "HttpServletRequest");
                } else {
                    paramMap.put(paramNames[i], arg);
                }
            }
            String json = objectMapper.writeValueAsString(paramMap);
            return json.length() > 2000 ? json.substring(0, 2000) + "..." : json;
        } catch (Exception e) {
            return "[参数解析失败]";
        }
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
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
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
