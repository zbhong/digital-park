package com.digitalpark.modules.asset.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.asset.entity.AssetDocument;
import com.digitalpark.modules.asset.service.AssetDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产文档Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/asset/document")
@RequiredArgsConstructor
public class AssetDocumentController {

    private final AssetDocumentService assetDocumentService;

    @PreAuthorize("@ss.hasPermi('asset:document:list')")
    @GetMapping("/page")
    public R<PageResult<AssetDocument>> page(AssetDocument query,
                                              @RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(assetDocumentService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('asset:document:list')")
    @GetMapping("/asset/{assetId}")
    public R<List<AssetDocument>> listByAssetId(@PathVariable Long assetId) {
        return R.ok(assetDocumentService.selectListByAssetId(assetId));
    }

    @PreAuthorize("@ss.hasPermi('asset:document:query')")
    @GetMapping("/{id}")
    public R<AssetDocument> getInfo(@PathVariable Long id) {
        return R.ok(assetDocumentService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('asset:document:add')")
    @PostMapping
    public R<AssetDocument> add(@RequestBody AssetDocument document) {
        assetDocumentService.insert(document);
        return R.ok(document);
    }

    @PreAuthorize("@ss.hasPermi('asset:document:edit')")
    @PutMapping
    public R<AssetDocument> edit(@RequestBody AssetDocument document) {
        assetDocumentService.update(document);
        return R.ok(document);
    }

    @PreAuthorize("@ss.hasPermi('asset:document:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        assetDocumentService.deleteById(id);
        return R.ok();
    }
}
