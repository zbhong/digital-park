package com.digitalpark.modules.asset.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.asset.entity.AssetDocument;
import com.digitalpark.modules.asset.mapper.AssetDocumentMapper;
import com.digitalpark.modules.asset.service.AssetDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 资产文档Service实现
 *
 * @author digitalpark
 */
@Service
@RequiredArgsConstructor
public class AssetDocumentServiceImpl implements AssetDocumentService {

    private final AssetDocumentMapper assetDocumentMapper;

    @Override
    public PageResult<AssetDocument> selectPage(AssetDocument query, int pageNum, int pageSize) {
        LambdaQueryWrapper<AssetDocument> wrapper = buildQueryWrapper(query);
        wrapper.orderByDesc(AssetDocument::getCreateTime);
        Page<AssetDocument> page = assetDocumentMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<AssetDocument> selectListByAssetId(Long assetId) {
        return assetDocumentMapper.selectList(
                new LambdaQueryWrapper<AssetDocument>()
                        .eq(AssetDocument::getAssetId, assetId)
                        .orderByDesc(AssetDocument::getCreateTime));
    }

    @Override
    public AssetDocument selectById(Long id) {
        return assetDocumentMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(AssetDocument document) {
        return assetDocumentMapper.insert(document);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(AssetDocument document) {
        return assetDocumentMapper.updateById(document);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(Long id) {
        return assetDocumentMapper.deleteById(id);
    }

    private LambdaQueryWrapper<AssetDocument> buildQueryWrapper(AssetDocument query) {
        LambdaQueryWrapper<AssetDocument> wrapper = new LambdaQueryWrapper<>();
        if (query.getAssetId() != null) {
            wrapper.eq(AssetDocument::getAssetId, query.getAssetId());
        }
        if (StringUtils.hasText(query.getName())) {
            wrapper.like(AssetDocument::getName, query.getName());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(AssetDocument::getType, query.getType());
        }
        return wrapper;
    }
}
