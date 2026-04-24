package com.digitalpark.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.utils.RedisUtils;
import com.digitalpark.modules.system.entity.SysDict;
import com.digitalpark.modules.system.entity.SysDictItem;
import com.digitalpark.modules.system.mapper.SysDictItemMapper;
import com.digitalpark.modules.system.mapper.SysDictMapper;
import com.digitalpark.modules.system.service.SysDictService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 系统字典 Service 实现
 *
 * @author digitalpark
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl implements SysDictService {

    private final SysDictMapper sysDictMapper;
    private final SysDictItemMapper sysDictItemMapper;
    private final RedisUtils redisUtils;
    private final ObjectMapper objectMapper;

    private static final String DICT_CACHE_PREFIX = "dict:";
    private static final long DICT_CACHE_HOURS = 2;

    @Override
    public PageResult<SysDict> getDictList(Page<?> page, String dictName, String dictType) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(dictName)) {
            wrapper.like(SysDict::getDictName, dictName);
        }
        if (StrUtil.isNotBlank(dictType)) {
            wrapper.like(SysDict::getDictType, dictType);
        }
        wrapper.orderByDesc(SysDict::getCreateTime);

        Page<SysDict> dictPage = sysDictMapper.selectPage((Page<SysDict>) page, wrapper);
        return PageResult.of(dictPage.getRecords(), dictPage.getTotal(), dictPage.getCurrent(), dictPage.getSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDict(SysDict dict) {
        // 校验字典类型唯一
        Long count = sysDictMapper.selectCount(
                new LambdaQueryWrapper<SysDict>().eq(SysDict::getDictType, dict.getDictType())
        );
        if (count > 0) {
            throw new BusinessException("字典类型已存在");
        }
        if (dict.getStatus() == null) {
            dict.setStatus(0);
        }
        sysDictMapper.insert(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDict(SysDict dict) {
        if (dict.getId() == null) {
            throw new BusinessException("字典ID不能为空");
        }
        SysDict existingDict = sysDictMapper.selectById(dict.getId());
        if (existingDict == null) {
            throw new BusinessException("字典不存在");
        }
        // 校验字典类型唯一（排除自身）
        Long count = sysDictMapper.selectCount(
                new LambdaQueryWrapper<SysDict>()
                        .eq(SysDict::getDictType, dict.getDictType())
                        .ne(SysDict::getId, dict.getId())
        );
        if (count > 0) {
            throw new BusinessException("字典类型已存在");
        }
        sysDictMapper.updateById(dict);

        // 清除缓存
        redisUtils.delete(DICT_CACHE_PREFIX + existingDict.getDictType());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDict(Long id) {
        SysDict dict = sysDictMapper.selectById(id);
        if (dict == null) {
            throw new BusinessException("字典不存在");
        }
        // 删除字典项
        sysDictItemMapper.delete(
                new LambdaQueryWrapper<SysDictItem>().eq(SysDictItem::getDictId, id)
        );
        // 删除字典
        sysDictMapper.deleteById(id);
        // 清除缓存
        redisUtils.delete(DICT_CACHE_PREFIX + dict.getDictType());
    }

    @Override
    public List<SysDictItem> getDictItems(String dictType) {
        return sysDictItemMapper.selectByDictType(dictType);
    }

    @Override
    public List<SysDictItem> getDictByType(String dictType) {
        // 先从缓存获取
        String cacheKey = DICT_CACHE_PREFIX + dictType;
        String cacheValue = redisUtils.get(cacheKey);
        if (StrUtil.isNotBlank(cacheValue)) {
            try {
                return objectMapper.readValue(cacheValue,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, SysDictItem.class));
            } catch (JsonProcessingException e) {
                log.error("解析字典缓存失败: {}", e.getMessage());
            }
        }
        // 缓存未命中，查询数据库
        List<SysDictItem> items = sysDictItemMapper.selectByDictType(dictType);
        // 写入缓存
        try {
            redisUtils.set(cacheKey, objectMapper.writeValueAsString(items), DICT_CACHE_HOURS, TimeUnit.HOURS);
        } catch (JsonProcessingException e) {
            log.error("序列化字典缓存失败: {}", e.getMessage());
        }
        return items;
    }

    @Override
    public void refreshCache() {
        // 清除所有字典缓存
        List<SysDict> dicts = sysDictMapper.selectList(null);
        for (SysDict dict : dicts) {
            redisUtils.delete(DICT_CACHE_PREFIX + dict.getDictType());
        }
        log.info("已刷新所有字典缓存，共{}条", dicts.size());
    }
}
