package com.digitalpark.modules.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 路由信息VO（前端动态路由用）
 *
 * @author digitalpark
 */
@Data
@Schema(description = "路由信息")
public class RouteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 路由地址
     */
    @Schema(description = "路由地址")
    private String path;

    /**
     * 路由名称
     */
    @Schema(description = "路由名称")
    private String name;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径")
    private String component;

    /**
     * 路由元信息
     */
    @Schema(description = "路由元信息")
    private MetaVO meta;

    /**
     * 子路由
     */
    @Schema(description = "子路由")
    private List<RouteVO> children;

    /**
     * 是否隐藏
     */
    @Schema(description = "是否隐藏")
    private Boolean hidden;

    /**
     * 路由重定向
     */
    @Schema(description = "路由重定向")
    private String redirect;

    /**
     * 是否总是显示根菜单
     */
    @Schema(description = "是否总是显示根菜单")
    private Boolean alwaysShow;

    /**
     * 路由元信息
     */
    @Data
    @Schema(description = "路由元信息")
    public static class MetaVO implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 标题
         */
        @Schema(description = "标题")
        private String title;

        /**
         * 图标
         */
        @Schema(description = "图标")
        private String icon;

        /**
         * 角色列表
         */
        @Schema(description = "角色列表")
        private List<String> roles;

        /**
         * 是否缓存
         */
        @Schema(description = "是否缓存")
        private Boolean keepAlive;

        /**
         * 是否外链
         */
        @Schema(description = "是否外链")
        private Boolean link;

        /**
         * 是否固定在标签栏
         */
        @Schema(description = "是否固定在标签栏")
        private Boolean affix;

        /**
         * 面包屑
         */
        @Schema(description = "面包屑")
        private Boolean breadcrumb;

        /**
         * 是否显示标签栏
         */
        @Schema(description = "是否显示标签栏")
        private Boolean noCache;

        public MetaVO() {
        }

        public MetaVO(String title, String icon) {
            this.title = title;
            this.icon = icon;
            this.keepAlive = true;
            this.breadcrumb = true;
        }

        public MetaVO(String title, String icon, List<String> roles) {
            this.title = title;
            this.icon = icon;
            this.roles = roles;
            this.keepAlive = true;
            this.breadcrumb = true;
        }
    }
}
