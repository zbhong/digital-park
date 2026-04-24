package com.digitalpark.modules.asset.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.asset.entity.AssetCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 资产分类Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface AssetCategoryMapper extends BaseMapper<AssetCategory> {
}
