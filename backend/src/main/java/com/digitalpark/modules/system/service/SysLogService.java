package com.digitalpark.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.system.entity.SysLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志 Service 接口
 *
 * @author digitalpark
 */
public interface SysLogService {

    /**
     * 获取操作日志列表（分页）
     *
     * @param page      分页参数
     * @param module    模块名称
     * @param username  操作用户
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 分页结果
     */
    PageResult<?> getLogList(Page<?> page, String module, String username, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 删除操作日志
     *
     * @param ids 日志ID列表
     */
    void deleteLog(List<Long> ids);

    /**
     * 清空操作日志
     */
    void clearLog();

    /**
     * 根据ID获取操作日志详情
     *
     * @param id 日志ID
     * @return 操作日志
     */
    SysLog getLogById(Long id);
}
