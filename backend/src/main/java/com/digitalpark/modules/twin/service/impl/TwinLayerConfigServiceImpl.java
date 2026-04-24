package com.digitalpark.modules.twin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinLayerConfig;
import com.digitalpark.modules.twin.mapper.TwinLayerConfigMapper;
import com.digitalpark.modules.twin.service.TwinLayerConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 图层配置Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class TwinLayerConfigServiceImpl implements TwinLayerConfigService {

    private final TwinLayerConfigMapper layerConfigMapper;

    @Override
    public PageResult<TwinLayerConfig> selectPage(TwinLayerConfig query, int pageNum, int pageSize) {
        LambdaQueryWrapper<TwinLayerConfig> wrapper = buildQueryWrapper(query);
        wrapper.orderByAsc(TwinLayerConfig::getSort);
        Page<TwinLayerConfig> page = layerConfigMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public TwinLayerConfig selectById(Long id) {
        return layerConfigMapper.selectById(id);
    }

    @Override
    public List<TwinLayerConfig> selectAll() {
        LambdaQueryWrapper<TwinLayerConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(TwinLayerConfig::getSort);
        return layerConfigMapper.selectList(wrapper);
    }

    @Override
    public List<TwinLayerConfig> selectByType(String type) {
        LambdaQueryWrapper<TwinLayerConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TwinLayerConfig::getType, type);
        wrapper.orderByAsc(TwinLayerConfig::getSort);
        return layerConfigMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(TwinLayerConfig config) {
        if (config.getVisible() == null) {
            config.setVisible(1);
        }
        if (config.getSort() == null) {
            config.setSort(0);
        }
        return layerConfigMapper.insert(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TwinLayerConfig config) {
        return layerConfigMapper.updateById(config);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return layerConfigMapper.deleteById(id);
    }

    private LambdaQueryWrapper<TwinLayerConfig> buildQueryWrapper(TwinLayerConfig query) {
        LambdaQueryWrapper<TwinLayerConfig> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(TwinLayerConfig::getName, query.getName());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(TwinLayerConfig::getType, query.getType());
        }
        if (query.getVisible() != null) {
            wrapper.eq(TwinLayerConfig::getVisible, query.getVisible());
        }
        return wrapper;
    }
}
