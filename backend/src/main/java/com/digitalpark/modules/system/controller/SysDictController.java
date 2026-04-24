package com.digitalpark.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.annotation.Log;
import com.digitalpark.common.base.BaseController;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.system.entity.SysDict;
import com.digitalpark.modules.system.entity.SysDictItem;
import com.digitalpark.modules.system.mapper.SysDictItemMapper;
import com.digitalpark.modules.system.mapper.SysDictMapper;
import com.digitalpark.modules.system.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统字典控制器
 * /system/dict 路径
 *
 * @author digitalpark
 */
@Tag(name = "字典管理")
@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
public class SysDictController extends BaseController {

    private final SysDictService sysDictService;
    private final SysDictItemMapper sysDictItemMapper;
    private final SysDictMapper sysDictMapper;

    /**
     * 获取字典列表（分页）
     */
    @Operation(summary = "字典列表")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public R<PageResult<SysDict>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String dictName,
            @RequestParam(required = false) String dictType) {
        Page<SysDict> page = new Page<>(current, size);
        PageResult<SysDict> result = sysDictService.getDictList(page, dictName, dictType);
        return success(result);
    }

    /**
     * 获取字典项
     */
    @Operation(summary = "字典项")
    @GetMapping("/items/{dictType}")
    public R<List<SysDictItem>> items(@PathVariable String dictType) {
        List<SysDictItem> items = sysDictService.getDictItems(dictType);
        return success(items);
    }

    /**
     * 新增字典
     */
    @Operation(summary = "新增字典")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(module = "字典管理", type = Log.OperationType.INSERT, description = "新增字典")
    @PostMapping
    public R<Void> add(@Valid @RequestBody SysDict dict) {
        sysDictService.createDict(dict);
        return success();
    }

    /**
     * 修改字典
     */
    @Operation(summary = "修改字典")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(module = "字典管理", type = Log.OperationType.UPDATE, description = "修改字典")
    @PutMapping
    public R<Void> edit(@Valid @RequestBody SysDict dict) {
        sysDictService.updateDict(dict);
        return success();
    }

    /**
     * 删除字典
     */
    @Operation(summary = "删除字典")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(module = "字典管理", type = Log.OperationType.DELETE, description = "删除字典")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        sysDictService.deleteDict(id);
        return success();
    }

    // ==================== 字典数据管理 ====================

    /**
     * 获取字典数据列表（分页）
     */
    @Operation(summary = "字典数据列表")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/data/list")
    public R<PageResult<SysDictItem>> dataList(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String dictType,
            @RequestParam(required = false) String label) {
        Page<SysDictItem> page = new Page<>(current, size);
        LambdaQueryWrapper<SysDictItem> wrapper = new LambdaQueryWrapper<>();
        if (dictType != null && !dictType.isEmpty()) {
            wrapper.eq(SysDictItem::getDictType, dictType);
        }
        if (label != null && !label.isEmpty()) {
            wrapper.like(SysDictItem::getLabel, label);
        }
        wrapper.orderByAsc(SysDictItem::getSortOrder);
        Page<SysDictItem> result = sysDictItemMapper.selectPage(page, wrapper);
        return success(PageResult.of(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize()));
    }

    /**
     * 获取字典数据详情
     */
    @Operation(summary = "字典数据详情")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/data/{dictCode}")
    public R<SysDictItem> getData(@PathVariable Long dictCode) {
        SysDictItem item = sysDictItemMapper.selectById(dictCode);
        return success(item);
    }

    /**
     * 根据字典类型获取字典数据
     */
    @Operation(summary = "根据字典类型获取字典数据")
    @GetMapping("/data/type/{dictType}")
    public R<List<SysDictItem>> getDataByType(@PathVariable String dictType) {
        List<SysDictItem> items = sysDictItemMapper.selectByDictType(dictType);
        return success(items);
    }

    /**
     * 新增字典数据
     */
    @Operation(summary = "新增字典数据")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(module = "字典数据管理", type = Log.OperationType.INSERT, description = "新增字典数据")
    @PostMapping("/data")
    public R<Void> addData(@RequestBody SysDictItem dictItem) {
        if (dictItem.getDictType() == null || dictItem.getDictType().isEmpty()) {
            throw new BusinessException("字典类型不能为空");
        }
        if (dictItem.getLabel() == null || dictItem.getLabel().isEmpty()) {
            throw new BusinessException("字典标签不能为空");
        }
        if (dictItem.getValue() == null || dictItem.getValue().isEmpty()) {
            throw new BusinessException("字典值不能为空");
        }
        // 查找dict_id
        SysDict dict = sysDictMapper.selectOne(
                new LambdaQueryWrapper<SysDict>().eq(SysDict::getDictType, dictItem.getDictType()));
        if (dict == null) {
            throw new BusinessException("字典类型不存在");
        }
        dictItem.setDictId(dict.getId());
        if (dictItem.getStatus() == null) {
            dictItem.setStatus(0);
        }
        sysDictItemMapper.insert(dictItem);
        return success();
    }

    /**
     * 修改字典数据
     */
    @Operation(summary = "修改字典数据")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(module = "字典数据管理", type = Log.OperationType.UPDATE, description = "修改字典数据")
    @PutMapping("/data")
    public R<Void> updateData(@RequestBody SysDictItem dictItem) {
        if (dictItem.getId() == null) {
            throw new BusinessException("字典数据ID不能为空");
        }
        sysDictItemMapper.updateById(dictItem);
        return success();
    }

    /**
     * 删除字典数据
     */
    @Operation(summary = "删除字典数据")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(module = "字典数据管理", type = Log.OperationType.DELETE, description = "删除字典数据")
    @DeleteMapping("/data/{dictCodes}")
    public R<Void> removeData(@PathVariable String dictCodes) {
        List<Long> ids = Arrays.stream(dictCodes.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        sysDictItemMapper.deleteBatchIds(ids);
        return success();
    }

    /**
     * 获取单个字典类型详情
     */
    @Operation(summary = "字典类型详情")
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping("/type/{dictId}")
    public R<SysDict> getDictType(@PathVariable Long dictId) {
        SysDict dict = sysDictMapper.selectById(dictId);
        if (dict != null) {
            List<SysDictItem> items = sysDictItemMapper.selectByDictType(dict.getDictType());
            dict.setItems(items);
        }
        return success(dict);
    }

    /**
     * 刷新字典缓存
     */
    @Operation(summary = "刷新字典缓存")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(module = "字典管理", type = Log.OperationType.UPDATE, description = "刷新字典缓存")
    @PostMapping("/type/refreshCache")
    public R<Void> refreshCache() {
        sysDictService.refreshCache();
        return success();
    }
}
