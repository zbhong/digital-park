package com.digitalpark.modules.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户详情VO（不含密码）
 *
 * @author digitalpark
 */
@Data
@Schema(description = "用户详情")
public class UserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    private String nickname;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 头像
     */
    @Schema(description = "头像")
    private String avatar;

    /**
     * 状态（0-正常 1-禁用）
     */
    @Schema(description = "状态（0-正常 1-禁用）")
    private Integer status;

    /**
     * 组织ID
     */
    @Schema(description = "组织ID")
    private Long orgId;

    /**
     * 组织名称
     */
    @Schema(description = "组织名称")
    private String orgName;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private Long deptId;

    /**
     * 角色列表
     */
    @Schema(description = "角色列表")
    private List<RoleVO> roles;

    /**
     * 角色ID列表
     */
    @Schema(description = "角色ID列表")
    private List<Long> roleIds;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 角色简要信息
     */
    @Data
    @Schema(description = "角色简要信息")
    public static class RoleVO implements Serializable {

        private static final long serialVersionUID = 1L;

        @Schema(description = "角色ID")
        private Long id;

        @Schema(description = "角色名称")
        private String roleName;

        @Schema(description = "角色编码")
        private String roleCode;
    }
}
