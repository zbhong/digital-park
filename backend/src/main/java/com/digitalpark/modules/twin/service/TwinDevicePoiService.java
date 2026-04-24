package com.digitalpark.modules.twin.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinDevicePoi;

import java.util.List;

/**
 * 设备POIService接口
 *
 * @author digitalpark
 */
public interface TwinDevicePoiService {

    PageResult<TwinDevicePoi> selectPage(TwinDevicePoi query, int pageNum, int pageSize);

    TwinDevicePoi selectById(Long id);

    List<TwinDevicePoi> selectByDeviceType(String deviceType);

    List<TwinDevicePoi> selectAll();

    int insert(TwinDevicePoi poi);

    int update(TwinDevicePoi poi);

    int batchSave(List<TwinDevicePoi> list);

    int deleteById(Long id);
}
