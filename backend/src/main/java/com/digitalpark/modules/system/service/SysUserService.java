package com.digitalpark.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalpark.common.result.PageResult;
import com.digitalpark.modules.system.dto.ChangePasswordDTO;
import com.digitalpark.modules.system.dto.UserDTO;
import com.digitalpark.modules.system.entity.SysRole;
import com.digitalpark.modules.system.vo.RouteVO;
import com.digitalpark.modules.system.vo.UserVO;

import java.util.List;

/**
 * 系统用户 Service 接口
 *
 * @author digitalpark
 */
public interface SysUserService {

    /**
     * 获取用户列表（分页）
     *
     * @param page    分页参数
     * @param keyword 搜索关键词
     * @return 分页结果
     */
    PageResult<UserVO> getUserList(Page<?> page, String keyword);

    /**
     * 创建用户
     *
     * @param userDTO 用户信息
     */
    void createUser(UserDTO userDTO);

    /**
     * 更新用户
     *
     * @param userDTO 用户信息
     */
    void updateUser(UserDTO userDTO);

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    void deleteUser(Long id);

    /**
     * 根据用户ID获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    UserVO getUserById(Long id);

    /**
     * 分配角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     */
    void assignRole(Long userId, List<Long> roleIds);

    /**
     * 重置密码
     *
     * @param userId      用户ID
     * @param newPassword 新密码
     */
    void resetPassword(Long userId, String newPassword);

    /**
     * 修改密码
     *
     * @param changePasswordDTO 修改密码请求
     */
    void changePassword(ChangePasswordDTO changePasswordDTO);

    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 状态
     */
    void changeStatus(Long userId, Integer status);

    /**
     * 获取用户角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> getUserRoles(Long userId);

    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限标识列表
     */
    List<String> getUserPermissions(Long userId);

    /**
     * 获取用户路由（构建前端路由树）
     *
     * @param userId 用户ID
     * @return 路由列表
     */
    List<RouteVO> getUserRouters(Long userId);
}
