package com.digitalpark.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.utils.RedisUtils;
import com.digitalpark.modules.system.entity.SysConfig;
import com.digitalpark.modules.system.mapper.SysConfigMapper;
import com.digitalpark.modules.system.service.SysConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 系统配置 Service 实现
 *
 * @author digitalpark
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigMapper sysConfigMapper;
    private final RedisUtils redisUtils;

    private static final String CONFIG_CACHE_PREFIX = "config:";
    private static final long CONFIG_CACHE_HOURS = 2;

    @Override
    public PageResult<SysConfig> getConfigList(Page<?> page, String configName) {
        LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(configName)) {
            wrapper.like(SysConfig::getConfigName, configName);
        }
        wrapper.orderByDesc(SysConfig::getCreateTime);

        Page<SysConfig> configPage = sysConfigMapper.selectPage((Page<SysConfig>) page, wrapper);
        return PageResult.of(configPage.getRecords(), configPage.getTotal(), configPage.getCurrent(), configPage.getSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createConfig(SysConfig config) {
        // 校验配置键唯一
        Long count = sysConfigMapper.selectCount(
                new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, config.getConfigKey())
        );
        if (count > 0) {
            throw new BusinessException("配置键已存在");
        }
        if (config.getConfigType() == null) {
            config.setConfigType("N");
        }
        sysConfigMapper.insert(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConfig(SysConfig config) {
        if (config.getId() == null) {
            throw new BusinessException("配置ID不能为空");
        }
        SysConfig existingConfig = sysConfigMapper.selectById(config.getId());
        if (existingConfig == null) {
            throw new BusinessException("配置不存在");
        }
        sysConfigMapper.updateById(config);
        // 清除缓存
        redisUtils.delete(CONFIG_CACHE_PREFIX + existingConfig.getConfigKey());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteConfig(Long id) {
        SysConfig config = sysConfigMapper.selectById(id);
        if (config == null) {
            throw new BusinessException("配置不存在");
        }
        sysConfigMapper.deleteById(id);
        // 清除缓存
        redisUtils.delete(CONFIG_CACHE_PREFIX + config.getConfigKey());
    }

    @Override
    public String getConfigByKey(String key) {
        // 先从缓存获取
        String cacheKey = CONFIG_CACHE_PREFIX + key;
        String cacheValue = redisUtils.get(cacheKey);
        if (StrUtil.isNotBlank(cacheValue)) {
            return cacheValue;
        }
        // 缓存未命中，查询数据库
        SysConfig config = sysConfigMapper.selectOne(
                new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, key)
        );
        if (config != null) {
            // 写入缓存
            redisUtils.set(cacheKey, config.getConfigValue(), CONFIG_CACHE_HOURS, TimeUnit.HOURS);
            return config.getConfigValue();
        }
        return null;
    }

    @Override
    public SysConfig getConfigById(Long id) {
        return sysConfigMapper.selectById(id);
    }

    @Override
    public void refreshCache() {
        // 清除所有配置缓存
        List<SysConfig> configs = sysConfigMapper.selectList(null);
        for (SysConfig config : configs) {
            redisUtils.delete(CONFIG_CACHE_PREFIX + config.getConfigKey());
        }
        log.info("已刷新所有配置缓存，共{}条", configs.size());
    }
}
