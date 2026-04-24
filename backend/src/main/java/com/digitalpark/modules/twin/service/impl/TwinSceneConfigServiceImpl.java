package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinSceneConfig;
import com.digitalpark.modules.twin.mapper.TwinSceneConfigMapper;
import com.digitalpark.modules.twin.service.TwinSceneConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 场景配置Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class TwinSceneConfigServiceImpl implements TwinSceneConfigService {

    private final TwinSceneConfigMapper sceneConfigMapper;

    @Override
    public PageResult<TwinSceneConfig> selectPage(TwinSceneConfig query, int pageNum, int pageSize) {
        LambdaQueryWrapper<TwinSceneConfig> wrapper = buildQueryWrapper(query);
        wrapper.orderByAsc(TwinSceneConfig::getId);
        Page<TwinSceneConfig> page = sceneConfigMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public TwinSceneConfig selectById(Long id) {
        return sceneConfigMapper.selectById(id);
    }

    @Override
    public List<TwinSceneConfig> selectAll() {
        return sceneConfigMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public List<TwinSceneConfig> selectByType(String type) {
        LambdaQueryWrapper<TwinSceneConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TwinSceneConfig::getType, type);
        return sceneConfigMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(TwinSceneConfig config) {
        return sceneConfigMapper.insert(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TwinSceneConfig config) {
        return sceneConfigMapper.updateById(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return sceneConfigMapper.deleteById(id);
    }

    private LambdaQueryWrapper<TwinSceneConfig> buildQueryWrapper(TwinSceneConfig query) {
        LambdaQueryWrapper<TwinSceneConfig> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(TwinSceneConfig::getName, query.getName());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(TwinSceneConfig::getType, query.getType());
        }
        return wrapper;
    }
}
