package com.digitalpark.modules.asset.controller;

import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.asset.entity.AssetInfo;
import com.digitalpark.modules.asset.service.AssetInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 资产信息Controller
 *
 * @author digitalpark
 */
@RestController
@RequestMapping("/asset/info")
@RequiredArgsConstructor
public class AssetInfoController {

    private final AssetInfoService assetInfoService;

    @PreAuthorize("@ss.hasPermi('asset:info:list')")
    @GetMapping("/page")
    public R<PageResult<AssetInfo>> page(AssetInfo query,
                                          @RequestParam(defaultValue = "1") int pageNum,
                                          @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(assetInfoService.selectPage(query, pageNum, pageSize));
    }

    @PreAuthorize("@ss.hasPermi('asset:info:query')")
    @GetMapping("/{id}")
    public R<AssetInfo> getInfo(@PathVariable Long id) {
        return R.ok(assetInfoService.selectById(id));
    }

    @PreAuthorize("@ss.hasPermi('asset:info:add')")
    @PostMapping
    public R<AssetInfo> add(@RequestBody AssetInfo info) {
        assetInfoService.insert(info);
        return R.ok(info);
    }

    @PreAuthorize("@ss.hasPermi('asset:info:edit')")
    @PutMapping
    public R<AssetInfo> edit(@RequestBody AssetInfo info) {
        assetInfoService.update(info);
        return R.ok(info);
    }

    @PreAuthorize("@ss.hasPermi('asset:info:remove')")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        assetInfoService.deleteById(id);
        return R.ok();
    }

    @PreAuthorize("@ss.hasPermi('asset:info:statistics')")
    @GetMapping("/statistics/type")
    public R<Map<String, Object>> statisticsByType() {
        return R.ok(assetInfoService.selectStatisticsByType());
    }

    @PreAuthorize("@ss.hasPermi('asset:info:statistics')")
    @GetMapping("/statistics/status")
    public R<Map<String, Object>> statisticsByStatus() {
        return R.ok(assetInfoService.selectStatisticsByStatus());
    }
}
