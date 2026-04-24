package com.digitalpark.modules.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.annotation.Log;
import com.digitalpark.common.base.BaseController;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.common.result.R;
import com.digitalpark.modules.system.dto.RoleDTO;
import com.digitalpark.modules.system.entity.SysRole;
import com.digitalpark.modules.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统角色控制器
 * /system/role 路径，角色CRUD、分配菜单
 *
 * @author digitalpark
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class SysRoleController extends BaseController {

    private final SysRoleService sysRoleService;

    /**
     * 获取角色列表（分页）
     */
    @Operation(summary = "角色列表")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public R<PageResult<SysRole>> list(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Page<SysRole> page = new Page<>(current, size);
        PageResult<SysRole> result = sysRoleService.getRoleList(page, keyword);
        return success(result);
    }

    /**
     * 获取所有角色（下拉选择）
     */
    @Operation(summary = "所有角色")
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/all")
    public R<List<SysRole>> all() {
        List<SysRole> roles = sysRoleService.getAllRoles();
        return success(roles);
    }

    /**
     * 获取角色详情
     */
    @Operation(summary = "角色详情")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/{id}")
    public R<SysRole> getInfo(@PathVariable Long id) {
        SysRole role = sysRoleService.getRoleById(id);
        return success(role);
    }

    /**
     * 新增角色
     */
    @Operation(summary = "新增角色")
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(module = "角色管理", type = Log.OperationType.INSERT, description = "新增角色")
    @PostMapping
    public R<Void> add(@Valid @RequestBody RoleDTO roleDTO) {
        sysRoleService.createRole(roleDTO);
        return success();
    }

    /**
     * 修改角色
     */
    @Operation(summary = "修改角色")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(module = "角色管理", type = Log.OperationType.UPDATE, description = "修改角色")
    @PutMapping
    public R<Void> edit(@Valid @RequestBody RoleDTO roleDTO) {
        sysRoleService.updateRole(roleDTO);
        return success();
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色")
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(module = "角色管理", type = Log.OperationType.DELETE, description = "删除角色")
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) {
        sysRoleService.deleteRole(id);
        return success();
    }

    /**
     * 分配菜单
     */
    @Operation(summary = "分配菜单")
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(module = "角色管理", type = Log.OperationType.UPDATE, description = "分配菜单")
    @PutMapping("/assignMenu")
    public R<Void> assignMenu(@RequestParam Long roleId, @RequestBody List<Long> menuIds) {
        sysRoleService.assignMenu(roleId, menuIds);
        return success();
    }

    /**
     * 获取角色菜单ID列表
     */
    @Operation(summary = "获取角色菜单")
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/menus/{roleId}")
    public R<List<Long>> getRoleMenus(@PathVariable Long roleId) {
        List<Long> menuIds = sysRoleService.getRoleMenus(roleId);
        return success(menuIds);
    }
}
