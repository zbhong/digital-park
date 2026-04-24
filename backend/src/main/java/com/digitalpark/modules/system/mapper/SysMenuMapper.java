package com.digitalpark.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.digitalpark.modules.system.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统菜单 Mapper
 *
 * @author digitalpark
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户ID查询菜单列表
     */
    @Select("SELECT DISTINCT m.* FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND m.status = 0 AND m.deleted = 0 " +
            "AND m.menu_type IN ('1', '2', 'M', 'C') " +
            "ORDER BY m.sort_order ASC")
    List<SysMenu> selectMenusByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID列表查询菜单列表
     */
    @Select("<script>" +
            "SELECT DISTINCT m.* FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "WHERE rm.role_id IN " +
            "<foreach collection='roleIds' item='roleId' open='(' separator=',' close=')'>" +
            "#{roleId}" +
            "</foreach>" +
            " AND m.status = 0 AND m.deleted = 0 " +
            " AND m.menu_type IN ('1', '2', 'M', 'C') " +
            "ORDER BY m.sort_order ASC" +
            "</script>")
    List<SysMenu> selectMenusByRoleIds(@Param("roleIds") List<Long> roleIds);
}
