package com.digitalpark.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.exception.BusinessException;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.system.dto.RoleDTO;
import com.digitalpark.modules.system.entity.SysRole;
import com.digitalpark.modules.system.mapper.SysRoleMapper;
import com.digitalpark.modules.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 系统角色 Service 实现
 *
 * @author digitalpark
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleMapper sysRoleMapper;

    @Override
    public PageResult<SysRole> getRoleList(Page<?> page, String keyword) {
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w.like(SysRole::getRoleName, keyword)
                    .or().like(SysRole::getRoleCode, keyword));
        }
        wrapper.orderByAsc(SysRole::getSortOrder);

        Page<SysRole> rolePage = sysRoleMapper.selectPage((Page<SysRole>) page, wrapper);
        return PageResult.of(rolePage.getRecords(), rolePage.getTotal(), rolePage.getCurrent(), rolePage.getSize());
    }

    @Override
    public List<SysRole> getAllRoles() {
        return sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getStatus, 0).orderByAsc(SysRole::getSortOrder)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(RoleDTO roleDTO) {
        // 校验角色编码唯一
        Long count = sysRoleMapper.selectCount(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, roleDTO.getRoleCode())
        );
        if (count > 0) {
            throw new BusinessException("角色编码已存在");
        }
        SysRole role = new SysRole();
        role.setRoleName(roleDTO.getRoleName());
        role.setRoleCode(roleDTO.getRoleCode());
        role.setSortOrder(roleDTO.getSortOrder() != null ? roleDTO.getSortOrder() : 0);
        role.setStatus(roleDTO.getStatus() != null ? roleDTO.getStatus() : 0);
        role.setRemark(roleDTO.getRemark());
        sysRoleMapper.insert(role);

        // 分配菜单
        if (roleDTO.getMenuIds() != null && !roleDTO.getMenuIds().isEmpty()) {
            assignMenuDirect(role.getId(), roleDTO.getMenuIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleDTO roleDTO) {
        if (roleDTO.getId() == null) {
            throw new BusinessException("角色ID不能为空");
        }
        SysRole role = sysRoleMapper.selectById(roleDTO.getId());
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        // 校验角色编码唯一（排除自身）
        Long count = sysRoleMapper.selectCount(
                new LambdaQueryWrapper<SysRole>()
                        .eq(SysRole::getRoleCode, roleDTO.getRoleCode())
                        .ne(SysRole::getId, roleDTO.getId())
        );
        if (count > 0) {
            throw new BusinessException("角色编码已存在");
        }
        role.setRoleName(roleDTO.getRoleName());
        role.setRoleCode(roleDTO.getRoleCode());
        role.setSortOrder(roleDTO.getSortOrder());
        role.setStatus(roleDTO.getStatus());
        role.setRemark(roleDTO.getRemark());
        sysRoleMapper.updateById(role);

        // 更新菜单
        if (roleDTO.getMenuIds() != null) {
            assignMenuDirect(role.getId(), roleDTO.getMenuIds());
        }
    }

    @Override
    public void deleteRole(Long id) {
        SysRole role = sysRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        // 删除角色菜单关联
        sysRoleMapper.deleteRoleMenu(id);
        sysRoleMapper.deleteById(id);
    }

    @Override
    public SysRole getRoleById(Long id) {
        SysRole role = sysRoleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return role;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenu(Long roleId, List<Long> menuIds) {
        SysRole role = sysRoleMapper.selectById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        assignMenuDirect(roleId, menuIds);
    }

    @Override
    public List<Long> getRoleMenus(Long roleId) {
        return sysRoleMapper.selectMenuIdsByRoleId(roleId);
    }

    // ==================== 私有方法 ====================

    /**
     * 直接分配菜单（内部方法）
     */
    private void assignMenuDirect(Long roleId, List<Long> menuIds) {
        // 先删除旧的菜单关联
        sysRoleMapper.deleteRoleMenu(roleId);
        // 再插入新的菜单关联
        if (menuIds != null && !menuIds.isEmpty()) {
            sysRoleMapper.insertRoleMenu(roleId, menuIds);
        }
    }
}
