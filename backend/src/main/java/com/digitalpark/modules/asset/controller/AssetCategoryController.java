package com.digitalpark.modules.asset.controller;

import com.digitalpark.common.result.R;
import com.digitalpark.modules.asset.entity.AssetCategory;
import com.digitalpark.modules.asset.service.AssetCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资产分类Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/asset/category")
@RequiredArgsConstructor
public class AssetCategoryController {

    private final AssetCategoryService assetCategoryService;

    @PreAuthorize("@ss.hasPermi('asset:category:list')")
    @GetMapping("/tree")
    public R<List<AssetCategory>> tree() {
        return R.ok(assetCategoryService.selectTree());
    }

    @PreAuthorize("@ss.hasPermi('asset:category:list')")
    @GetMapping("/list")
    public R<List<AssetCategory>> list(AssetCategory query) {
        return R.ok(assetCategoryService.selectList(query));
    }

    @PreAuthorize("@ss.hasPermi('asset:category:query')")
    @GetMapping("/{id}")
    public R<AssetCategory> getInfo(@PathVariable Long id) {
        return R.ok(assetCategoryService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('asset:category:add')")
    @PostMapping
    public R<AssetCategory> add(@RequestBody AssetCategory category) {
        assetCategoryService.insert(category);
        return R.ok(category);
    }

    @PreAuthorize("@ss.hasPermi('asset:category:edit')")
    @PutMapping
    public R<AssetCategory> edit(@RequestBody AssetCategory category) {
        assetCategoryService.update(category);
        return R.ok(category);
    }

    @PreAuthorize("@ss.hasPermi('asset:category:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        assetCategoryService.deleteById(id);
        return R.ok();
    }
}
