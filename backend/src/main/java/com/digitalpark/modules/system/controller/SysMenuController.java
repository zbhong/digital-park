package com.digitalpark.modules.system.controller;

import com.digitalpark.common.annotation.Log;
import com.digitalpark.common.base.BaseController;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.system.dto.MenuDTO;
import com.digitalpark.modules.system.entity.SysMenu;
import com.digitalpark.modules.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统菜单控制器
 * /system/menu 路径，菜单CRUD、路由树
 *
 * @author digitalpark
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController extends BaseController {

    private final SysMenuService sysMenuService;

    /**
     * 获取菜单树
     */
    @Operation(summary = "菜单树")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/tree")
    public R<List<SysMenu>> tree() {
        List<SysMenu> menuTree = sysMenuService.getMenuTree();
        return success(menuTree);
    }

    /**
     * 获取菜单列表（平铺）
     */
    @Operation(summary = "菜单列表")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public R<List<SysMenu>> list() {
        List<SysMenu> menuList = sysMenuService.getMenuList();
        return success(menuList);
    }

    /**
     * 获取单个菜单详情
     */
    @Operation(summary = "菜单详情")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping("/{id}")
    public R<SysMenu> getInfo(@PathVariable Long id) {
        SysMenu menu = sysMenuService.getMenuById(id);
        return success(menu);
    }

    /**
     * 新增菜单
     */
    @Operation(summary = "新增菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(module = "菜单管理", type = Log.OperationType.INSERT, description = "新增菜单")
    @PostMapping
    public R<Void> add(@Valid @RequestBody MenuDTO menuDTO) {
        sysMenuService.createMenu(menuDTO);
        return success();
    }

    /**
     * 修改菜单
     */
    @Operation(summary = "修改菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(module = "菜单管理", type = Log.OperationType.UPDATE, description = "修改菜单")
    @PutMapping
    public R<Void> edit(@Valid @RequestBody MenuDTO menuDTO) {
        sysMenuService.updateMenu(menuDTO);
        return success();
    }

    /**
     * 删除菜单
     */
    @Operation(summary = "删除菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(module = "菜单管理", type = Log.OperationType.DELETE, description = "删除菜单")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        sysMenuService.deleteMenu(id);
        return success();
    }
}
