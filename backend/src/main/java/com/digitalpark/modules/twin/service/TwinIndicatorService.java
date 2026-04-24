package com.digitalpark.modules.twin.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinIndicator;

import java.util.List;

/**
 * 核心指标Service接口
 *
 * @author digitalpark
 */
public interface TwinIndicatorService {

    PageResult<TwinIndicator> selectPage(TwinIndicator query, int pageNum, int pageSize);

    TwinIndicator selectById(Long id);

    List<TwinIndicator> selectAll();

    List<TwinIndicator> selectByCategory(String category);

    List<TwinIndicator> selectRealtimeData();

    int insert(TwinIndicator indicator);

    int update(TwinIndicator indicator);

    int deleteById(Long id);
}
