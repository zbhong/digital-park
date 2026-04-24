package com.digitalpark.modules.twin.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinSceneConfig;

import java.util.List;

/**
 * 场景配置Service接口
 *
 * @author digitalpark
 */
public interface TwinSceneConfigService {

    PageResult<TwinSceneConfig> selectPage(TwinSceneConfig query, int pageNum, int pageSize);

    TwinSceneConfig selectById(Long id);

    List<TwinSceneConfig> selectAll();

    List<TwinSceneConfig> selectByType(String type);

    int insert(TwinSceneConfig config);

    int update(TwinSceneConfig config);

    int deleteById(Long id);
}
