package com.digitalpark.modules.twin.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinLayerConfig;

import java.util.List;

/**
 * 图层配置Service接口
 *
 * @author digitalpark
 */
public interface TwinLayerConfigService {

    PageResult<TwinLayerConfig> selectPage(TwinLayerConfig query, int pageNum, int pageSize);

    TwinLayerConfig selectById(Long id);

    List<TwinLayerConfig> selectAll();

    List<TwinLayerConfig> selectByType(String type);

    int insert(TwinLayerConfig config);

    int update(TwinLayerConfig config);

    int deleteById(Long id);
}
