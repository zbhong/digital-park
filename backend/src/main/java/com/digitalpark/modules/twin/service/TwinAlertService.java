package com.digitalpark.modules.twin.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinAlert;

import java.util.List;
import java.util.Map;

/**
 * 告警记录Service接口
 *
 * @author digitalpark
 */
public interface TwinAlertService {

    PageResult<TwinAlert> selectPage(TwinAlert query, int pageNum, int pageSize);

    TwinAlert selectById(Long id);

    List<TwinAlert> selectByType(String type);

    List<TwinAlert> selectByStatus(String status);

    Map<String, Object> selectStatistics();

    int insert(TwinAlert alert);

    int update(TwinAlert alert);

    int handleAlert(Long id, Long handlerId, String handleResult);

    int deleteById(Long id);
}
