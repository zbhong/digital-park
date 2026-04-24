package com.digitalpark.modules.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 用户创建/更新DTO
 *
 * @author digitalpark
 */
@Data
@Schema(description = "用户创建/更新请求")
public class UserDTO {

    /**
     * 用户ID（更新时必填）
     */
    @Schema(description = "用户ID")
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 密码（新增时必填）
     */
    @Schema(description = "密码")
    private String password;

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
     * 部门ID
     */
    @Schema(description = "部门ID")
    private Long deptId;

    /**
     * 角色ID列表
     */
    @Schema(description = "角色ID列表")
    private List<Long> roleIds;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
