package com.digitalpark.modules.asset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.asset.entity.AssetInventoryItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 盘点明细Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface AssetInventoryItemMapper extends BaseMapper<AssetInventoryItem> {
}
