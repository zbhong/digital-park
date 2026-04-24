package com.digitalpark.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.system.dto.RoleDTO;
import com.digitalpark.modules.system.entity.SysRole;

import java.util.List;

/**
 * 系统角色 Service 接口
 *
 * @author digitalpark
 */
public interface SysRoleService {

    /**
     * 获取角色列表（分页）
     *
     * @param page    分页参数
     * @param keyword 搜索关键词
     * @return 分页结果
     */
    PageResult<SysRole> getRoleList(Page<?> page, String keyword);

    /**
     * 获取所有角色（下拉选择用）
     *
     * @return 角色列表
     */
    List<SysRole> getAllRoles();

    /**
     * 创建角色
     *
     * @param roleDTO 角色信息
     */
    void createRole(RoleDTO roleDTO);

    /**
     * 更新角色
     *
     * @param roleDTO 角色信息
     */
    void updateRole(RoleDTO roleDTO);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    void deleteRole(Long id);

    /**
     * 根据角色ID获取角色详情
     *
     * @param id 角色ID
     * @return 角色信息
     */
    SysRole getRoleById(Long id);

    /**
     * 分配菜单
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     */
    void assignMenu(Long roleId, List<Long> menuIds);

    /**
     * 获取角色菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> getRoleMenus(Long roleId);
}
