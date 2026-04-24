package com.digitalpark.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;

import java.util.List;

/**
 * 登录日志 Service 接口
 *
 * @author digitalpark
 */
public interface SysLoginLogService {

    /**
     * 获取登录日志列表（分页）
     *
     * @param page     分页参数
     * @param username 用户名
     * @param status   登录状态
     * @return 分页结果
     */
    PageResult<?> getLoginLogList(Page<?> page, String username, Integer status);

    /**
     * 删除登录日志
     *
     * @param ids 日志ID列表
     */
    void deleteLoginLog(List<Long> ids);

    /**
     * 记录登录日志
     *
     * @param username 用户名
     * @param ip       登录IP
     * @param status   登录状态（0-成功 1-失败）
     * @param message  提示消息
     */
    void recordLoginLog(String username, String ip, Integer status, String message);

    /**
     * 清空登录日志
     */
    void clearLoginLog();

    /**
     * 解锁用户（清除登录失败锁定状态）
     *
     * @param username 用户名
     */
    void unlockUser(String username);
}
