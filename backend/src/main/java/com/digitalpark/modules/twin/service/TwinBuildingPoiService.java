package com.digitalpark.modules.twin.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.TwinBuildingPoi;

import java.util.List;

/**
 * 楼栋POIService接口
 *
 * @author digitalpark
 */
public interface TwinBuildingPoiService {

    PageResult<TwinBuildingPoi> selectPage(TwinBuildingPoi query, int pageNum, int pageSize);

    TwinBuildingPoi selectById(Long id);

    List<TwinBuildingPoi> selectByBuildingId(Long buildingId);

    List<TwinBuildingPoi> selectAll();

    int insert(TwinBuildingPoi poi);

    int update(TwinBuildingPoi poi);

    int deleteById(Long id);
}
