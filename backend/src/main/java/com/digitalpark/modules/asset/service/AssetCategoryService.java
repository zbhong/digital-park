package com.digitalpark.modules.asset.service;

import com.digitalpark.modules.asset.entity.AssetCategory;

import java.util.List;

/**
 * 资产分类Service接口
 *
 * @author digitalpark
 */
public interface AssetCategoryService {

    /**
     * 获取分类树
     */
    List<AssetCategory> selectTree();

    /**
     * 获取分类列表
     */
    List<AssetCategory> selectList(AssetCategory query);

    /**
     * 根据ID查询
     */
    AssetCategory selectById(Long id);

    /**
     * 新增分类
     */
    int insert(AssetCategory category);

    /**
     * 修改分类
     */
    int update(AssetCategory category);

    /**
     * 删除分类
     */
    int deleteById(Long id);
}
