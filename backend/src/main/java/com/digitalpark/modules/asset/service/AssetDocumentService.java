package com.digitalpark.modules.asset.service;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.asset.entity.AssetDocument;

import java.util.List;

/**
 * 资产文档Service接口
 *
 * @author digitalpark
 */
public interface AssetDocumentService {

    PageResult<AssetDocument> selectPage(AssetDocument query, int pageNum, int pageSize);

    List<AssetDocument> selectListByAssetId(Long assetId);

    AssetDocument selectById(Long id);

    int insert(AssetDocument document);

    int update(AssetDocument document);

    int deleteById(Long id);
}
