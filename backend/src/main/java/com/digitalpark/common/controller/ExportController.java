package com.digitalpark.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.digitalpark.common.utils.ExcelUtil;
import com.digitalpark.modules.energy.entity.EnergyMeter;
import com.digitalpark.modules.energy.mapper.EnergyMeterMapper;
import com.digitalpark.modules.system.entity.SysLog;
import com.digitalpark.modules.system.entity.SysLoginLog;
import com.digitalpark.modules.system.entity.SysMenu;
import com.digitalpark.modules.system.entity.SysRole;
import com.digitalpark.modules.system.entity.SysUser;
import com.digitalpark.modules.system.mapper.SysLogMapper;
import com.digitalpark.modules.system.mapper.SysLoginLogMapper;
import com.digitalpark.modules.system.mapper.SysMenuMapper;
import com.digitalpark.modules.system.mapper.SysRoleMapper;
import com.digitalpark.modules.system.mapper.SysUserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数据导出控制器
 *
 * @author digitalpark
 */
@Slf4j
@Tag(name = "数据导出")
@RestController
@RequestMapping("/export")
@RequiredArgsConstructor
public class ExportController {

    private final SysUserMapper sysUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysMenuMapper sysMenuMapper;
    private final SysLogMapper sysLogMapper;
    private final SysLoginLogMapper sysLoginLogMapper;
    private final EnergyMeterMapper energyMeterMapper;

    /**
     * 导出用户列表
     */
    @Operation(summary = "导出用户列表")
    @GetMapping("/users")
    public void exportUsers(HttpServletResponse response) throws IOException {
        List<SysUser> users = sysUserMapper.selectList(new LambdaQueryWrapper<>());
        List<String> headers = Arrays.asList("ID", "用户名", "昵称", "邮箱", "手机号", "状态", "创建时间", "备注");
        List<List<Object>> dataList = new ArrayList<>();
        for (SysUser user : users) {
            List<Object> row = new ArrayList<>();
            row.add(user.getId());
            row.add(user.getUsername());
            row.add(user.getNickname());
            row.add(user.getEmail());
            row.add(user.getPhone());
            row.add(user.getStatus() != null && user.getStatus() == 0 ? "正常" : "禁用");
            row.add(user.getCreateTime());
            row.add(user.getRemark());
            dataList.add(row);
        }
        ExcelUtil.export(response, "用户列表.xlsx", "用户列表", headers, dataList);
    }

    /**
     * 导出角色列表
     */
    @Operation(summary = "导出角色列表")
    @GetMapping("/roles")
    public void exportRoles(HttpServletResponse response) throws IOException {
        List<SysRole> roles = sysRoleMapper.selectList(new LambdaQueryWrapper<>());
        List<String> headers = Arrays.asList("ID", "角色名称", "角色编码", "排序", "状态", "创建时间", "备注");
        List<List<Object>> dataList = new ArrayList<>();
        for (SysRole role : roles) {
            List<Object> row = new ArrayList<>();
            row.add(role.getId());
            row.add(role.getRoleName());
            row.add(role.getRoleCode());
            row.add(role.getSortOrder());
            row.add(role.getStatus() != null && role.getStatus() == 0 ? "正常" : "禁用");
            row.add(role.getCreateTime());
            row.add(role.getRemark());
            dataList.add(row);
        }
        ExcelUtil.export(response, "角色列表.xlsx", "角色列表", headers, dataList);
    }

    /**
     * 导出菜单列表
     */
    @Operation(summary = "导出菜单列表")
    @GetMapping("/menus")
    public void exportMenus(HttpServletResponse response) throws IOException {
        List<SysMenu> menus = sysMenuMapper.selectList(new LambdaQueryWrapper<>());
        List<String> headers = Arrays.asList("ID", "父菜单ID", "菜单名称", "路由地址", "组件路径",
                "权限标识", "菜单类型", "排序", "是否可见", "状态", "创建时间");
        List<List<Object>> dataList = new ArrayList<>();
        for (SysMenu menu : menus) {
            List<Object> row = new ArrayList<>();
            row.add(menu.getId());
            row.add(menu.getParentId());
            row.add(menu.getMenuName());
            row.add(menu.getPath());
            row.add(menu.getComponent());
            row.add(menu.getPerms());
            row.add(menu.getMenuType());
            row.add(menu.getSortOrder());
            row.add(menu.getVisible() != null && menu.getVisible() == 0 ? "可见" : "隐藏");
            row.add(menu.getStatus() != null && menu.getStatus() == 0 ? "正常" : "禁用");
            row.add(menu.getCreateTime());
            dataList.add(row);
        }
        ExcelUtil.export(response, "菜单列表.xlsx", "菜单列表", headers, dataList);
    }

    /**
     * 导出操作日志
     */
    @Operation(summary = "导出操作日志")
    @GetMapping("/logs")
    public void exportLogs(HttpServletResponse response) throws IOException {
        List<SysLog> logs = sysLogMapper.selectList(new LambdaQueryWrapper<>());
        List<String> headers = Arrays.asList("ID", "模块名称", "操作类型", "请求URL", "请求方式",
                "操作IP", "耗时(ms)", "操作状态", "错误消息", "操作时间");
        List<List<Object>> dataList = new ArrayList<>();
        for (SysLog sysLog : logs) {
            List<Object> row = new ArrayList<>();
            row.add(sysLog.getId());
            row.add(sysLog.getModule());
            row.add(sysLog.getOperationType());
            row.add(sysLog.getRequestUrl());
            row.add(sysLog.getRequestMethod());
            row.add(sysLog.getIp());
            row.add(sysLog.getDuration());
            row.add(sysLog.getStatus() != null && sysLog.getStatus() == 0 ? "正常" : "异常");
            row.add(sysLog.getErrorMsg());
            row.add(sysLog.getCreateTime());
            dataList.add(row);
        }
        ExcelUtil.export(response, "操作日志.xlsx", "操作日志", headers, dataList);
    }

    /**
     * 导出登录日志
     */
    @Operation(summary = "导出登录日志")
    @GetMapping("/login-logs")
    public void exportLoginLogs(HttpServletResponse response) throws IOException {
        List<SysLoginLog> loginLogs = sysLoginLogMapper.selectList(new LambdaQueryWrapper<>());
        List<String> headers = Arrays.asList("ID", "用户名", "登录IP", "登录地点", "浏览器",
                "操作系统", "登录状态", "提示消息", "登录时间");
        List<List<Object>> dataList = new ArrayList<>();
        for (SysLoginLog loginLog : loginLogs) {
            List<Object> row = new ArrayList<>();
            row.add(loginLog.getId());
            row.add(loginLog.getUsername());
            row.add(loginLog.getIp());
            row.add(loginLog.getLocation());
            row.add(loginLog.getBrowser());
            row.add(loginLog.getOs());
            row.add(loginLog.getStatus() != null && loginLog.getStatus() == 0 ? "成功" : "失败");
            row.add(loginLog.getMessage());
            row.add(loginLog.getCreateTime());
            dataList.add(row);
        }
        ExcelUtil.export(response, "登录日志.xlsx", "登录日志", headers, dataList);
    }

    /**
     * 导出设备列表（能源计量表）
     */
    @Operation(summary = "导出设备列表")
    @GetMapping("/devices")
    public void exportDevices(HttpServletResponse response) throws IOException {
        List<EnergyMeter> meters = energyMeterMapper.selectList(new LambdaQueryWrapper<>());
        List<String> headers = Arrays.asList("ID", "计量表名称", "计量表编号", "类型", "能源类型",
                "计量单位", "精度等级", "最大量程", "安装位置", "楼层", "状态", "通信协议", "IP地址", "创建时间");
        List<List<Object>> dataList = new ArrayList<>();
        for (EnergyMeter meter : meters) {
            List<Object> row = new ArrayList<>();
            row.add(meter.getId());
            row.add(meter.getName());
            row.add(meter.getMeterNo());
            row.add(meter.getType());
            row.add(meter.getEnergyType());
            row.add(meter.getUnit());
            row.add(meter.getAccuracyClass());
            row.add(meter.getMaxRange());
            row.add(meter.getLocation());
            row.add(meter.getFloor());
            row.add(meter.getStatus() != null && meter.getStatus() == 1 ? "正常" : "停用");
            row.add(meter.getProtocol());
            row.add(meter.getIpAddress());
            row.add(meter.getCreateTime());
            dataList.add(row);
        }
        ExcelUtil.export(response, "设备列表.xlsx", "设备列表", headers, dataList);
    }
}
