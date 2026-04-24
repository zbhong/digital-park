package com.digitalpark.modules.asset.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.asset.dto.AssetInventoryDTO;
import com.digitalpark.modules.asset.entity.AssetInventory;
import com.digitalpark.modules.asset.service.AssetInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 资产盘点Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/asset/inventory")
@RequiredArgsConstructor
public class AssetInventoryController {

    private final AssetInventoryService assetInventoryService;

    @PreAuthorize("@ss.hasPermi('asset:inventory:list')")
    @GetMapping("/page")
    public R<PageResult<AssetInventory>> page(AssetInventory query,
                                               @RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(assetInventoryService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('asset:inventory:query')")
    @GetMapping("/{id}")
    public R<AssetInventory> getInfo(@PathVariable Long id) {
        return R.ok(assetInventoryService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('asset:inventory:add')")
    @PostMapping
    public R<Void> add(@RequestBody AssetInventoryDTO dto) {
        AssetInventory inventory = new AssetInventory();
        inventory.setTitle(dto.getTitle());
        inventory.setStartDate(dto.getStartDate());
        inventory.setEndDate(dto.getEndDate());
        inventory.setRemark(dto.getRemark());
        assetInventoryService.createInventory(inventory, dto.getAssetIds());
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('asset:inventory:edit')")
    @PutMapping
    public R<AssetInventory> edit(@RequestBody AssetInventory inventory) {
        assetInventoryService.update(inventory);
        return R.ok(inventory);
    }

    @PreAuthorize("@ss.hasPermi('asset:inventory:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        assetInventoryService.deleteById(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('asset:inventory:complete')")
    @PutMapping("/{id}/complete")
    public R<Void> complete(@PathVariable Long id) {
        assetInventoryService.completeInventory(id);
        return R.ok();
    }
}
