package com.digitalpark.modules.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.modules.asset.entity.AssetCategory;
import com.digitalpark.modules.asset.mapper.AssetCategoryMapper;
import com.digitalpark.modules.asset.service.AssetCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资产分类Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class AssetCategoryServiceImpl implements AssetCategoryService {

    private final AssetCategoryMapper assetCategoryMapper;

    @Override
    public List<AssetCategory> selectTree() {
        List<AssetCategory> allCategories = assetCategoryMapper.selectList(
                new LambdaQueryWrapper<AssetCategory>().orderByAsc(AssetCategory::getSort));
        return buildTree(allCategories, 0L);
    }

    @Override
    public List<AssetCategory> selectList(AssetCategory query) {
        LambdaQueryWrapper<AssetCategory> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(AssetCategory::getName, query.getName());
        }
        if (StringUtils.hasText(query.getCode())) {
            wrapper.eq(AssetCategory::getCode, query.getCode());
        }
        wrapper.orderByAsc(AssetCategory::getSort);
        return assetCategoryMapper.selectList(wrapper);
    }

    @Override
    public AssetCategory selectById(Long id) {
        return assetCategoryMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(AssetCategory category) {
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        if (category.getSort() == null) {
            category.setSort(0);
        }
        return assetCategoryMapper.insert(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(AssetCategory category) {
        return assetCategoryMapper.updateById(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        // 检查是否有子分类
        Long childCount = assetCategoryMapper.selectCount(
                new LambdaQueryWrapper<AssetCategory>().eq(AssetCategory::getParentId, id));
        if (childCount > 0) {
            throw new BusinessException("存在子分类，无法删除");
        }
        return assetCategoryMapper.deleteById(id);
    }

    /**
     * 构建树形结构
     */
    private List<AssetCategory> buildTree(List<AssetCategory> allCategories, Long parentId) {
        Map<Long, List<AssetCategory>> childrenMap = allCategories.stream()
                .collect(Collectors.groupingBy(AssetCategory::getParentId));

        List<AssetCategory> tree = new ArrayList<>();
        for (AssetCategory category : allCategories) {
            if (parentId.equals(category.getParentId())) {
                category.setRemark(null); // 清空remark避免树形数据冗余
                tree.add(category);
            }
        }
        return tree;
    }
}
