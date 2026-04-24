package com.digitalpark.modules.twin.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.ParkBuilding;

import java.util.List;

/**
 * 园区建筑Service接口
 *
 * @author digitalpark
 */
public interface ParkBuildingService {

    PageResult<ParkBuilding> selectPage(ParkBuilding query, int pageNum, int pageSize);

    ParkBuilding selectById(Long id);

    List<ParkBuilding> selectByAreaId(Long areaId);

    List<ParkBuilding> selectAll();

    int insert(ParkBuilding building);

    int update(ParkBuilding building);

    int deleteById(Long id);
}
