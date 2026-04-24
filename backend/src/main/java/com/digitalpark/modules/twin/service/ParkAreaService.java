package com.digitalpark.modules.twin.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.twin.entity.ParkArea;

import java.util.List;

/**
 * 园区区域Service接口
 *
 * @author digitalpark
 */
public interface ParkAreaService {

    PageResult<ParkArea> selectPage(ParkArea query, int pageNum, int pageSize);

    ParkArea selectById(Long id);

    List<ParkArea> selectTree();

    List<ParkArea> selectByParentId(Long parentId);

    List<ParkArea> selectAll();

    int insert(ParkArea area);

    int update(ParkArea area);

    int deleteById(Long id);
}
