package com.digitalpark.modules.asset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.asset.entity.AssetInventory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资产盘点Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface AssetInventoryMapper extends BaseMapper<AssetInventory> {
}
