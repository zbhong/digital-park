package com.digitalpark.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.system.entity.SysConfig;

/**
 * 系统配置 Service 接口
 *
 * @author digitalpark
 */
public interface SysConfigService {

    /**
     * 获取配置列表（分页）
     *
     * @param page       分页参数
     * @param configName 配置名称
     * @return 分页结果
     */
    PageResult<SysConfig> getConfigList(Page<?> page, String configName);

    /**
     * 创建配置
     *
     * @param config 配置信息
     */
    void createConfig(SysConfig config);

    /**
     * 更新配置
     *
     * @param config 配置信息
     */
    void updateConfig(SysConfig config);

    /**
     * 删除配置
     *
     * @param id 配置ID
     */
    void deleteConfig(Long id);

    /**
     * 根据key获取配置值（带缓存）
     *
     * @param key 配置键
     * @return 配置值
     */
    String getConfigByKey(String key);

    /**
     * 根据ID获取配置详情
     *
     * @param id 配置ID
     * @return 配置信息
     */
    SysConfig getConfigById(Long id);

    /**
     * 刷新配置缓存
     */
    void refreshCache();
}
