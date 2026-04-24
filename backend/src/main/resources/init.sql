-- ============================================================
-- 数字园区综合管理平台 - 完整数据库初始化脚本
-- 数据库: MySQL 8.0
-- 字符集: utf8mb4
-- 引擎: InnoDB
-- 说明: 合并schema和所有初始数据，可直接在全新MySQL实例上执行
-- 用法: mysql -u root -p < digital-park-init.sql
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------
-- 创建数据库
-- -----------------------------------------------------------
CREATE DATABASE IF NOT EXISTS `digital_park` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `digital_park`;

-- ============================================================
-- 第一部分：表结构（所有CREATE TABLE语句）
-- ============================================================

-- Table structure for table `ai_report_instance`
--

DROP TABLE IF EXISTS `ai_report_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_report_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_id` bigint NOT NULL COMMENT '模板ID',
  `title` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '报告标题',
  `module` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '所属模块',
  `content` longtext COLLATE utf8mb4_general_ci COMMENT '报告内容',
  `generate_time` datetime DEFAULT NULL COMMENT '生成时间',
  `period` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '报告周期',
  `status` tinyint DEFAULT '0' COMMENT '状态（0生成中 1已生成 2生成失败）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_module` (`module`),
  KEY `idx_generate_time` (`generate_time`),
  KEY `idx_period` (`period`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI报告实例表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ai_report_template`
--

DROP TABLE IF EXISTS `ai_report_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_report_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `module` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '所属模块（能源/安全/设备/环境/经济/综合）',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '报告类型',
  `content_template` text COLLATE utf8mb4_general_ci COMMENT '内容模板',
  `cycle` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '生成周期（日/周/月/季/年）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0禁用 1启用）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_module` (`module`),
  KEY `idx_type` (`type`),
  KEY `idx_cycle` (`cycle`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='AI报告模板表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `api_config`
--

DROP TABLE IF EXISTS `api_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `api_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口名称',
  `api_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '接口编码',
  `api_category` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '接口分类（能源接口/设备接口/气象接口/碳交易接口/支付接口/第三方平台）',
  `api_url` varchar(512) COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口地址',
  `request_method` varchar(10) COLLATE utf8mb4_general_ci DEFAULT 'GET' COMMENT '请求方式（GET/POST/PUT/DELETE）',
  `request_headers` json DEFAULT NULL COMMENT '请求头（JSON格式）',
  `request_params` json DEFAULT NULL COMMENT '请求参数模板（JSON格式）',
  `response_format` varchar(32) COLLATE utf8mb4_general_ci DEFAULT 'JSON' COMMENT '响应格式（JSON/XML）',
  `auth_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '认证方式（无/APIKey/Bearer/OAuth2/签名等）',
  `auth_config` json DEFAULT NULL COMMENT '认证配置（JSON格式）',
  `timeout` int DEFAULT '30000' COMMENT '超时时间（毫秒）',
  `retry_count` int DEFAULT '3' COMMENT '重试次数',
  `retry_interval` int DEFAULT '5000' COMMENT '重试间隔（毫秒）',
  `call_frequency` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '调用频率（如每5分钟/每小时/每日）',
  `enabled` tinyint DEFAULT '1' COMMENT '是否启用（0禁用 1启用）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0禁用 1启用）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_api_code` (`api_code`),
  KEY `idx_api_category` (`api_category`),
  KEY `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='接口配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `api_log`
--

DROP TABLE IF EXISTS `api_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `api_id` bigint DEFAULT NULL COMMENT '接口配置ID',
  `api_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '接口名称',
  `api_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '接口编码',
  `request_url` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '请求URL',
  `request_method` varchar(10) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '请求方式',
  `request_headers` text COLLATE utf8mb4_general_ci COMMENT '请求头',
  `request_body` text COLLATE utf8mb4_general_ci COMMENT '请求体',
  `response_status` int DEFAULT NULL COMMENT '响应状态码',
  `response_body` text COLLATE utf8mb4_general_ci COMMENT '响应体',
  `cost_time` bigint DEFAULT '0' COMMENT '耗时（毫秒）',
  `retry_count` int DEFAULT '0' COMMENT '重试次数',
  `error_msg` text COLLATE utf8mb4_general_ci COMMENT '错误信息',
  `call_time` datetime DEFAULT NULL COMMENT '调用时间',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_api_id` (`api_id`),
  KEY `idx_api_code` (`api_code`),
  KEY `idx_call_time` (`call_time`),
  KEY `idx_response_status` (`response_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='接口日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asset_category`
--

DROP TABLE IF EXISTS `asset_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父分类ID（0为顶级）',
  `code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分类编码',
  `sort` int DEFAULT '0' COMMENT '排序',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asset_document`
--

DROP TABLE IF EXISTS `asset_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_document` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `name` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '文档名称',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '文档类型（CAD图纸/产权证明/说明书/检测报告/竣工资料）',
  `file_url` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '文件URL',
  `version` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '版本号',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产文档表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asset_info`
--

DROP TABLE IF EXISTS `asset_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '资产名称',
  `category_id` bigint DEFAULT NULL COMMENT '资产分类ID',
  `asset_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '资产编码',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '资产类型（不动产/智能化设备/公共设施/管网/电力/安防/应急/环境监测）',
  `brand` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '品牌',
  `model` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '型号',
  `purchase_date` date DEFAULT NULL COMMENT '采购日期',
  `purchase_price` decimal(16,2) DEFAULT '0.00' COMMENT '采购价格（元）',
  `supplier` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '供应商',
  `location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '存放位置',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `building_id` bigint DEFAULT NULL COMMENT '所属建筑ID',
  `floor` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '楼层',
  `room` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '房间号',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '在用' COMMENT '状态（在用/闲置/维修/报废）',
  `warranty_expire` date DEFAULT NULL COMMENT '保修截止日期',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_asset_code` (`asset_code`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_type` (`type`),
  KEY `idx_area_id` (`area_id`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asset_inventory`
--

DROP TABLE IF EXISTS `asset_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '盘点标题',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '待盘点' COMMENT '状态（待盘点/盘点中/已完成）',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `result` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '盘点结果',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_start_date` (`start_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产盘点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asset_inventory_item`
--

DROP TABLE IF EXISTS `asset_inventory_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_inventory_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `inventory_id` bigint NOT NULL COMMENT '盘点ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `actual_quantity` int DEFAULT '1' COMMENT '实际数量',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '正常' COMMENT '状态（正常/盘盈/盘亏/异常）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_inventory_id` (`inventory_id`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='盘点明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `asset_maintenance`
--

DROP TABLE IF EXISTS `asset_maintenance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asset_maintenance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `asset_id` bigint NOT NULL COMMENT '资产ID',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '维保类型（日常/定期/故障）',
  `plan_date` date DEFAULT NULL COMMENT '计划日期',
  `actual_date` date DEFAULT NULL COMMENT '实际日期',
  `content` text COLLATE utf8mb4_general_ci COMMENT '维保内容',
  `cost` decimal(12,2) DEFAULT '0.00' COMMENT '费用（元）',
  `executor` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '执行人',
  `result` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '维保结果',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_type` (`type`),
  KEY `idx_plan_date` (`plan_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='资产维保表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dashboard_config`
--

DROP TABLE IF EXISTS `dashboard_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dashboard_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dashboard_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '看板名称',
  `dashboard_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '看板编码',
  `dashboard_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '看板类型（能源总览/碳排总览/设备总览/安全总览/综合看板）',
  `layout_config` json DEFAULT NULL COMMENT '布局配置（JSON格式）',
  `widget_config` json DEFAULT NULL COMMENT '组件配置（JSON格式，含图表、指标卡片等）',
  `data_config` json DEFAULT NULL COMMENT '数据源配置（JSON格式）',
  `refresh_interval` int DEFAULT '60' COMMENT '刷新间隔（秒）',
  `is_default` tinyint DEFAULT '0' COMMENT '是否默认看板（0否 1是）',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态（0禁用 1启用）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_dashboard_code` (`dashboard_code`),
  KEY `idx_dashboard_type` (`dashboard_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='看板配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `decision_analysis`
--

DROP TABLE IF EXISTS `decision_analysis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `decision_analysis` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `analysis_name` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '分析名称',
  `analysis_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分析类型（节能潜力分析/碳减排路径分析/投资回报分析/风险评估/趋势预测）',
  `analysis_period` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分析周期',
  `analysis_config` json DEFAULT NULL COMMENT '分析配置（JSON格式）',
  `data_range` json DEFAULT NULL COMMENT '数据范围（JSON格式）',
  `result_data` json DEFAULT NULL COMMENT '分析结果数据（JSON格式）',
  `conclusion` text COLLATE utf8mb4_general_ci COMMENT '分析结论',
  `suggestions` text COLLATE utf8mb4_general_ci COMMENT '决策建议',
  `attachment_url` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '附件URL',
  `analysis_status` tinyint DEFAULT '0' COMMENT '分析状态（0待分析 1分析中 2已完成 3分析失败）',
  `analysis_time` datetime DEFAULT NULL COMMENT '分析时间',
  `create_user_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `create_user_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人姓名',
  `status` tinyint DEFAULT '1' COMMENT '状态（0禁用 1启用）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_analysis_type` (`analysis_type`),
  KEY `idx_analysis_status` (`analysis_status`),
  KEY `idx_analysis_time` (`analysis_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='决策分析表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_info`
--

DROP TABLE IF EXISTS `device_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `device_code` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备编码',
  `device_type` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型',
  `device_category` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '设备分类（能源设备/暖通设备/照明设备/安防设备/消防设备/其他）',
  `brand` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '品牌',
  `model` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '型号',
  `manufacturer` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '生产厂家',
  `serial_no` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '出厂编号',
  `building_id` bigint DEFAULT NULL COMMENT '所属建筑ID',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `install_location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '安装位置',
  `install_date` date DEFAULT NULL COMMENT '安装日期',
  `warranty_date` date DEFAULT NULL COMMENT '保修截止日期',
  `rated_power` decimal(12,4) DEFAULT '0.0000' COMMENT '额定功率（kW）',
  `rated_voltage` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '额定电压',
  `rated_current` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '额定电流',
  `communication_protocol` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '通讯协议',
  `ip_address` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT 'IP地址',
  `port` int DEFAULT '0' COMMENT '端口号',
  `responsible_id` bigint DEFAULT NULL COMMENT '负责人ID',
  `responsible_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '负责人姓名',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1运行 2故障 3维修中 4报废）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_device_code` (`device_code`),
  KEY `idx_device_type` (`device_type`),
  KEY `idx_device_category` (`device_category`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_area_id` (`area_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备台账表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_maintenance`
--

DROP TABLE IF EXISTS `device_maintenance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_maintenance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_id` bigint NOT NULL COMMENT '设备ID',
  `device_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '设备编码',
  `maintenance_type` tinyint DEFAULT '1' COMMENT '维保类型（1日常巡检 2定期保养 3故障维修 4大修 5技术改造）',
  `maintenance_plan_time` datetime DEFAULT NULL COMMENT '计划维保时间',
  `maintenance_time` datetime DEFAULT NULL COMMENT '实际维保时间',
  `maintenance_content` text COLLATE utf8mb4_general_ci COMMENT '维保内容',
  `maintenance_result` text COLLATE utf8mb4_general_ci COMMENT '维保结果',
  `maintainer_id` bigint DEFAULT NULL COMMENT '维保人ID',
  `maintainer_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '维保人姓名',
  `cost` decimal(12,2) DEFAULT '0.00' COMMENT '维保费用（元）',
  `spare_parts_used` json DEFAULT NULL COMMENT '使用的备品备件（JSON格式）',
  `next_plan_time` datetime DEFAULT NULL COMMENT '下次计划维保时间',
  `maintenance_status` tinyint DEFAULT '0' COMMENT '维保状态（0待执行 1进行中 2已完成 3已取消）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_maintenance_type` (`maintenance_type`),
  KEY `idx_maintenance_status` (`maintenance_status`),
  KEY `idx_maintenance_time` (`maintenance_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备维保记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_spare`
--

DROP TABLE IF EXISTS `device_spare`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_spare` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `spare_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '备件名称',
  `spare_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备件编码',
  `spare_category` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备件分类（电气/机械/仪表/电子元件/其他）',
  `specification` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '规格型号',
  `brand` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '品牌',
  `unit` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '计量单位',
  `stock_qty` int DEFAULT '0' COMMENT '库存数量',
  `safety_stock` int DEFAULT '0' COMMENT '安全库存',
  `unit_price` decimal(12,2) DEFAULT '0.00' COMMENT '单价（元）',
  `supplier` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '供应商',
  `storage_location` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '存放位置',
  `applicable_devices` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '适用设备',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1正常）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_spare_code` (`spare_code`),
  KEY `idx_spare_category` (`spare_category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='备品备件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_status`
--

DROP TABLE IF EXISTS `device_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `device_status` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_id` bigint NOT NULL COMMENT '设备ID',
  `device_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '设备编码',
  `run_status` tinyint DEFAULT '1' COMMENT '运行状态（0停机 1运行 2待机 3故障）',
  `health_status` tinyint DEFAULT '1' COMMENT '健康状态（1良好 2一般 3注意 4异常）',
  `temperature` decimal(8,2) DEFAULT NULL COMMENT '温度（摄氏度）',
  `humidity` decimal(5,2) DEFAULT NULL COMMENT '湿度（%）',
  `vibration` decimal(10,4) DEFAULT NULL COMMENT '振动值',
  `voltage` decimal(10,2) DEFAULT NULL COMMENT '电压（V）',
  `current` decimal(10,2) DEFAULT NULL COMMENT '电流（A）',
  `power` decimal(12,4) DEFAULT NULL COMMENT '功率（kW）',
  `runtime_hours` decimal(12,2) DEFAULT '0.00' COMMENT '累计运行时长（小时）',
  `alarm_info` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '告警信息',
  `collect_time` datetime NOT NULL COMMENT '采集时间',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_device_code` (`device_code`),
  KEY `idx_run_status` (`run_status`),
  KEY `idx_collect_time` (`collect_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备状态记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `economy_enterprise_profile`
--

DROP TABLE IF EXISTS `economy_enterprise_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `economy_enterprise_profile` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `enterprise_id` bigint NOT NULL COMMENT '企业ID',
  `industry` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '所属行业',
  `scale` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '企业规模（大型/中型/小型/微型）',
  `revenue` decimal(16,2) DEFAULT '0.00' COMMENT '营业收入（万元）',
  `tax` decimal(16,2) DEFAULT '0.00' COMMENT '纳税额（万元）',
  `employees` int DEFAULT '0' COMMENT '员工人数',
  `credit_rating` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '信用评级（AAA/AA/A/BBB/BB/B等）',
  `risk_level` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '低' COMMENT '风险等级（低/中/高）',
  `tags_json` json DEFAULT NULL COMMENT '标签（JSON格式）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_enterprise_id` (`enterprise_id`),
  KEY `idx_industry` (`industry`),
  KEY `idx_scale` (`scale`),
  KEY `idx_credit_rating` (`credit_rating`),
  KEY `idx_risk_level` (`risk_level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='企业画像表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `economy_indicator`
--

DROP TABLE IF EXISTS `economy_indicator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `economy_indicator` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '指标名称',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '指标类型（GDP/产值/税收/就业/投资等）',
  `unit` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '单位',
  `value` decimal(16,4) DEFAULT '0.0000' COMMENT '指标值',
  `period` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '统计周期（如2026-Q1）',
  `enterprise_id` bigint DEFAULT NULL COMMENT '关联企业ID',
  `area_id` bigint DEFAULT NULL COMMENT '关联区域ID',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_period` (`period`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_area_id` (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='经济指标表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `economy_report`
--

DROP TABLE IF EXISTS `economy_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `economy_report` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '报告标题',
  `type` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '报告类型（月报/季报/年报）',
  `period` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '统计周期',
  `content` text COLLATE utf8mb4_general_ci COMMENT '报告内容',
  `status` tinyint DEFAULT '0' COMMENT '状态（0草稿 1已发布）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_period` (`period`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='经济报告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `emergency_drill`
--

DROP TABLE IF EXISTS `emergency_drill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_drill` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `plan_id` bigint DEFAULT NULL COMMENT '关联预案ID',
  `name` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '演练名称',
  `drill_date` date DEFAULT NULL COMMENT '演练日期',
  `location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '演练地点',
  `participants` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '参与人员',
  `result` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '演练结果',
  `summary` text COLLATE utf8mb4_general_ci COMMENT '演练总结',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_plan_id` (`plan_id`),
  KEY `idx_drill_date` (`drill_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应急演练表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `emergency_event`
--

DROP TABLE IF EXISTS `emergency_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_event` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '事件标题',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '事件类型（火灾/自然灾害/设备故障/环境污染/安全事件/公共卫生）',
  `level` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '一般' COMMENT '事件等级（一般/较大/重大/特大）',
  `description` text COLLATE utf8mb4_general_ci COMMENT '事件描述',
  `location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '事件地点',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '待处理' COMMENT '处理状态（待处理/处理中/已处理/已结案）',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_result` text COLLATE utf8mb4_general_ci COMMENT '处理结果',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_level` (`level`),
  KEY `idx_status` (`status`),
  KEY `idx_handler_id` (`handler_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应急事件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `emergency_material`
--

DROP TABLE IF EXISTS `emergency_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_material` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '物资名称',
  `category` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '物资分类（防护用品/消防器材/急救物资/通讯设备/照明设备/其他）',
  `quantity` int DEFAULT '0' COMMENT '数量',
  `unit` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '单位',
  `location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '存放位置',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `expire_date` date DEFAULT NULL COMMENT '有效期',
  `status` tinyint DEFAULT '1' COMMENT '状态（0过期 1正常 2不足）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_area_id` (`area_id`),
  KEY `idx_status` (`status`),
  KEY `idx_expire_date` (`expire_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应急物资表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `emergency_plan`
--

DROP TABLE IF EXISTS `emergency_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_plan` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '预案名称',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '预案类型（火灾/电力/天气/污染/生产安全）',
  `content` text COLLATE utf8mb4_general_ci COMMENT '预案内容',
  `attachment` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '附件URL',
  `status` tinyint DEFAULT '1' COMMENT '状态（0草稿 1已发布 2已废止）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应急预案表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `emergency_team`
--

DROP TABLE IF EXISTS `emergency_team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emergency_team` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '队伍名称',
  `leader` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '负责人',
  `members_json` json DEFAULT NULL COMMENT '队员列表（JSON格式）',
  `phone` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '联系电话',
  `duty` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '职责描述',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='应急队伍表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `energy_charging`
--

DROP TABLE IF EXISTS `energy_charging`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `energy_charging` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `charging_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '充电桩名称',
  `charging_code` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '充电桩编码',
  `charging_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '充电桩类型（交流慢充/直流快充/超级快充）',
  `power` decimal(10,4) DEFAULT '0.0000' COMMENT '额定功率（kW）',
  `connector_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '接口类型（国标/欧标/特斯拉等）',
  `connector_count` int DEFAULT '1' COMMENT '接口数量',
  `building_id` bigint DEFAULT NULL COMMENT '所属建筑ID',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `price_type` tinyint DEFAULT '1' COMMENT '计费类型（1按电量 2按时间 3包月）',
  `unit_price` decimal(8,4) DEFAULT '0.0000' COMMENT '单价（元/kWh或元/分钟）',
  `service_fee` decimal(8,4) DEFAULT '0.0000' COMMENT '服务费（元/kWh）',
  `network_status` tinyint DEFAULT '1' COMMENT '联网状态（0离线 1在线）',
  `work_status` tinyint DEFAULT '0' COMMENT '工作状态（0空闲 1充电中 2故障 3预约中）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1启用）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_charging_code` (`charging_code`),
  KEY `idx_charging_type` (`charging_type`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_work_status` (`work_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='充电桩表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `energy_charging_record`
--

DROP TABLE IF EXISTS `energy_charging_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `energy_charging_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `charging_id` bigint NOT NULL COMMENT '充电桩ID',
  `charging_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '充电桩编码',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `user_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '用户姓名',
  `vehicle_no` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '车牌号',
  `connector_no` int DEFAULT '1' COMMENT '接口编号',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `duration` int DEFAULT '0' COMMENT '充电时长（分钟）',
  `charge_energy` decimal(12,4) DEFAULT '0.0000' COMMENT '充电量（kWh）',
  `charge_amount` decimal(10,2) DEFAULT '0.00' COMMENT '充电金额（元）',
  `service_amount` decimal(10,2) DEFAULT '0.00' COMMENT '服务费（元）',
  `total_amount` decimal(10,2) DEFAULT '0.00' COMMENT '总金额（元）',
  `start_soc` decimal(5,2) DEFAULT '0.00' COMMENT '开始SOC（%）',
  `end_soc` decimal(5,2) DEFAULT '0.00' COMMENT '结束SOC（%）',
  `pay_status` tinyint DEFAULT '0' COMMENT '支付状态（0未支付 1已支付 2退款中 3已退款）',
  `pay_method` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '支付方式',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_charging_id` (`charging_id`),
  KEY `idx_charging_code` (`charging_code`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_vehicle_no` (`vehicle_no`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_pay_status` (`pay_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='充电记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `energy_grid`
--

DROP TABLE IF EXISTS `energy_grid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `energy_grid` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `grid_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '配电网名称',
  `grid_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '配电网编码',
  `voltage_level` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '电压等级（10kV/380V/220V）',
  `transformer_capacity` decimal(12,4) DEFAULT '0.0000' COMMENT '变压器容量（kVA）',
  `transformer_count` int DEFAULT '0' COMMENT '变压器数量',
  `line_length` decimal(12,2) DEFAULT '0.00' COMMENT '线路长度（km）',
  `max_load` decimal(12,4) DEFAULT '0.0000' COMMENT '最大负荷（kW）',
  `current_load` decimal(12,4) DEFAULT '0.0000' COMMENT '当前负荷（kW）',
  `load_rate` decimal(5,2) DEFAULT '0.00' COMMENT '负载率（%）',
  `power_factor` decimal(5,4) DEFAULT '0.0000' COMMENT '功率因数',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `daily_supply` decimal(16,4) DEFAULT '0.0000' COMMENT '日供电量（kWh）',
  `monthly_supply` decimal(16,4) DEFAULT '0.0000' COMMENT '月供电量（kWh）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1正常 2故障）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_grid_code` (`grid_code`),
  KEY `idx_voltage_level` (`voltage_level`),
  KEY `idx_area_id` (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='配电网信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `energy_load`
--

DROP TABLE IF EXISTS `energy_load`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `energy_load` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `load_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '负荷名称',
  `load_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '负荷编码',
  `load_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '负荷类型（照明/空调/动力/特殊负荷）',
  `building_id` bigint DEFAULT NULL COMMENT '所属建筑ID',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `enterprise_id` bigint DEFAULT NULL COMMENT '关联企业ID',
  `rated_power` decimal(12,4) DEFAULT '0.0000' COMMENT '额定功率（kW）',
  `current_power` decimal(12,4) DEFAULT '0.0000' COMMENT '当前功率（kW）',
  `max_power` decimal(12,4) DEFAULT '0.0000' COMMENT '最大负荷（kW）',
  `peak_load_time` datetime DEFAULT NULL COMMENT '峰值负荷时间',
  `valley_load_time` datetime DEFAULT NULL COMMENT '谷值负荷时间',
  `avg_daily_load` decimal(12,4) DEFAULT '0.0000' COMMENT '日平均负荷（kW）',
  `load_level` tinyint DEFAULT '1' COMMENT '负荷等级（1低 2中 3高 4极高）',
  `shift_enabled` tinyint DEFAULT '0' COMMENT '是否参与移峰填谷（0否 1是）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1正常）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_load_code` (`load_code`),
  KEY `idx_load_type` (`load_type`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_enterprise_id` (`enterprise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='负荷管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `energy_meter`
--

DROP TABLE IF EXISTS `energy_meter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `energy_meter` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `meter_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '计量表名称',
  `meter_code` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '计量表编码',
  `meter_type` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '计量类型（电表/水表/气表/热表）',
  `energy_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '能源类型（电/水/天然气/蒸汽/集中供热等）',
  `building_id` bigint DEFAULT NULL COMMENT '所属建筑ID',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `device_id` bigint DEFAULT NULL COMMENT '关联设备ID',
  `enterprise_id` bigint DEFAULT NULL COMMENT '关联企业ID',
  `meter_model` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '表计型号',
  `manufacturer` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '生产厂家',
  `install_date` date DEFAULT NULL COMMENT '安装日期',
  `install_location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '安装位置',
  `initial_value` decimal(16,4) DEFAULT '0.0000' COMMENT '初始读数',
  `current_value` decimal(16,4) DEFAULT '0.0000' COMMENT '当前读数',
  `communication_protocol` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '通讯协议（Modbus/DL-T645/MQTT等）',
  `accuracy_class` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '精度等级',
  `max_range` decimal(16,4) DEFAULT NULL COMMENT '最大量程',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1正常 2故障）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_meter_code` (`meter_code`),
  KEY `idx_meter_type` (`meter_type`),
  KEY `idx_energy_type` (`energy_type`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_enterprise_id` (`enterprise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='能源计量表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `energy_record`
--

DROP TABLE IF EXISTS `energy_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `energy_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `meter_id` bigint NOT NULL COMMENT '计量表ID',
  `meter_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '计量表编码',
  `energy_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '能源类型',
  `read_time` datetime NOT NULL COMMENT '抄表时间',
  `read_value` decimal(16,4) NOT NULL COMMENT '读数',
  `consumption` decimal(16,4) DEFAULT '0.0000' COMMENT '消耗量',
  `unit` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '单位',
  `cost` decimal(16,4) DEFAULT '0.0000' COMMENT '费用（元）',
  `carbon_emission` decimal(16,4) DEFAULT '0.0000' COMMENT '碳排放量（tCO2e）',
  `data_type` tinyint DEFAULT '1' COMMENT '数据类型（1实时 2小时 3日 4月）',
  `quality_code` tinyint DEFAULT '1' COMMENT '数据质量码（1正常 2疑似异常 3估算）',
  `peak_consumption` decimal(16,4) DEFAULT '0.0000' COMMENT '峰时消耗量',
  `flat_consumption` decimal(16,4) DEFAULT '0.0000' COMMENT '平时消耗量',
  `valley_consumption` decimal(16,4) DEFAULT '0.0000' COMMENT '谷时消耗量',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_meter_id` (`meter_id`),
  KEY `idx_meter_code` (`meter_code`),
  KEY `idx_energy_type` (`energy_type`),
  KEY `idx_read_time` (`read_time`),
  KEY `idx_data_type` (`data_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='能源计量记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `energy_source`
--

DROP TABLE IF EXISTS `energy_source`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `energy_source` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `source_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `source_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '设备编码',
  `source_type` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型（光伏/风电/储能/燃气发电等）',
  `capacity` decimal(12,4) DEFAULT '0.0000' COMMENT '装机容量（kW）',
  `building_id` bigint DEFAULT NULL COMMENT '所属建筑ID',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `manufacturer` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '生产厂家',
  `model` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '设备型号',
  `install_date` date DEFAULT NULL COMMENT '安装日期',
  `grid_connected` tinyint DEFAULT '1' COMMENT '是否并网（0否 1是）',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `annual_output` decimal(16,4) DEFAULT '0.0000' COMMENT '年发电量（kWh）',
  `efficiency` decimal(5,2) DEFAULT '0.00' COMMENT '转换效率（%）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1运行 2故障 3维修中）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_source_code` (`source_code`),
  KEY `idx_source_type` (`source_type`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_area_id` (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='能源源侧设备表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `energy_storage`
--

DROP TABLE IF EXISTS `energy_storage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `energy_storage` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `storage_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '储能系统名称',
  `storage_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '储能系统编码',
  `storage_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '储能类型（锂电池/液流电池/超级电容/飞轮储能等）',
  `rated_capacity` decimal(12,4) DEFAULT '0.0000' COMMENT '额定容量（kWh）',
  `rated_power` decimal(12,4) DEFAULT '0.0000' COMMENT '额定功率（kW）',
  `current_soc` decimal(5,2) DEFAULT '0.00' COMMENT '当前SOC（%）',
  `current_power` decimal(12,4) DEFAULT '0.0000' COMMENT '当前功率（kW）',
  `total_charge` decimal(16,4) DEFAULT '0.0000' COMMENT '累计充电量（kWh）',
  `total_discharge` decimal(16,4) DEFAULT '0.0000' COMMENT '累计放电量（kWh）',
  `cycle_count` int DEFAULT '0' COMMENT '循环次数',
  `health_status` decimal(5,2) DEFAULT '100.00' COMMENT '健康度（%）',
  `building_id` bigint DEFAULT NULL COMMENT '所属建筑ID',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `run_mode` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '运行模式（峰谷套利/需求响应/平滑出力/备用电源）',
  `daily_income` decimal(12,2) DEFAULT '0.00' COMMENT '日收益（元）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1充电 2放电 3待机 4故障）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_storage_code` (`storage_code`),
  KEY `idx_storage_type` (`storage_type`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_area_id` (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='储能系统表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enterprise_info`
--

DROP TABLE IF EXISTS `enterprise_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enterprise_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '企业名称',
  `code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '企业编码',
  `industry` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '所属行业',
  `scale` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '企业规模',
  `contact_person` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '联系人',
  `contact_phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '联系电话',
  `email` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '邮箱',
  `address` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '地址',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `building_id` bigint DEFAULT NULL COMMENT '所属建筑ID',
  `floor` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '楼层',
  `room` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '房间号',
  `enter_date` date DEFAULT NULL COMMENT '入驻日期',
  `contract_expire` date DEFAULT NULL COMMENT '合同到期日期',
  `status` tinyint DEFAULT '1' COMMENT '状态（0退租 1正常 2待入驻）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_code` (`code`),
  KEY `idx_industry` (`industry`),
  KEY `idx_area_id` (`area_id`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='企业信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enterprise_policy`
--

DROP TABLE IF EXISTS `enterprise_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enterprise_policy` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '政策标题',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '政策类型（产业扶持/税收优惠/人才引进/科技创新/绿色发展）',
  `industry_scope` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '适用行业范围',
  `enterprise_scale` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '适用企业规模',
  `content` text COLLATE utf8mb4_general_ci COMMENT '政策内容',
  `attachment` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '附件URL',
  `publish_date` date DEFAULT NULL COMMENT '发布日期',
  `expire_date` date DEFAULT NULL COMMENT '失效日期',
  `status` tinyint DEFAULT '1' COMMENT '状态（0已失效 1有效）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_publish_date` (`publish_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='政策信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enterprise_service_request`
--

DROP TABLE IF EXISTS `enterprise_service_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enterprise_service_request` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `enterprise_id` bigint NOT NULL COMMENT '企业ID',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '服务类型（政策申报/法律咨询/金融服务/物业报修/能耗咨询/环境指导）',
  `title` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求标题',
  `description` text COLLATE utf8mb4_general_ci COMMENT '请求描述',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '待处理' COMMENT '状态（待处理/处理中/已完成/已关闭）',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_result` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '处理结果',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_handler_id` (`handler_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='服务请求表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `env_monitor_data`
--

DROP TABLE IF EXISTS `env_monitor_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `env_monitor_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `point_id` bigint NOT NULL COMMENT '监测点ID',
  `point_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '监测点编码',
  `temperature` decimal(6,2) DEFAULT NULL COMMENT '温度（摄氏度）',
  `humidity` decimal(5,2) DEFAULT NULL COMMENT '湿度（%）',
  `pm25` decimal(8,2) DEFAULT NULL COMMENT 'PM2.5（ug/m3）',
  `pm10` decimal(8,2) DEFAULT NULL COMMENT 'PM10（ug/m3）',
  `co2` decimal(8,2) DEFAULT NULL COMMENT 'CO2浓度（ppm）',
  `tvoc` decimal(8,2) DEFAULT NULL COMMENT 'TVOC（mg/m3）',
  `noise` decimal(6,2) DEFAULT NULL COMMENT '噪音（dB）',
  `wind_speed` decimal(6,2) DEFAULT NULL COMMENT '风速（m/s）',
  `wind_direction` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '风向',
  `illumination` decimal(10,2) DEFAULT NULL COMMENT '照度（Lux）',
  `air_pressure` decimal(8,2) DEFAULT NULL COMMENT '气压（hPa）',
  `so2` decimal(8,2) DEFAULT NULL COMMENT 'SO2（ug/m3）',
  `no2` decimal(8,2) DEFAULT NULL COMMENT 'NO2（ug/m3）',
  `o3` decimal(8,2) DEFAULT NULL COMMENT 'O3（ug/m3）',
  `water_ph` decimal(5,2) DEFAULT NULL COMMENT '水质pH值',
  `water_turbidity` decimal(8,2) DEFAULT NULL COMMENT '水质浊度（NTU）',
  `collect_time` datetime NOT NULL COMMENT '采集时间',
  `quality_code` tinyint DEFAULT '1' COMMENT '数据质量码（1正常 2疑似异常 3离线估算）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_point_id` (`point_id`),
  KEY `idx_point_code` (`point_code`),
  KEY `idx_collect_time` (`collect_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='环境监测数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `env_monitor_point`
--

DROP TABLE IF EXISTS `env_monitor_point`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `env_monitor_point` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `point_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '监测点名称',
  `point_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '监测点编码',
  `monitor_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '监测类型（气象/空气质量/噪音/水质/土壤）',
  `building_id` bigint DEFAULT NULL COMMENT '所属建筑ID',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `install_location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '安装位置',
  `device_id` bigint DEFAULT NULL COMMENT '关联设备ID',
  `monitor_items` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '监测项（温度/湿度/PM2.5/PM10/CO2/噪音等）',
  `indoor_outdoor` tinyint DEFAULT '1' COMMENT '室内外（0室内 1室外）',
  `map_2d_bind` json DEFAULT NULL COMMENT '2D地图绑定配置（JSON格式）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1正常 2故障）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_point_code` (`point_code`),
  KEY `idx_monitor_type` (`monitor_type`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_area_id` (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='环境监测点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `investment_asset`
--

DROP TABLE IF EXISTS `investment_asset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investment_asset` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `building_id` bigint DEFAULT NULL COMMENT '所属建筑ID',
  `floor` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '楼层',
  `room_number` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '房间号',
  `area` decimal(12,2) DEFAULT '0.00' COMMENT '面积（平方米）',
  `price_per_sqm` decimal(10,2) DEFAULT '0.00' COMMENT '单价（元/平方米/月）',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '可租' COMMENT '状态（可租/已租/预留/不可租）',
  `enterprise_id` bigint DEFAULT NULL COMMENT '入驻企业ID',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_floor` (`floor`),
  KEY `idx_status` (`status`),
  KEY `idx_enterprise_id` (`enterprise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='招商房源表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `investment_contract`
--

DROP TABLE IF EXISTS `investment_contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investment_contract` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_id` bigint DEFAULT NULL COMMENT '客户ID',
  `enterprise_id` bigint DEFAULT NULL COMMENT '企业ID',
  `asset_id` bigint DEFAULT NULL COMMENT '房源ID',
  `contract_no` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '合同编号',
  `start_date` date DEFAULT NULL COMMENT '开始日期',
  `end_date` date DEFAULT NULL COMMENT '结束日期',
  `rent_amount` decimal(12,2) DEFAULT '0.00' COMMENT '租金（元/月）',
  `deposit` decimal(12,2) DEFAULT '0.00' COMMENT '押金（元）',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '草稿' COMMENT '状态（草稿/审批中/生效中/已终止）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_contract_no` (`contract_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_asset_id` (`asset_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='合同表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `investment_customer`
--

DROP TABLE IF EXISTS `investment_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investment_customer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户名称',
  `contact_person` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '联系人',
  `contact_phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '联系电话',
  `company` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '公司名称',
  `industry` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '所属行业',
  `demand_area` decimal(12,2) DEFAULT NULL COMMENT '需求面积（平方米）',
  `demand_budget` decimal(16,2) DEFAULT NULL COMMENT '预算范围（元/月）',
  `source` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '客户来源（线上/线下/转介绍/自荐）',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '公海' COMMENT '状态（公海/跟进中/已签约/已流失）',
  `follow_user_id` bigint DEFAULT NULL COMMENT '跟进人ID',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_industry` (`industry`),
  KEY `idx_source` (`source`),
  KEY `idx_status` (`status`),
  KEY `idx_follow_user_id` (`follow_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='客户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `investment_follow`
--

DROP TABLE IF EXISTS `investment_follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investment_follow` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `opportunity_id` bigint DEFAULT NULL COMMENT '商机ID',
  `content` text COLLATE utf8mb4_general_ci COMMENT '跟进内容',
  `follow_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '跟进方式（电话/微信/面谈/邮件）',
  `next_follow_date` date DEFAULT NULL COMMENT '下次跟进日期',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_opportunity_id` (`opportunity_id`),
  KEY `idx_next_follow_date` (`next_follow_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='跟进记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `investment_opportunity`
--

DROP TABLE IF EXISTS `investment_opportunity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investment_opportunity` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `stage` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '初次接洽' COMMENT '阶段（初次接洽/实地考察/价格谈判/签约入驻）',
  `area_demand` decimal(12,2) DEFAULT NULL COMMENT '需求面积（平方米）',
  `budget_range` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '预算范围',
  `expected_date` date DEFAULT NULL COMMENT '期望入驻日期',
  `probability` decimal(5,2) DEFAULT '0.00' COMMENT '成交概率（%）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_stage` (`stage`),
  KEY `idx_probability` (`probability`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商机表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `investment_policy`
--

DROP TABLE IF EXISTS `investment_policy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `investment_policy` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '政策名称',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '政策类型（产业扶持/税收优惠/租金减免/装修补贴）',
  `industry_scope` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '适用行业范围',
  `enterprise_scale` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '适用企业规模',
  `content` text COLLATE utf8mb4_general_ci COMMENT '政策内容',
  `status` tinyint DEFAULT '1' COMMENT '状态（0已失效 1有效）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='招商政策表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `park_area`
--

DROP TABLE IF EXISTS `park_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `park_area` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父区域ID（0为顶级）',
  `area_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '区域名称',
  `area_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '区域编码',
  `area_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '区域类型（生产区/办公区/绿化区/停车区等）',
  `area_level` int DEFAULT '1' COMMENT '区域层级',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '中心经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '中心纬度',
  `map_2d_config` json DEFAULT NULL COMMENT '2D区域配置（JSON格式）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1正常）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_area_code` (`area_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='园区区域表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `park_building`
--

DROP TABLE IF EXISTS `park_building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `park_building` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `building_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '建筑名称',
  `building_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '建筑编码',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `building_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '建筑类型（办公/生产/仓储/研发等）',
  `floor_count` int DEFAULT '0' COMMENT '楼层数',
  `total_area` decimal(12,2) DEFAULT '0.00' COMMENT '总面积（平方米）',
  `usable_area` decimal(12,2) DEFAULT '0.00' COMMENT '可用面积（平方米）',
  `longitude` decimal(10,6) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,6) DEFAULT NULL COMMENT '纬度',
  `model_url` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '3D模型URL',
  `map_2d_url` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '2D平面图URL',
  `map_2d_config` json DEFAULT NULL COMMENT '2D地图配置（JSON格式，含坐标、缩放等）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1正常 2维修中）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_area_id` (`area_id`),
  KEY `idx_building_code` (`building_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='园区建筑表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `property_facility`
--

DROP TABLE IF EXISTS `property_facility`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `property_facility` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设施名称',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '设施类型（电梯/空调/给排水/照明/环卫）',
  `location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '位置',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `last_check_date` date DEFAULT NULL COMMENT '上次检查日期',
  `next_check_date` date DEFAULT NULL COMMENT '下次检查日期',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1正常 2故障 3维修中）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_area_id` (`area_id`),
  KEY `idx_status` (`status`),
  KEY `idx_next_check_date` (`next_check_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设施运维表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `property_fee`
--

DROP TABLE IF EXISTS `property_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `property_fee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `enterprise_id` bigint NOT NULL COMMENT '企业ID',
  `fee_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '费用类型（物业费/水电费/停车费/租赁费）',
  `amount` decimal(12,2) NOT NULL COMMENT '金额（元）',
  `period` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '账单周期（如2026-01）',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '待缴' COMMENT '状态（待缴/已缴/逾期）',
  `pay_time` datetime DEFAULT NULL COMMENT '缴费时间',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_fee_type` (`fee_type`),
  KEY `idx_period` (`period`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='物业费表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `property_vehicle`
--

DROP TABLE IF EXISTS `property_vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `property_vehicle` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `plate_number` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '车牌号',
  `owner_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '车主姓名',
  `owner_type` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '企业' COMMENT '车主类型（企业/访客/员工）',
  `enterprise_id` bigint DEFAULT NULL COMMENT '关联企业ID',
  `card_type` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '临时' COMMENT '卡类型（月卡/临时）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0禁用 1正常）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_plate_number` (`plate_number`),
  KEY `idx_owner_type` (`owner_type`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='车辆管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `property_visitor`
--

DROP TABLE IF EXISTS `property_visitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `property_visitor` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '访客姓名',
  `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '手机号',
  `id_card` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '身份证号',
  `visit_enterprise` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '访问企业',
  `visit_person` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '被访人',
  `purpose` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '来访目的',
  `visit_date` datetime DEFAULT NULL COMMENT '来访时间',
  `status` tinyint DEFAULT '0' COMMENT '状态（0预约中 1已到访 2已离开）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_phone` (`phone`),
  KEY `idx_visit_enterprise` (`visit_enterprise`(64)),
  KEY `idx_visit_date` (`visit_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='访客管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `property_work_order`
--

DROP TABLE IF EXISTS `property_work_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `property_work_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `enterprise_id` bigint DEFAULT NULL COMMENT '企业ID',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '工单类型（保洁/绿化/安保/会务/维修/其他）',
  `title` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '工单标题',
  `description` text COLLATE utf8mb4_general_ci COMMENT '工单描述',
  `priority` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '中' COMMENT '优先级（低/中/高/紧急）',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '待派单' COMMENT '状态（待派单/已派单/处理中/已完成/已评价）',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `satisfaction` tinyint DEFAULT NULL COMMENT '满意度评分（1-5分）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_type` (`type`),
  KEY `idx_priority` (`priority`),
  KEY `idx_status` (`status`),
  KEY `idx_handler_id` (`handler_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='物业工单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_instance`
--

DROP TABLE IF EXISTS `report_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report_instance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_id` bigint NOT NULL COMMENT '模板ID',
  `template_name` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '模板名称',
  `report_name` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '报表名称',
  `report_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '报表类型',
  `period_year` int DEFAULT '0' COMMENT '年份',
  `period_month` int DEFAULT '0' COMMENT '月份',
  `period_start` date DEFAULT NULL COMMENT '数据开始日期',
  `period_end` date DEFAULT NULL COMMENT '数据结束日期',
  `file_url` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '报表文件URL',
  `file_size` bigint DEFAULT '0' COMMENT '文件大小（字节）',
  `generate_status` tinyint DEFAULT '0' COMMENT '生成状态（0待生成 1生成中 2已生成 3生成失败）',
  `generate_time` datetime DEFAULT NULL COMMENT '生成时间',
  `generate_user_id` bigint DEFAULT NULL COMMENT '生成人ID',
  `generate_user_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '生成人姓名',
  `status` tinyint DEFAULT '1' COMMENT '状态（0禁用 1启用）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_report_type` (`report_type`),
  KEY `idx_period` (`period_year`,`period_month`),
  KEY `idx_generate_status` (`generate_status`),
  KEY `idx_generate_time` (`generate_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='报表实例表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `report_template`
--

DROP TABLE IF EXISTS `report_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `report_template` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_name` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `template_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '模板编码',
  `report_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '报表类型（能源报表/碳排报表/设备报表/综合报表/自定义报表）',
  `template_format` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '模板格式（PDF/Excel/Word）',
  `data_source` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '数据源配置',
  `template_config` json DEFAULT NULL COMMENT '模板配置（JSON格式，含布局、字段、样式等）',
  `chart_config` json DEFAULT NULL COMMENT '图表配置（JSON格式）',
  `generate_cycle` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '生成周期（手动/每日/每周/每月/每季/每年）',
  `is_system` tinyint DEFAULT '0' COMMENT '是否系统内置（0否 1是）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0禁用 1启用）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_template_code` (`template_code`),
  KEY `idx_report_type` (`report_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='报表模板表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `safety_event`
--

DROP TABLE IF EXISTS `safety_event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `safety_event` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '事件类型（闯入/火灾/用电异常/生产安全/网络安全/人员聚集/周界报警）',
  `level` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '一般' COMMENT '事件等级（一般/较大/重大/特大）',
  `source` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '事件来源',
  `description` text COLLATE utf8mb4_general_ci COMMENT '事件描述',
  `location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '事件位置',
  `monitor_point_id` bigint DEFAULT NULL COMMENT '关联监测点位ID',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '待处理' COMMENT '处理状态（待处理/处理中/已处理/已关闭）',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_result` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '处理结果',
  `images` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '事件图片（多张以逗号分隔）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_level` (`level`),
  KEY `idx_status` (`status`),
  KEY `idx_monitor_point_id` (`monitor_point_id`),
  KEY `idx_handler_id` (`handler_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='安全事件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `safety_fire_equipment`
--

DROP TABLE IF EXISTS `safety_fire_equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `safety_fire_equipment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `type` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型（灭火器/消防栓/喷淋/烟感/温感/消防主机/防火门/应急灯）',
  `location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '安装位置',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `expire_date` date DEFAULT NULL COMMENT '有效期',
  `status` tinyint DEFAULT '1' COMMENT '状态（0失效 1正常 2故障 3维修中）',
  `last_check_date` date DEFAULT NULL COMMENT '上次检查日期',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_area_id` (`area_id`),
  KEY `idx_status` (`status`),
  KEY `idx_expire_date` (`expire_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消防设备表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `safety_monitor_point`
--

DROP TABLE IF EXISTS `safety_monitor_point`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `safety_monitor_point` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '监测点位名称',
  `type` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型（摄像头/烟感/门禁/温感/水浸/红外/电子围栏）',
  `location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '安装位置',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `building_id` bigint DEFAULT NULL COMMENT '所属建筑ID',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1正常 2故障）',
  `ip_address` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT 'IP地址',
  `device_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '设备编码',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_area_id` (`area_id`),
  KEY `idx_building_id` (`building_id`),
  KEY `idx_status` (`status`),
  KEY `idx_device_code` (`device_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='安防监测点位表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `safety_network`
--

DROP TABLE IF EXISTS `safety_network`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `safety_network` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型（IP管理/威胁诱捕/设备监控/防私接）',
  `event_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '事件类型',
  `source_ip` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '源IP',
  `target_ip` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '目标IP',
  `port` int DEFAULT NULL COMMENT '端口',
  `protocol` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '协议（TCP/UDP/ICMP等）',
  `severity` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '低' COMMENT '严重程度（低/中/高/严重）',
  `description` text COLLATE utf8mb4_general_ci COMMENT '描述',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '待处理' COMMENT '处理状态（待处理/处理中/已处理/已忽略）',
  `handle_result` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '处理结果',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_source_ip` (`source_ip`),
  KEY `idx_target_ip` (`target_ip`),
  KEY `idx_severity` (`severity`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='网络安全表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `safety_patrol`
--

DROP TABLE IF EXISTS `safety_patrol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `safety_patrol` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `route_id` bigint DEFAULT NULL COMMENT '巡检路线ID',
  `patrol_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '巡检类型（日常巡检/安全巡检/消防巡检/设备巡检）',
  `patrol_person` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '巡检人',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '未开始' COMMENT '巡检状态（未开始/进行中/已完成/异常中断）',
  `result` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '巡检结果',
  `abnormal_count` int DEFAULT '0' COMMENT '异常数量',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_route_id` (`route_id`),
  KEY `idx_patrol_type` (`patrol_type`),
  KEY `idx_patrol_person` (`patrol_person`),
  KEY `idx_status` (`status`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='安全巡检表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `safety_patrol_route`
--

DROP TABLE IF EXISTS `safety_patrol_route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `safety_patrol_route` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '路线名称',
  `points_json` json DEFAULT NULL COMMENT '巡检点位列表（JSON格式）',
  `cycle` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '巡检周期（每日/每周/每月）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='巡检路线表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `safety_production`
--

DROP TABLE IF EXISTS `safety_production`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `safety_production` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型（操作违规/设备异常/环境异常/物料异常）',
  `enterprise_id` bigint DEFAULT NULL COMMENT '关联企业ID',
  `area_id` bigint DEFAULT NULL COMMENT '所属区域ID',
  `description` text COLLATE utf8mb4_general_ci COMMENT '描述',
  `severity` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '一般' COMMENT '严重程度（一般/较大/重大/特大）',
  `status` varchar(16) COLLATE utf8mb4_general_ci DEFAULT '待处理' COMMENT '处理状态（待处理/处理中/已处理/已关闭）',
  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_enterprise_id` (`enterprise_id`),
  KEY `idx_area_id` (`area_id`),
  KEY `idx_severity` (`severity`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生产安全表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名称',
  `config_key` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置键',
  `config_value` text COLLATE utf8mb4_general_ci COMMENT '配置值',
  `config_type` tinyint DEFAULT '1' COMMENT '配置类型（1系统内置 2自定义）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_dict`
--

DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dict_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `dict_type` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型（唯一标识）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0禁用 1启用）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_dict_item`
--

DROP TABLE IF EXISTS `sys_dict_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dict_id` bigint NOT NULL COMMENT '字典ID',
  `dict_type` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型',
  `item_label` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项标签',
  `item_value` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项值',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态（0禁用 1启用）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_dict_id` (`dict_id`),
  KEY `idx_dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1911 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `module` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作模块',
  `operation_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作类型',
  `operation` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作描述',
  `method` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '请求方法',
  `request_url` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '请求URL',
  `request_method` varchar(10) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '请求方式（GET/POST/PUT/DELETE）',
  `request_params` text COLLATE utf8mb4_general_ci COMMENT '请求参数',
  `response_result` text COLLATE utf8mb4_general_ci COMMENT '返回结果',
  `ip` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作IP',
  `user_agent` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '用户代理',
  `duration` bigint DEFAULT '0' COMMENT '耗时(ms)',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作人姓名',
  `operator_ip` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作人IP',
  `cost_time` bigint DEFAULT '0' COMMENT '耗时（毫秒）',
  `status` tinyint DEFAULT '1' COMMENT '操作状态（0失败 1成功）',
  `error_msg` text COLLATE utf8mb4_general_ci COMMENT '错误信息',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_module` (`module`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_login_log`
--

DROP TABLE IF EXISTS `sys_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录用户名',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `login_type` tinyint DEFAULT '1' COMMENT '登录方式（1账号密码 2手机验证码 3微信扫码）',
  `login_ip` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录IP',
  `login_location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录地点',
  `browser` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作系统',
  `status` tinyint DEFAULT '1' COMMENT '登录状态（0失败 1成功）',
  `message` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '提示消息',
  `ip` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录IP',
  `location` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '登录地点',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_username` (`username`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='登录日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID（0为顶级）',
  `menu_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `menu_type` tinyint NOT NULL COMMENT '菜单类型（1目录 2菜单 3按钮）',
  `path` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '路由路径',
  `component` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '组件路径',
  `perms` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '权限标识',
  `icon` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '菜单图标',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `visible` tinyint DEFAULT '0' COMMENT '是否可见（0可见 1隐藏）',
  `cache` tinyint DEFAULT '0' COMMENT '是否缓存（0否 1是）',
  `status` tinyint DEFAULT '0' COMMENT '状态（0正常 1禁用）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21084 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_organization`
--

DROP TABLE IF EXISTS `sys_organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_organization` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint DEFAULT '0' COMMENT '父组织ID（0为顶级）',
  `org_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '组织名称',
  `org_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '组织编码',
  `leader` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '负责人',
  `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '联系电话',
  `email` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '邮箱',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态（0禁用 1启用）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_org_code` (`org_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='组织架构表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_name` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_code` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
  `data_scope` tinyint DEFAULT '1' COMMENT '数据范围（1全部 2自定义 3本部门 4本部门及以下 5仅本人）',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '0' COMMENT '状态（0正常 1禁用）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`,`menu_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=512 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码（加密存储）',
  `nickname` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '昵称',
  `real_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '真实姓名',
  `avatar` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '头像URL',
  `email` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '邮箱',
  `phone` varchar(20) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '手机号',
  `gender` tinyint DEFAULT '0' COMMENT '性别（0未知 1男 2女）',
  `org_id` bigint DEFAULT NULL COMMENT '所属组织ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `status` tinyint DEFAULT '0' COMMENT '状态（0正常 1禁用）',
  `login_ip` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '最后登录IP',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_org_id` (`org_id`),
  KEY `idx_phone` (`phone`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_alert`
--

DROP TABLE IF EXISTS `twin_alert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_alert` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `alert_title` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '告警标题',
  `alert_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '告警类型（设备故障/能耗超标/碳排放超标/环境异常/安防告警）',
  `alert_level` tinyint DEFAULT '1' COMMENT '告警等级（1提示 2警告 3严重 4紧急）',
  `alert_source` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '告警来源',
  `source_id` bigint DEFAULT NULL COMMENT '来源ID',
  `alert_content` text COLLATE utf8mb4_general_ci COMMENT '告警内容',
  `alert_time` datetime DEFAULT NULL COMMENT '告警时间',
  `handle_status` tinyint DEFAULT '0' COMMENT '处理状态（0未处理 1处理中 2已处理 3已忽略）',
  `handle_user_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `handle_user_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '处理人姓名',
  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
  `handle_result` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '处理结果',
  `map_2d_location` json DEFAULT NULL COMMENT '2D地图定位（JSON格式，含x/y坐标）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_alert_type` (`alert_type`),
  KEY `idx_alert_level` (`alert_level`),
  KEY `idx_handle_status` (`handle_status`),
  KEY `idx_alert_time` (`alert_time`),
  KEY `idx_source` (`alert_source`,`source_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='告警记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_alert_rule`
--

DROP TABLE IF EXISTS `twin_alert_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_alert_rule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rule_name` varchar(128) NOT NULL COMMENT '规则名称',
  `field_name` varchar(64) NOT NULL COMMENT '监测字段',
  `field_label` varchar(64) DEFAULT '' COMMENT '字段显示名',
  `condition_type` varchar(16) NOT NULL COMMENT '条件类型: gt-大于, lt-小于, between-区间, eq-等于',
  `threshold_value` varchar(128) NOT NULL COMMENT '阈值(多个用逗号分隔)',
  `alert_level` varchar(16) DEFAULT 'warning' COMMENT '告警级别: warning-预警, danger-告警',
  `point_style_id` bigint DEFAULT NULL COMMENT '关联样式ID',
  `status` tinyint DEFAULT '1' COMMENT '状态',
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='告警阈值规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_api_config`
--

DROP TABLE IF EXISTS `twin_api_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_api_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `api_name` varchar(128) NOT NULL COMMENT '接口名称',
  `api_type` varchar(32) NOT NULL COMMENT '接口类型: device_list-设备列表, realtime-实时数据, alert-告警, video-视频',
  `api_url` varchar(512) NOT NULL COMMENT '接口地址',
  `method` varchar(16) DEFAULT 'GET' COMMENT '请求方式: GET/POST',
  `headers` text COMMENT '请求头JSON',
  `params` text COMMENT '请求参数JSON',
  `refresh_interval` int DEFAULT '10' COMMENT '刷新频率(秒)',
  `field_mapping` text COMMENT '字段映射JSON',
  `timeout` int DEFAULT '5000' COMMENT '超时时间(ms)',
  `retry_count` int DEFAULT '3' COMMENT '重连次数',
  `error_msg` varchar(256) DEFAULT '数据获取异常' COMMENT '异常提示文案',
  `status` tinyint DEFAULT '1' COMMENT '状态: 1-启用 0-禁用',
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_api_type` (`api_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_base_map`
--

DROP TABLE IF EXISTS `twin_base_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_base_map` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `map_name` varchar(128) NOT NULL COMMENT '底图名称',
  `map_type` varchar(32) NOT NULL COMMENT '底图类型: campus-园区, building-楼栋, floor-楼层',
  `bind_id` bigint DEFAULT NULL COMMENT '绑定ID: 楼栋ID或楼层标识',
  `file_url` varchar(512) NOT NULL COMMENT '底图文件URL',
  `file_size` bigint DEFAULT '0' COMMENT '文件大小(bytes)',
  `width` int DEFAULT '0' COMMENT '图片宽度(px)',
  `height` int DEFAULT '0' COMMENT '图片高度(px)',
  `show_grid` tinyint DEFAULT '1' COMMENT '是否显示网格: 1-是 0-否',
  `status` tinyint DEFAULT '1' COMMENT '状态: 1-启用 0-禁用',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `remark` varchar(512) DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_map_type` (`map_type`),
  KEY `idx_bind_id` (`bind_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='底图管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_building_poi`
--

DROP TABLE IF EXISTS `twin_building_poi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_building_poi` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `building_id` bigint NOT NULL COMMENT '建筑ID',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'POI名称',
  `x` decimal(12,4) DEFAULT '0.0000' COMMENT 'X坐标',
  `y` decimal(12,4) DEFAULT '0.0000' COMMENT 'Y坐标',
  `width` decimal(12,4) DEFAULT '0.0000' COMMENT '宽度',
  `height` decimal(12,4) DEFAULT '0.0000' COMMENT '高度',
  `icon` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '图标',
  `popup_config_json` json DEFAULT NULL COMMENT '弹窗配置（JSON格式）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_building_id` (`building_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='楼栋POI表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_config_log`
--

DROP TABLE IF EXISTS `twin_config_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_config_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `module` varchar(64) NOT NULL COMMENT '操作模块: basemap-底图, point-点位, style-样式, api-接口, config-配置',
  `action_type` varchar(32) NOT NULL COMMENT '操作类型: create/update/delete/publish/rollback/upload',
  `action_detail` text COMMENT '操作详情JSON',
  `target_id` bigint DEFAULT NULL COMMENT '操作对象ID',
  `target_name` varchar(128) DEFAULT '' COMMENT '操作对象名称',
  `operator` varchar(64) DEFAULT '' COMMENT '操作人',
  `operator_ip` varchar(64) DEFAULT '' COMMENT '操作IP',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_module` (`module`),
  KEY `idx_operator` (`operator`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='可视化配置操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_config_version`
--

DROP TABLE IF EXISTS `twin_config_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_config_version` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `version_no` varchar(32) NOT NULL COMMENT '版本号',
  `config_snapshot` text NOT NULL COMMENT '配置快照JSON',
  `publish_status` tinyint DEFAULT '0' COMMENT '发布状态: 0-草稿 1-已发布 2-已回滚',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `publish_by` varchar(64) DEFAULT '' COMMENT '发布人',
  `rollback_time` datetime DEFAULT NULL COMMENT '回滚时间',
  `rollback_by` varchar(64) DEFAULT '' COMMENT '回滚人',
  `change_log` text COMMENT '变更说明',
  `check_result` text COMMENT '发布校验结果JSON',
  `status` tinyint DEFAULT '1' COMMENT '状态',
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_version_no` (`version_no`),
  KEY `idx_publish_status` (`publish_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='配置版本管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_device_poi`
--

DROP TABLE IF EXISTS `twin_device_poi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_device_poi` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `device_id` bigint NOT NULL COMMENT '设备ID',
  `device_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '设备类型',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT 'POI名称',
  `x` decimal(12,4) DEFAULT '0.0000' COMMENT 'X坐标',
  `y` decimal(12,4) DEFAULT '0.0000' COMMENT 'Y坐标',
  `icon` varchar(256) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '图标',
  `status` tinyint DEFAULT '1' COMMENT '状态（0停用 1正常 2故障）',
  `popup_config_json` json DEFAULT NULL COMMENT '弹窗配置（JSON格式）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_device_type` (`device_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='设备POI表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_icon_library`
--

DROP TABLE IF EXISTS `twin_icon_library`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_icon_library` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `icon_name` varchar(64) NOT NULL COMMENT '图标名称',
  `icon_type` varchar(64) NOT NULL COMMENT '图标类型: building-楼栋, camera-摄像头, sensor-传感器, fire-消防, access-门禁, power-配电, custom-自定义',
  `file_url` varchar(512) NOT NULL COMMENT '图标文件URL',
  `file_size` bigint DEFAULT '0' COMMENT '文件大小',
  `width` int DEFAULT '32' COMMENT '默认宽度',
  `height` int DEFAULT '32' COMMENT '默认高度',
  `is_builtin` tinyint DEFAULT '0' COMMENT '是否内置: 1-是 0-否',
  `status` tinyint DEFAULT '1' COMMENT '状态',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_icon_type` (`icon_type`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='图标库管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_indicator`
--

DROP TABLE IF EXISTS `twin_indicator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_indicator` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `indicator_name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '指标名称',
  `indicator_code` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '指标编码',
  `indicator_type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '指标类型（能源/碳排放/环境/经济/安全/设备）',
  `unit` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '单位',
  `target_value` decimal(16,4) DEFAULT NULL COMMENT '目标值',
  `current_value` decimal(16,4) DEFAULT NULL COMMENT '当前值',
  `warning_value` decimal(16,4) DEFAULT NULL COMMENT '预警值',
  `danger_value` decimal(16,4) DEFAULT NULL COMMENT '危险值',
  `refresh_cycle` int DEFAULT '60' COMMENT '刷新周期（秒）',
  `display_order` int DEFAULT '0' COMMENT '展示排序',
  `visible` tinyint DEFAULT '1' COMMENT '是否展示（0隐藏 1展示）',
  `chart_type` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '图表类型（line/bar/pie/gauge/number）',
  `map_2d_bind` json DEFAULT NULL COMMENT '2D地图绑定配置（JSON格式）',
  `status` tinyint DEFAULT '1' COMMENT '状态（0禁用 1启用）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_indicator_code` (`indicator_code`),
  KEY `idx_indicator_type` (`indicator_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='核心指标配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_layer_config`
--

DROP TABLE IF EXISTS `twin_layer_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_layer_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '图层名称',
  `type` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '图层类型（建筑/设备/安防/环境/管网/标注）',
  `visible` tinyint DEFAULT '1' COMMENT '是否可见（0隐藏 1显示）',
  `sort` int DEFAULT '0' COMMENT '排序',
  `config_json` json DEFAULT NULL COMMENT '图层配置（JSON格式）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_visible` (`visible`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='图层配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_point_config`
--

DROP TABLE IF EXISTS `twin_point_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_point_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `point_name` varchar(128) NOT NULL COMMENT '点位名称',
  `point_code` varchar(64) DEFAULT '' COMMENT '点位编码',
  `map_id` bigint NOT NULL COMMENT '所属底图ID',
  `icon_id` bigint DEFAULT NULL COMMENT '图标ID',
  `pos_x` decimal(10,2) NOT NULL COMMENT 'X坐标(百分比)',
  `pos_y` decimal(10,2) NOT NULL COMMENT 'Y坐标(百分比)',
  `pos_rotation` int DEFAULT '0' COMMENT '旋转角度',
  `pos_z_index` int DEFAULT '0' COMMENT '层级',
  `point_group` varchar(128) DEFAULT '' COMMENT '分组',
  `device_id` bigint DEFAULT NULL COMMENT '绑定设备ID',
  `device_code` varchar(64) DEFAULT '' COMMENT '绑定设备编号',
  `device_name` varchar(128) DEFAULT '' COMMENT '绑定设备名称',
  `bind_type` varchar(32) DEFAULT '' COMMENT '绑定类型: device-设备, camera-摄像头, alert-告警',
  `click_action` varchar(32) DEFAULT 'popup' COMMENT '单击动作: popup-弹窗, detail-详情, none-无',
  `dbl_click_action` varchar(32) DEFAULT 'detail' COMMENT '双击动作',
  `show_label` tinyint DEFAULT '1' COMMENT '是否显示标签',
  `label_content` varchar(32) DEFAULT 'name' COMMENT '标签内容: name-名称, status-状态, value-数值',
  `status` tinyint DEFAULT '1' COMMENT '点位状态: 1-正常 0-禁用',
  `create_by` varchar(64) DEFAULT '',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(64) DEFAULT '',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_map_id` (`map_id`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_point_group` (`point_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='点位配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_point_style`
--

DROP TABLE IF EXISTS `twin_point_style`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_point_style` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `style_name` varchar(64) NOT NULL COMMENT '样式名称',
  `icon_type` varchar(64) DEFAULT '' COMMENT '适用图标类型',
  `size` int DEFAULT '32' COMMENT '图标大小',
  `color` varchar(32) DEFAULT '#00E5FF' COMMENT '默认颜色',
  `opacity` decimal(3,2) DEFAULT '1.00' COMMENT '透明度',
  `glow_color` varchar(32) DEFAULT '#00E5FF' COMMENT '发光颜色',
  `glow_intensity` int DEFAULT '50' COMMENT '发光强度(0-100)',
  `breath_speed` int DEFAULT '3' COMMENT '呼吸速度(秒)',
  `warn_color` varchar(32) DEFAULT '#FFA500' COMMENT '预警颜色',
  `warn_flash_freq` int DEFAULT '2' COMMENT '预警闪烁频率(次/秒)',
  `alarm_color` varchar(32) DEFAULT '#FF3333' COMMENT '告警颜色',
  `alarm_flash_freq` int DEFAULT '4' COMMENT '告警闪烁频率',
  `offline_color` varchar(32) DEFAULT '#666666' COMMENT '离线颜色',
  `offline_opacity` decimal(3,2) DEFAULT '0.40' COMMENT '离线透明度',
  `hover_scale` decimal(3,2) DEFAULT '1.30' COMMENT '悬浮放大倍数',
  `hover_highlight` tinyint DEFAULT '1' COMMENT '悬浮高亮',
  `hover_tooltip` tinyint DEFAULT '1' COMMENT '悬浮提示',
  `is_default` tinyint DEFAULT '0' COMMENT '是否默认样式',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_icon_type` (`icon_type`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='点位样式配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `twin_scene_config`
--

DROP TABLE IF EXISTS `twin_scene_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `twin_scene_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '场景名称',
  `background` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '背景图URL',
  `zoom` decimal(6,2) DEFAULT '1.00' COMMENT '缩放比例',
  `center_x` decimal(12,4) DEFAULT '0.0000' COMMENT '中心X坐标',
  `center_y` decimal(12,4) DEFAULT '0.0000' COMMENT '中心Y坐标',
  `width` decimal(12,4) DEFAULT '0.0000' COMMENT '场景宽度',
  `height` decimal(12,4) DEFAULT '0.0000' COMMENT '场景高度',
  `layers_json` json DEFAULT NULL COMMENT '图层列表（JSON格式）',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='场景配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_order`
--

DROP TABLE IF EXISTS `work_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(64) COLLATE utf8mb4_general_ci NOT NULL COMMENT '工单编号',
  `order_title` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '工单标题',
  `order_type` tinyint NOT NULL COMMENT '工单类型（1故障报修 2巡检工单 3维保工单 4安装工单 5改造工单）',
  `priority` tinyint DEFAULT '2' COMMENT '优先级（1低 2中 3高 4紧急）',
  `device_id` bigint DEFAULT NULL COMMENT '关联设备ID',
  `device_code` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '关联设备编码',
  `device_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '关联设备名称',
  `fault_desc` text COLLATE utf8mb4_general_ci COMMENT '故障描述',
  `fault_images` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '故障图片（多张以逗号分隔）',
  `reporter_id` bigint DEFAULT NULL COMMENT '报修人ID',
  `reporter_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '报修人姓名',
  `report_time` datetime DEFAULT NULL COMMENT '报修时间',
  `assignee_id` bigint DEFAULT NULL COMMENT '处理人ID',
  `assignee_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '处理人姓名',
  `assign_time` datetime DEFAULT NULL COMMENT '派单时间',
  `accept_time` datetime DEFAULT NULL COMMENT '接单时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `handle_result` text COLLATE utf8mb4_general_ci COMMENT '处理结果',
  `handle_images` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '处理图片',
  `evaluate_score` tinyint DEFAULT NULL COMMENT '评价评分（1-5分）',
  `evaluate_content` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '评价内容',
  `order_status` tinyint DEFAULT '0' COMMENT '工单状态（0待派单 1待接单 2处理中 3待验收 4已完成 5已关闭 6已驳回）',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_order_type` (`order_type`),
  KEY `idx_priority` (`priority`),
  KEY `idx_device_id` (`device_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_assignee_id` (`assignee_id`),
  KEY `idx_report_time` (`report_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `work_order_log`
--

DROP TABLE IF EXISTS `work_order_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `work_order_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '工单ID',
  `order_no` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '工单编号',
  `operation` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型（创建/派单/接单/处理/完成/关闭/驳回/评价）',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '操作人姓名',
  `content` text COLLATE utf8mb4_general_ci COMMENT '操作内容',
  `images` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '附件图片',
  `remark` varchar(512) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint DEFAULT '0' COMMENT '逻辑删除（0未删除 1已删除）',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='工单操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;



-- ============================================================
-- 第二部分：初始数据
-- ============================================================

-- -----------------------------------------------------------
-- 1. 管理员用户
--    username: admin
--    password: BCrypt加密的 admin123
--    nickname: 超级管理员
-- -----------------------------------------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `real_name`, `avatar`, `email`, `phone`, `gender`, `org_id`, `dept_id`, `status`, `login_ip`, `login_time`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', '超级管理员', '', 'admin@digitalpark.com', '13800000000', 1, 1, NULL, 0, '', NULL, '超级管理员', 'admin', NOW(), 'admin', NOW(), 0);

-- -----------------------------------------------------------
-- 2. 默认角色
-- -----------------------------------------------------------
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `data_scope`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1, '超级管理员', 'admin', 1, 1, 0, '超级管理员，拥有所有权限', 'admin', NOW(), 'admin', NOW(), 0);

-- -----------------------------------------------------------
-- 3. 用户角色关联
-- -----------------------------------------------------------
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1, 1, 1, '管理员关联超级管理员角色', 'admin', NOW(), 'admin', NOW(), 0);

-- -----------------------------------------------------------
-- 4. 默认组织
-- -----------------------------------------------------------
INSERT INTO `sys_organization` (`id`, `parent_id`, `org_name`, `org_code`, `leader`, `phone`, `email`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1, 0, '银河数字园区', 'YH_DIGITAL_PARK', '超级管理员', '13800000000', 'admin@digitalpark.com', 1, 1, '园区总部组织', 'admin', NOW(), 'admin', NOW(), 0);

-- -----------------------------------------------------------
-- 5. 系统配置
-- -----------------------------------------------------------
INSERT INTO `sys_config` (`id`, `config_name`, `config_key`, `config_value`, `config_type`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES
(1, '主框架页-默认皮肤样式', 'sys.index.skinName', 'skin-blue', 1, '默认皮肤样式', 'admin', NOW(), 'admin', NOW(), 0),
(2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 1, '初始化密码', 'admin', NOW(), 'admin', NOW(), 0),
(3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 1, '侧边栏主题', 'admin', NOW(), 'admin', NOW(), 0);

-- -----------------------------------------------------------
-- 6. 菜单数据（完整菜单树）
-- -----------------------------------------------------------

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1000,0,'安全管理',1,'safety','','','WarningFilled',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-23 01:17:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1001,1000,'安防监控',2,'monitor-point','safety/MonitorPoint','safety:monitor-point:list','VideoCamera',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1002,1000,'安全事件',2,'event','safety/SafetyEvent','safety:event:list','Bell',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-23 01:18:41',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1003,1000,'巡检管理',2,'patrol','safety/PatrolManage','safety:patrol:list','Flag',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1004,1000,'消防设备',2,'fire-equipment','safety/FireEquipment','safety:fire-equipment:list','FirstAidKit',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1005,1000,'网络安全',2,'network','safety/NetworkSecurity','safety:network:list','Lock',5,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1006,1000,'生产安全',2,'production','safety/ProductionSafety','safety:production:list','WarnTriangleFilled',6,0,0,0,'','','2026-04-22 22:47:29','','2026-04-23 01:17:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1100,0,'应急管理',1,'emergency','','','Warning',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1101,1100,'应急物资',2,'material','emergency/EmergencyMaterial','emergency:material:list','Box',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1102,1100,'应急队伍',2,'team','emergency/EmergencyTeam','emergency:team:list','Avatar',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1103,1100,'应急预案',2,'plan','emergency/EmergencyPlan','emergency:plan:list','Notebook',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1104,1100,'应急演练',2,'drill','emergency/EmergencyDrill','emergency:drill:list','Aim',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1105,1100,'应急事件',2,'event','emergency/EmergencyEvent','emergency:event:list','PhoneFilled',5,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1200,0,'能源管理',1,'energy','','','Sunny',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1201,1200,'能源计量',2,'meter','energy/EnergyMeter','energy:meter:list','Odometer',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1202,1200,'能源数据',2,'data','energy/EnergyData','energy:data:list','DataLine',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1203,1200,'源侧设备',2,'source','energy/EnergySource','energy:source:list','Sunrise',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1204,1200,'储能管理',2,'storage','energy/EnergyStorage','energy:storage:list','SwitchButton',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-23 01:17:28',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1205,1200,'充电桩',2,'charging','energy/EnergyCharging','energy:charging:list','Lightning',5,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1300,0,'资产管理',1,'asset','','','Coin',5,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1301,1300,'资产分类',2,'category','asset/AssetCategory','asset:category:list','Files',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1302,1300,'资产台账',2,'info','asset/AssetInfo','asset:info:list','List',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1303,1300,'资产文档',2,'document','asset/AssetDocument','asset:document:list','Document',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1304,1300,'维保管理',2,'maintenance','asset/AssetMaintenance','asset:maintenance:list','Tools',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1305,1300,'资产盘点',2,'inventory','asset/AssetInventory','asset:inventory:list','Tickets',5,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1400,0,'环境管理',1,'environment','','','Cloudy',6,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1401,1400,'监测点位',2,'point','environment/MonitorPoint','environment:point:list','MapLocation',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1402,1400,'环境数据',2,'data','environment/MonitorData','environment:data:list','TrendCharts',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1403,1400,'污染溯源',2,'pollution','environment/PollutionSource','environment:pollution:list','Smoking',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1500,0,'经济运行',1,'economy','','','DataAnalysis',7,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1501,1500,'企业画像',2,'profile','economy/EnterpriseProfile','economy:profile:list','UserFilled',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1502,1500,'经济指标',2,'indicator','economy/EconomyIndicator','economy:indicator:list','Histogram',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1503,1500,'经济报告',2,'report','economy/EconomyReport','economy:report:list','Memo',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1600,0,'企业服务',1,'enterprise','','','OfficeBuilding',8,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1601,1600,'企业信息',2,'info','enterprise/EnterpriseInfo','enterprise:info:list','OfficeBuilding',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1602,1600,'服务请求',2,'service','enterprise/ServiceRequest','enterprise:service:list','Service',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1603,1600,'政策信息',2,'policy','enterprise/PolicyInfo','enterprise:policy:list','Reading',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1700,0,'物业管理',1,'property','','','House',9,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1701,1700,'费用管理',2,'fee','property/FeeManage','property:fee:list','Money',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1702,1700,'工单管理',2,'work-order','property/WorkOrder','property:work-order:list','Tickets',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1703,1700,'访客管理',2,'visitor','property/VisitorManage','property:visitor:list','User',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1704,1700,'车辆管理',2,'vehicle','property/VehicleManage','property:vehicle:list','Van',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1705,1700,'设施运维',2,'facility','property/FacilityManage','property:facility:list','SetUp',5,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1800,0,'招商管理',1,'investment','','','TrendCharts',10,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1801,1800,'房源管理',2,'asset','investment/AssetManage','investment:asset:list','House',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1802,1800,'客户管理',2,'customer','investment/CustomerManage','investment:customer:list','UserFilled',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1803,1800,'商机管理',2,'opportunity','investment/OpportunityManage','investment:opportunity:list','Opportunity',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1804,1800,'合同管理',2,'contract','investment/ContractManage','investment:contract:list','Document',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1805,1800,'招商政策',2,'policy','investment/PolicyManage','investment:policy:list','Promotion',5,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1900,0,'数字孪生',1,'twin','','','Monitor',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1901,1900,'园区总览',2,'overview','twin/overview','twin:overview:list','View',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-23 07:39:38',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1902,1900,'告警中心',2,'alert','twin/alert','twin:alert:list','Bell',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-23 07:39:38',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (1903,1900,'可视化配置',2,'config','twin/config','twin:config:list','SetUp',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-23 07:39:38',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (2000,0,'AI智能报告',1,'ai-report','','','Cpu',11,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (2001,2000,'报告管理',2,'report','aiReport/index','ai-report:list','Cpu',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-23 07:39:38',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (2100,0,'系统管理',1,'system','','','Setting',12,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (2101,2100,'用户管理',2,'user','system/User','system:user:list','UserFilled',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (2102,2100,'角色管理',2,'role','system/Role','system:role:list','Stamp',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (2103,2100,'菜单管理',2,'menu','system/Menu','system:menu:list','Menu',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (2104,2100,'组织管理',2,'org','system/Org','system:org:list','Connection',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (2105,2100,'字典管理',2,'dict','system/Dict','system:dict:list','Collection',5,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (2106,2100,'系统配置',2,'config','system/Config','system:config:list','Operation',6,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (2107,2100,'操作日志',2,'log','system/Log','system:log:list','Notebook',7,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (2108,2100,'登录日志',2,'login-log','system/LoginLog','system:login-log:list','Clock',8,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10011,1001,'安防监控查询',3,'','','safety:monitor-point:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10012,1001,'安防监控新增',3,'','','safety:monitor-point:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10013,1001,'安防监控修改',3,'','','safety:monitor-point:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10014,1001,'安防监控删除',3,'','','safety:monitor-point:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10021,1002,'安全事件查询',3,'','','safety:event:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10022,1002,'安全事件新增',3,'','','safety:event:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10023,1002,'安全事件修改',3,'','','safety:event:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10024,1002,'安全事件删除',3,'','','safety:event:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10031,1003,'巡检管理查询',3,'','','safety:patrol:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10032,1003,'巡检管理新增',3,'','','safety:patrol:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10033,1003,'巡检管理修改',3,'','','safety:patrol:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10034,1003,'巡检管理删除',3,'','','safety:patrol:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10041,1004,'消防设备查询',3,'','','safety:fire-equipment:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10042,1004,'消防设备新增',3,'','','safety:fire-equipment:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10043,1004,'消防设备修改',3,'','','safety:fire-equipment:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10044,1004,'消防设备删除',3,'','','safety:fire-equipment:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10051,1005,'网络安全查询',3,'','','safety:network:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10052,1005,'网络安全新增',3,'','','safety:network:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10053,1005,'网络安全修改',3,'','','safety:network:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10054,1005,'网络安全删除',3,'','','safety:network:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10061,1006,'生产安全查询',3,'','','safety:production:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10062,1006,'生产安全新增',3,'','','safety:production:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10063,1006,'生产安全修改',3,'','','safety:production:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (10064,1006,'生产安全删除',3,'','','safety:production:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11011,1101,'应急物资查询',3,'','','emergency:material:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11012,1101,'应急物资新增',3,'','','emergency:material:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11013,1101,'应急物资修改',3,'','','emergency:material:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11014,1101,'应急物资删除',3,'','','emergency:material:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11021,1102,'应急队伍查询',3,'','','emergency:team:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11022,1102,'应急队伍新增',3,'','','emergency:team:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11023,1102,'应急队伍修改',3,'','','emergency:team:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11024,1102,'应急队伍删除',3,'','','emergency:team:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11031,1103,'应急预案查询',3,'','','emergency:plan:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11032,1103,'应急预案新增',3,'','','emergency:plan:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11033,1103,'应急预案修改',3,'','','emergency:plan:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11034,1103,'应急预案删除',3,'','','emergency:plan:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11041,1104,'应急演练查询',3,'','','emergency:drill:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11042,1104,'应急演练新增',3,'','','emergency:drill:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11043,1104,'应急演练修改',3,'','','emergency:drill:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11044,1104,'应急演练删除',3,'','','emergency:drill:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11051,1105,'应急事件查询',3,'','','emergency:event:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11052,1105,'应急事件新增',3,'','','emergency:event:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11053,1105,'应急事件修改',3,'','','emergency:event:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (11054,1105,'应急事件删除',3,'','','emergency:event:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12011,1201,'能源计量查询',3,'','','energy:meter:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12012,1201,'能源计量新增',3,'','','energy:meter:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12013,1201,'能源计量修改',3,'','','energy:meter:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12014,1201,'能源计量删除',3,'','','energy:meter:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12021,1202,'能源数据查询',3,'','','energy:data:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12022,1202,'能源数据新增',3,'','','energy:data:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12023,1202,'能源数据修改',3,'','','energy:data:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12024,1202,'能源数据删除',3,'','','energy:data:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12031,1203,'源侧设备查询',3,'','','energy:source:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12032,1203,'源侧设备新增',3,'','','energy:source:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12033,1203,'源侧设备修改',3,'','','energy:source:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12034,1203,'源侧设备删除',3,'','','energy:source:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12041,1204,'储能管理查询',3,'','','energy:storage:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12042,1204,'储能管理新增',3,'','','energy:storage:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12043,1204,'储能管理修改',3,'','','energy:storage:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12044,1204,'储能管理删除',3,'','','energy:storage:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12051,1205,'充电桩查询',3,'','','energy:charging:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12052,1205,'充电桩新增',3,'','','energy:charging:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12053,1205,'充电桩修改',3,'','','energy:charging:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (12054,1205,'充电桩删除',3,'','','energy:charging:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13011,1301,'资产分类查询',3,'','','asset:category:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13012,1301,'资产分类新增',3,'','','asset:category:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13013,1301,'资产分类修改',3,'','','asset:category:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13014,1301,'资产分类删除',3,'','','asset:category:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13021,1302,'资产台账查询',3,'','','asset:info:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13022,1302,'资产台账新增',3,'','','asset:info:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13023,1302,'资产台账修改',3,'','','asset:info:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13024,1302,'资产台账删除',3,'','','asset:info:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13031,1303,'资产文档查询',3,'','','asset:document:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13032,1303,'资产文档新增',3,'','','asset:document:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13033,1303,'资产文档修改',3,'','','asset:document:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13034,1303,'资产文档删除',3,'','','asset:document:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13041,1304,'维保管理查询',3,'','','asset:maintenance:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13042,1304,'维保管理新增',3,'','','asset:maintenance:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13043,1304,'维保管理修改',3,'','','asset:maintenance:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13044,1304,'维保管理删除',3,'','','asset:maintenance:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13051,1305,'资产盘点查询',3,'','','asset:inventory:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13052,1305,'资产盘点新增',3,'','','asset:inventory:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13053,1305,'资产盘点修改',3,'','','asset:inventory:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (13054,1305,'资产盘点删除',3,'','','asset:inventory:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (14011,1401,'监测点位查询',3,'','','environment:point:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (14012,1401,'监测点位新增',3,'','','environment:point:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (14013,1401,'监测点位修改',3,'','','environment:point:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (14014,1401,'监测点位删除',3,'','','environment:point:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (14021,1402,'环境数据查询',3,'','','environment:data:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (14022,1402,'环境数据新增',3,'','','environment:data:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (14023,1402,'环境数据修改',3,'','','environment:data:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (14024,1402,'环境数据删除',3,'','','environment:data:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (14031,1403,'污染溯源查询',3,'','','environment:pollution:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (14032,1403,'污染溯源新增',3,'','','environment:pollution:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (14033,1403,'污染溯源修改',3,'','','environment:pollution:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (14034,1403,'污染溯源删除',3,'','','environment:pollution:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (15011,1501,'企业画像查询',3,'','','economy:profile:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (15012,1501,'企业画像新增',3,'','','economy:profile:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (15013,1501,'企业画像修改',3,'','','economy:profile:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (15014,1501,'企业画像删除',3,'','','economy:profile:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (15021,1502,'经济指标查询',3,'','','economy:indicator:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (15022,1502,'经济指标新增',3,'','','economy:indicator:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (15023,1502,'经济指标修改',3,'','','economy:indicator:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (15024,1502,'经济指标删除',3,'','','economy:indicator:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (15031,1503,'经济报告查询',3,'','','economy:report:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (15032,1503,'经济报告新增',3,'','','economy:report:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (15033,1503,'经济报告修改',3,'','','economy:report:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (15034,1503,'经济报告删除',3,'','','economy:report:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (16011,1601,'企业信息查询',3,'','','enterprise:info:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (16012,1601,'企业信息新增',3,'','','enterprise:info:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (16013,1601,'企业信息修改',3,'','','enterprise:info:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (16014,1601,'企业信息删除',3,'','','enterprise:info:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (16021,1602,'服务请求查询',3,'','','enterprise:service:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (16022,1602,'服务请求新增',3,'','','enterprise:service:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (16023,1602,'服务请求修改',3,'','','enterprise:service:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (16024,1602,'服务请求删除',3,'','','enterprise:service:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (16031,1603,'政策信息查询',3,'','','enterprise:policy:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (16032,1603,'政策信息新增',3,'','','enterprise:policy:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (16033,1603,'政策信息修改',3,'','','enterprise:policy:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (16034,1603,'政策信息删除',3,'','','enterprise:policy:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17011,1701,'费用管理查询',3,'','','property:fee:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17012,1701,'费用管理新增',3,'','','property:fee:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17013,1701,'费用管理修改',3,'','','property:fee:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17014,1701,'费用管理删除',3,'','','property:fee:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17021,1702,'工单管理查询',3,'','','property:work-order:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17022,1702,'工单管理新增',3,'','','property:work-order:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17023,1702,'工单管理修改',3,'','','property:work-order:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17024,1702,'工单管理删除',3,'','','property:work-order:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17031,1703,'访客管理查询',3,'','','property:visitor:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17032,1703,'访客管理新增',3,'','','property:visitor:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17033,1703,'访客管理修改',3,'','','property:visitor:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17034,1703,'访客管理删除',3,'','','property:visitor:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17041,1704,'车辆管理查询',3,'','','property:vehicle:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17042,1704,'车辆管理新增',3,'','','property:vehicle:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17043,1704,'车辆管理修改',3,'','','property:vehicle:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17044,1704,'车辆管理删除',3,'','','property:vehicle:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17051,1705,'设施运维查询',3,'','','property:facility:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17052,1705,'设施运维新增',3,'','','property:facility:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17053,1705,'设施运维修改',3,'','','property:facility:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (17054,1705,'设施运维删除',3,'','','property:facility:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18011,1801,'房源管理查询',3,'','','investment:asset:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18012,1801,'房源管理新增',3,'','','investment:asset:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18013,1801,'房源管理修改',3,'','','investment:asset:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18014,1801,'房源管理删除',3,'','','investment:asset:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18021,1802,'客户管理查询',3,'','','investment:customer:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18022,1802,'客户管理新增',3,'','','investment:customer:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18023,1802,'客户管理修改',3,'','','investment:customer:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18024,1802,'客户管理删除',3,'','','investment:customer:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18031,1803,'商机管理查询',3,'','','investment:opportunity:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18032,1803,'商机管理新增',3,'','','investment:opportunity:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18033,1803,'商机管理修改',3,'','','investment:opportunity:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18034,1803,'商机管理删除',3,'','','investment:opportunity:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18041,1804,'合同管理查询',3,'','','investment:contract:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18042,1804,'合同管理新增',3,'','','investment:contract:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18043,1804,'合同管理修改',3,'','','investment:contract:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18044,1804,'合同管理删除',3,'','','investment:contract:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18051,1805,'招商政策查询',3,'','','investment:policy:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18052,1805,'招商政策新增',3,'','','investment:policy:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18053,1805,'招商政策修改',3,'','','investment:policy:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (18054,1805,'招商政策删除',3,'','','investment:policy:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (19011,1901,'园区总览查询',3,'','','twin:overview:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (19012,1901,'园区总览新增',3,'','','twin:overview:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (19013,1901,'园区总览修改',3,'','','twin:overview:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (19014,1901,'园区总览删除',3,'','','twin:overview:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (19021,1902,'告警中心查询',3,'','','twin:alert:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (19022,1902,'告警中心新增',3,'','','twin:alert:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (19023,1902,'告警中心修改',3,'','','twin:alert:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (19024,1902,'告警中心删除',3,'','','twin:alert:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (19031,1903,'可视化配置查询',3,'','','twin:config:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (19032,1903,'可视化配置新增',3,'','','twin:config:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (19033,1903,'可视化配置修改',3,'','','twin:config:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (19034,1903,'可视化配置删除',3,'','','twin:config:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (20011,2001,'报告管理查询',3,'','','ai-report:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (20012,2001,'报告管理新增',3,'','','ai-report:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (20013,2001,'报告管理修改',3,'','','ai-report:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (20014,2001,'报告管理删除',3,'','','ai-report:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21011,2101,'用户管理查询',3,'','','system:user:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21012,2101,'用户管理新增',3,'','','system:user:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21013,2101,'用户管理修改',3,'','','system:user:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21014,2101,'用户管理删除',3,'','','system:user:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21015,2101,'重置密码',3,'','','system:user:resetPwd','',5,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21021,2102,'角色管理查询',3,'','','system:role:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21022,2102,'角色管理新增',3,'','','system:role:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21023,2102,'角色管理修改',3,'','','system:role:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21024,2102,'角色管理删除',3,'','','system:role:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21031,2103,'菜单管理查询',3,'','','system:menu:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21032,2103,'菜单管理新增',3,'','','system:menu:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21033,2103,'菜单管理修改',3,'','','system:menu:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21034,2103,'菜单管理删除',3,'','','system:menu:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21041,2104,'组织管理查询',3,'','','system:org:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21042,2104,'组织管理新增',3,'','','system:org:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21043,2104,'组织管理修改',3,'','','system:org:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21044,2104,'组织管理删除',3,'','','system:org:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21051,2105,'字典管理查询',3,'','','system:dict:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21052,2105,'字典管理新增',3,'','','system:dict:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21053,2105,'字典管理修改',3,'','','system:dict:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21054,2105,'字典管理删除',3,'','','system:dict:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21061,2106,'系统配置查询',3,'','','system:config:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21062,2106,'系统配置新增',3,'','','system:config:add','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21063,2106,'系统配置修改',3,'','','system:config:edit','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21064,2106,'系统配置删除',3,'','','system:config:remove','',4,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21071,2107,'操作日志查询',3,'','','system:log:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21072,2107,'操作日志删除',3,'','','system:log:remove','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21073,2107,'操作日志导出',3,'','','system:log:export','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21081,2108,'登录日志查询',3,'','','system:login-log:list','',1,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21082,2108,'登录日志删除',3,'','','system:login-log:remove','',2,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `sort_order`, `visible`, `cache`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES (21083,2108,'登录日志导出',3,'','','system:login-log:export','',3,0,0,0,'','','2026-04-22 22:47:29','','2026-04-22 22:47:29',0);

-- -----------------------------------------------------------
-- 7. 角色菜单关联 (admin -> 所有菜单)
--    共 271 条记录
-- -----------------------------------------------------------

INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES
(1, 1, 1000, '超级管理员角色关联菜单1000', 'admin', NOW(), 'admin', NOW(), 0),
(2, 1, 1001, '超级管理员角色关联菜单1001', 'admin', NOW(), 'admin', NOW(), 0),
(3, 1, 1002, '超级管理员角色关联菜单1002', 'admin', NOW(), 'admin', NOW(), 0),
(4, 1, 1003, '超级管理员角色关联菜单1003', 'admin', NOW(), 'admin', NOW(), 0),
(5, 1, 1004, '超级管理员角色关联菜单1004', 'admin', NOW(), 'admin', NOW(), 0),
(6, 1, 1005, '超级管理员角色关联菜单1005', 'admin', NOW(), 'admin', NOW(), 0),
(7, 1, 1006, '超级管理员角色关联菜单1006', 'admin', NOW(), 'admin', NOW(), 0),
(8, 1, 1100, '超级管理员角色关联菜单1100', 'admin', NOW(), 'admin', NOW(), 0),
(9, 1, 1101, '超级管理员角色关联菜单1101', 'admin', NOW(), 'admin', NOW(), 0),
(10, 1, 1102, '超级管理员角色关联菜单1102', 'admin', NOW(), 'admin', NOW(), 0),
(11, 1, 1103, '超级管理员角色关联菜单1103', 'admin', NOW(), 'admin', NOW(), 0),
(12, 1, 1104, '超级管理员角色关联菜单1104', 'admin', NOW(), 'admin', NOW(), 0),
(13, 1, 1105, '超级管理员角色关联菜单1105', 'admin', NOW(), 'admin', NOW(), 0),
(14, 1, 1200, '超级管理员角色关联菜单1200', 'admin', NOW(), 'admin', NOW(), 0),
(15, 1, 1201, '超级管理员角色关联菜单1201', 'admin', NOW(), 'admin', NOW(), 0),
(16, 1, 1202, '超级管理员角色关联菜单1202', 'admin', NOW(), 'admin', NOW(), 0),
(17, 1, 1203, '超级管理员角色关联菜单1203', 'admin', NOW(), 'admin', NOW(), 0),
(18, 1, 1204, '超级管理员角色关联菜单1204', 'admin', NOW(), 'admin', NOW(), 0),
(19, 1, 1205, '超级管理员角色关联菜单1205', 'admin', NOW(), 'admin', NOW(), 0),
(20, 1, 1300, '超级管理员角色关联菜单1300', 'admin', NOW(), 'admin', NOW(), 0),
(21, 1, 1301, '超级管理员角色关联菜单1301', 'admin', NOW(), 'admin', NOW(), 0),
(22, 1, 1302, '超级管理员角色关联菜单1302', 'admin', NOW(), 'admin', NOW(), 0),
(23, 1, 1303, '超级管理员角色关联菜单1303', 'admin', NOW(), 'admin', NOW(), 0),
(24, 1, 1304, '超级管理员角色关联菜单1304', 'admin', NOW(), 'admin', NOW(), 0),
(25, 1, 1305, '超级管理员角色关联菜单1305', 'admin', NOW(), 'admin', NOW(), 0),
(26, 1, 1400, '超级管理员角色关联菜单1400', 'admin', NOW(), 'admin', NOW(), 0),
(27, 1, 1401, '超级管理员角色关联菜单1401', 'admin', NOW(), 'admin', NOW(), 0),
(28, 1, 1402, '超级管理员角色关联菜单1402', 'admin', NOW(), 'admin', NOW(), 0),
(29, 1, 1403, '超级管理员角色关联菜单1403', 'admin', NOW(), 'admin', NOW(), 0),
(30, 1, 1500, '超级管理员角色关联菜单1500', 'admin', NOW(), 'admin', NOW(), 0),
(31, 1, 1501, '超级管理员角色关联菜单1501', 'admin', NOW(), 'admin', NOW(), 0),
(32, 1, 1502, '超级管理员角色关联菜单1502', 'admin', NOW(), 'admin', NOW(), 0),
(33, 1, 1503, '超级管理员角色关联菜单1503', 'admin', NOW(), 'admin', NOW(), 0),
(34, 1, 1600, '超级管理员角色关联菜单1600', 'admin', NOW(), 'admin', NOW(), 0),
(35, 1, 1601, '超级管理员角色关联菜单1601', 'admin', NOW(), 'admin', NOW(), 0),
(36, 1, 1602, '超级管理员角色关联菜单1602', 'admin', NOW(), 'admin', NOW(), 0),
(37, 1, 1603, '超级管理员角色关联菜单1603', 'admin', NOW(), 'admin', NOW(), 0),
(38, 1, 1700, '超级管理员角色关联菜单1700', 'admin', NOW(), 'admin', NOW(), 0),
(39, 1, 1701, '超级管理员角色关联菜单1701', 'admin', NOW(), 'admin', NOW(), 0),
(40, 1, 1702, '超级管理员角色关联菜单1702', 'admin', NOW(), 'admin', NOW(), 0),
(41, 1, 1703, '超级管理员角色关联菜单1703', 'admin', NOW(), 'admin', NOW(), 0),
(42, 1, 1704, '超级管理员角色关联菜单1704', 'admin', NOW(), 'admin', NOW(), 0),
(43, 1, 1705, '超级管理员角色关联菜单1705', 'admin', NOW(), 'admin', NOW(), 0),
(44, 1, 1800, '超级管理员角色关联菜单1800', 'admin', NOW(), 'admin', NOW(), 0),
(45, 1, 1801, '超级管理员角色关联菜单1801', 'admin', NOW(), 'admin', NOW(), 0),
(46, 1, 1802, '超级管理员角色关联菜单1802', 'admin', NOW(), 'admin', NOW(), 0),
(47, 1, 1803, '超级管理员角色关联菜单1803', 'admin', NOW(), 'admin', NOW(), 0),
(48, 1, 1804, '超级管理员角色关联菜单1804', 'admin', NOW(), 'admin', NOW(), 0),
(49, 1, 1805, '超级管理员角色关联菜单1805', 'admin', NOW(), 'admin', NOW(), 0),
(50, 1, 1900, '超级管理员角色关联菜单1900', 'admin', NOW(), 'admin', NOW(), 0),
(51, 1, 1901, '超级管理员角色关联菜单1901', 'admin', NOW(), 'admin', NOW(), 0),
(52, 1, 1902, '超级管理员角色关联菜单1902', 'admin', NOW(), 'admin', NOW(), 0),
(53, 1, 1903, '超级管理员角色关联菜单1903', 'admin', NOW(), 'admin', NOW(), 0),
(54, 1, 2000, '超级管理员角色关联菜单2000', 'admin', NOW(), 'admin', NOW(), 0),
(55, 1, 2001, '超级管理员角色关联菜单2001', 'admin', NOW(), 'admin', NOW(), 0),
(56, 1, 2100, '超级管理员角色关联菜单2100', 'admin', NOW(), 'admin', NOW(), 0),
(57, 1, 2101, '超级管理员角色关联菜单2101', 'admin', NOW(), 'admin', NOW(), 0),
(58, 1, 2102, '超级管理员角色关联菜单2102', 'admin', NOW(), 'admin', NOW(), 0),
(59, 1, 2103, '超级管理员角色关联菜单2103', 'admin', NOW(), 'admin', NOW(), 0),
(60, 1, 2104, '超级管理员角色关联菜单2104', 'admin', NOW(), 'admin', NOW(), 0),
(61, 1, 2105, '超级管理员角色关联菜单2105', 'admin', NOW(), 'admin', NOW(), 0),
(62, 1, 2106, '超级管理员角色关联菜单2106', 'admin', NOW(), 'admin', NOW(), 0),
(63, 1, 2107, '超级管理员角色关联菜单2107', 'admin', NOW(), 'admin', NOW(), 0),
(64, 1, 2108, '超级管理员角色关联菜单2108', 'admin', NOW(), 'admin', NOW(), 0),
(65, 1, 10011, '超级管理员角色关联菜单10011', 'admin', NOW(), 'admin', NOW(), 0),
(66, 1, 10012, '超级管理员角色关联菜单10012', 'admin', NOW(), 'admin', NOW(), 0),
(67, 1, 10013, '超级管理员角色关联菜单10013', 'admin', NOW(), 'admin', NOW(), 0),
(68, 1, 10014, '超级管理员角色关联菜单10014', 'admin', NOW(), 'admin', NOW(), 0),
(69, 1, 10021, '超级管理员角色关联菜单10021', 'admin', NOW(), 'admin', NOW(), 0),
(70, 1, 10022, '超级管理员角色关联菜单10022', 'admin', NOW(), 'admin', NOW(), 0),
(71, 1, 10023, '超级管理员角色关联菜单10023', 'admin', NOW(), 'admin', NOW(), 0),
(72, 1, 10024, '超级管理员角色关联菜单10024', 'admin', NOW(), 'admin', NOW(), 0),
(73, 1, 10031, '超级管理员角色关联菜单10031', 'admin', NOW(), 'admin', NOW(), 0),
(74, 1, 10032, '超级管理员角色关联菜单10032', 'admin', NOW(), 'admin', NOW(), 0),
(75, 1, 10033, '超级管理员角色关联菜单10033', 'admin', NOW(), 'admin', NOW(), 0),
(76, 1, 10034, '超级管理员角色关联菜单10034', 'admin', NOW(), 'admin', NOW(), 0),
(77, 1, 10041, '超级管理员角色关联菜单10041', 'admin', NOW(), 'admin', NOW(), 0),
(78, 1, 10042, '超级管理员角色关联菜单10042', 'admin', NOW(), 'admin', NOW(), 0),
(79, 1, 10043, '超级管理员角色关联菜单10043', 'admin', NOW(), 'admin', NOW(), 0),
(80, 1, 10044, '超级管理员角色关联菜单10044', 'admin', NOW(), 'admin', NOW(), 0),
(81, 1, 10051, '超级管理员角色关联菜单10051', 'admin', NOW(), 'admin', NOW(), 0),
(82, 1, 10052, '超级管理员角色关联菜单10052', 'admin', NOW(), 'admin', NOW(), 0),
(83, 1, 10053, '超级管理员角色关联菜单10053', 'admin', NOW(), 'admin', NOW(), 0),
(84, 1, 10054, '超级管理员角色关联菜单10054', 'admin', NOW(), 'admin', NOW(), 0),
(85, 1, 10061, '超级管理员角色关联菜单10061', 'admin', NOW(), 'admin', NOW(), 0),
(86, 1, 10062, '超级管理员角色关联菜单10062', 'admin', NOW(), 'admin', NOW(), 0),
(87, 1, 10063, '超级管理员角色关联菜单10063', 'admin', NOW(), 'admin', NOW(), 0),
(88, 1, 10064, '超级管理员角色关联菜单10064', 'admin', NOW(), 'admin', NOW(), 0),
(89, 1, 11011, '超级管理员角色关联菜单11011', 'admin', NOW(), 'admin', NOW(), 0),
(90, 1, 11012, '超级管理员角色关联菜单11012', 'admin', NOW(), 'admin', NOW(), 0),
(91, 1, 11013, '超级管理员角色关联菜单11013', 'admin', NOW(), 'admin', NOW(), 0),
(92, 1, 11014, '超级管理员角色关联菜单11014', 'admin', NOW(), 'admin', NOW(), 0),
(93, 1, 11021, '超级管理员角色关联菜单11021', 'admin', NOW(), 'admin', NOW(), 0),
(94, 1, 11022, '超级管理员角色关联菜单11022', 'admin', NOW(), 'admin', NOW(), 0),
(95, 1, 11023, '超级管理员角色关联菜单11023', 'admin', NOW(), 'admin', NOW(), 0),
(96, 1, 11024, '超级管理员角色关联菜单11024', 'admin', NOW(), 'admin', NOW(), 0),
(97, 1, 11031, '超级管理员角色关联菜单11031', 'admin', NOW(), 'admin', NOW(), 0),
(98, 1, 11032, '超级管理员角色关联菜单11032', 'admin', NOW(), 'admin', NOW(), 0),
(99, 1, 11033, '超级管理员角色关联菜单11033', 'admin', NOW(), 'admin', NOW(), 0),
(100, 1, 11034, '超级管理员角色关联菜单11034', 'admin', NOW(), 'admin', NOW(), 0),
(101, 1, 11041, '超级管理员角色关联菜单11041', 'admin', NOW(), 'admin', NOW(), 0),
(102, 1, 11042, '超级管理员角色关联菜单11042', 'admin', NOW(), 'admin', NOW(), 0),
(103, 1, 11043, '超级管理员角色关联菜单11043', 'admin', NOW(), 'admin', NOW(), 0),
(104, 1, 11044, '超级管理员角色关联菜单11044', 'admin', NOW(), 'admin', NOW(), 0),
(105, 1, 11051, '超级管理员角色关联菜单11051', 'admin', NOW(), 'admin', NOW(), 0),
(106, 1, 11052, '超级管理员角色关联菜单11052', 'admin', NOW(), 'admin', NOW(), 0),
(107, 1, 11053, '超级管理员角色关联菜单11053', 'admin', NOW(), 'admin', NOW(), 0),
(108, 1, 11054, '超级管理员角色关联菜单11054', 'admin', NOW(), 'admin', NOW(), 0),
(109, 1, 12011, '超级管理员角色关联菜单12011', 'admin', NOW(), 'admin', NOW(), 0),
(110, 1, 12012, '超级管理员角色关联菜单12012', 'admin', NOW(), 'admin', NOW(), 0),
(111, 1, 12013, '超级管理员角色关联菜单12013', 'admin', NOW(), 'admin', NOW(), 0),
(112, 1, 12014, '超级管理员角色关联菜单12014', 'admin', NOW(), 'admin', NOW(), 0),
(113, 1, 12021, '超级管理员角色关联菜单12021', 'admin', NOW(), 'admin', NOW(), 0),
(114, 1, 12022, '超级管理员角色关联菜单12022', 'admin', NOW(), 'admin', NOW(), 0),
(115, 1, 12023, '超级管理员角色关联菜单12023', 'admin', NOW(), 'admin', NOW(), 0),
(116, 1, 12024, '超级管理员角色关联菜单12024', 'admin', NOW(), 'admin', NOW(), 0),
(117, 1, 12031, '超级管理员角色关联菜单12031', 'admin', NOW(), 'admin', NOW(), 0),
(118, 1, 12032, '超级管理员角色关联菜单12032', 'admin', NOW(), 'admin', NOW(), 0),
(119, 1, 12033, '超级管理员角色关联菜单12033', 'admin', NOW(), 'admin', NOW(), 0),
(120, 1, 12034, '超级管理员角色关联菜单12034', 'admin', NOW(), 'admin', NOW(), 0),
(121, 1, 12041, '超级管理员角色关联菜单12041', 'admin', NOW(), 'admin', NOW(), 0),
(122, 1, 12042, '超级管理员角色关联菜单12042', 'admin', NOW(), 'admin', NOW(), 0),
(123, 1, 12043, '超级管理员角色关联菜单12043', 'admin', NOW(), 'admin', NOW(), 0),
(124, 1, 12044, '超级管理员角色关联菜单12044', 'admin', NOW(), 'admin', NOW(), 0),
(125, 1, 12051, '超级管理员角色关联菜单12051', 'admin', NOW(), 'admin', NOW(), 0),
(126, 1, 12052, '超级管理员角色关联菜单12052', 'admin', NOW(), 'admin', NOW(), 0),
(127, 1, 12053, '超级管理员角色关联菜单12053', 'admin', NOW(), 'admin', NOW(), 0),
(128, 1, 12054, '超级管理员角色关联菜单12054', 'admin', NOW(), 'admin', NOW(), 0),
(129, 1, 13011, '超级管理员角色关联菜单13011', 'admin', NOW(), 'admin', NOW(), 0),
(130, 1, 13012, '超级管理员角色关联菜单13012', 'admin', NOW(), 'admin', NOW(), 0),
(131, 1, 13013, '超级管理员角色关联菜单13013', 'admin', NOW(), 'admin', NOW(), 0),
(132, 1, 13014, '超级管理员角色关联菜单13014', 'admin', NOW(), 'admin', NOW(), 0),
(133, 1, 13021, '超级管理员角色关联菜单13021', 'admin', NOW(), 'admin', NOW(), 0),
(134, 1, 13022, '超级管理员角色关联菜单13022', 'admin', NOW(), 'admin', NOW(), 0),
(135, 1, 13023, '超级管理员角色关联菜单13023', 'admin', NOW(), 'admin', NOW(), 0),
(136, 1, 13024, '超级管理员角色关联菜单13024', 'admin', NOW(), 'admin', NOW(), 0),
(137, 1, 13031, '超级管理员角色关联菜单13031', 'admin', NOW(), 'admin', NOW(), 0),
(138, 1, 13032, '超级管理员角色关联菜单13032', 'admin', NOW(), 'admin', NOW(), 0),
(139, 1, 13033, '超级管理员角色关联菜单13033', 'admin', NOW(), 'admin', NOW(), 0),
(140, 1, 13034, '超级管理员角色关联菜单13034', 'admin', NOW(), 'admin', NOW(), 0),
(141, 1, 13041, '超级管理员角色关联菜单13041', 'admin', NOW(), 'admin', NOW(), 0),
(142, 1, 13042, '超级管理员角色关联菜单13042', 'admin', NOW(), 'admin', NOW(), 0),
(143, 1, 13043, '超级管理员角色关联菜单13043', 'admin', NOW(), 'admin', NOW(), 0),
(144, 1, 13044, '超级管理员角色关联菜单13044', 'admin', NOW(), 'admin', NOW(), 0),
(145, 1, 13051, '超级管理员角色关联菜单13051', 'admin', NOW(), 'admin', NOW(), 0),
(146, 1, 13052, '超级管理员角色关联菜单13052', 'admin', NOW(), 'admin', NOW(), 0),
(147, 1, 13053, '超级管理员角色关联菜单13053', 'admin', NOW(), 'admin', NOW(), 0),
(148, 1, 13054, '超级管理员角色关联菜单13054', 'admin', NOW(), 'admin', NOW(), 0),
(149, 1, 14011, '超级管理员角色关联菜单14011', 'admin', NOW(), 'admin', NOW(), 0),
(150, 1, 14012, '超级管理员角色关联菜单14012', 'admin', NOW(), 'admin', NOW(), 0),
(151, 1, 14013, '超级管理员角色关联菜单14013', 'admin', NOW(), 'admin', NOW(), 0),
(152, 1, 14014, '超级管理员角色关联菜单14014', 'admin', NOW(), 'admin', NOW(), 0),
(153, 1, 14021, '超级管理员角色关联菜单14021', 'admin', NOW(), 'admin', NOW(), 0),
(154, 1, 14022, '超级管理员角色关联菜单14022', 'admin', NOW(), 'admin', NOW(), 0),
(155, 1, 14023, '超级管理员角色关联菜单14023', 'admin', NOW(), 'admin', NOW(), 0),
(156, 1, 14024, '超级管理员角色关联菜单14024', 'admin', NOW(), 'admin', NOW(), 0),
(157, 1, 14031, '超级管理员角色关联菜单14031', 'admin', NOW(), 'admin', NOW(), 0),
(158, 1, 14032, '超级管理员角色关联菜单14032', 'admin', NOW(), 'admin', NOW(), 0),
(159, 1, 14033, '超级管理员角色关联菜单14033', 'admin', NOW(), 'admin', NOW(), 0),
(160, 1, 14034, '超级管理员角色关联菜单14034', 'admin', NOW(), 'admin', NOW(), 0),
(161, 1, 15011, '超级管理员角色关联菜单15011', 'admin', NOW(), 'admin', NOW(), 0),
(162, 1, 15012, '超级管理员角色关联菜单15012', 'admin', NOW(), 'admin', NOW(), 0),
(163, 1, 15013, '超级管理员角色关联菜单15013', 'admin', NOW(), 'admin', NOW(), 0),
(164, 1, 15014, '超级管理员角色关联菜单15014', 'admin', NOW(), 'admin', NOW(), 0),
(165, 1, 15021, '超级管理员角色关联菜单15021', 'admin', NOW(), 'admin', NOW(), 0),
(166, 1, 15022, '超级管理员角色关联菜单15022', 'admin', NOW(), 'admin', NOW(), 0),
(167, 1, 15023, '超级管理员角色关联菜单15023', 'admin', NOW(), 'admin', NOW(), 0),
(168, 1, 15024, '超级管理员角色关联菜单15024', 'admin', NOW(), 'admin', NOW(), 0),
(169, 1, 15031, '超级管理员角色关联菜单15031', 'admin', NOW(), 'admin', NOW(), 0),
(170, 1, 15032, '超级管理员角色关联菜单15032', 'admin', NOW(), 'admin', NOW(), 0),
(171, 1, 15033, '超级管理员角色关联菜单15033', 'admin', NOW(), 'admin', NOW(), 0),
(172, 1, 15034, '超级管理员角色关联菜单15034', 'admin', NOW(), 'admin', NOW(), 0),
(173, 1, 16011, '超级管理员角色关联菜单16011', 'admin', NOW(), 'admin', NOW(), 0),
(174, 1, 16012, '超级管理员角色关联菜单16012', 'admin', NOW(), 'admin', NOW(), 0),
(175, 1, 16013, '超级管理员角色关联菜单16013', 'admin', NOW(), 'admin', NOW(), 0),
(176, 1, 16014, '超级管理员角色关联菜单16014', 'admin', NOW(), 'admin', NOW(), 0),
(177, 1, 16021, '超级管理员角色关联菜单16021', 'admin', NOW(), 'admin', NOW(), 0),
(178, 1, 16022, '超级管理员角色关联菜单16022', 'admin', NOW(), 'admin', NOW(), 0),
(179, 1, 16023, '超级管理员角色关联菜单16023', 'admin', NOW(), 'admin', NOW(), 0),
(180, 1, 16024, '超级管理员角色关联菜单16024', 'admin', NOW(), 'admin', NOW(), 0),
(181, 1, 16031, '超级管理员角色关联菜单16031', 'admin', NOW(), 'admin', NOW(), 0),
(182, 1, 16032, '超级管理员角色关联菜单16032', 'admin', NOW(), 'admin', NOW(), 0),
(183, 1, 16033, '超级管理员角色关联菜单16033', 'admin', NOW(), 'admin', NOW(), 0),
(184, 1, 16034, '超级管理员角色关联菜单16034', 'admin', NOW(), 'admin', NOW(), 0),
(185, 1, 17011, '超级管理员角色关联菜单17011', 'admin', NOW(), 'admin', NOW(), 0),
(186, 1, 17012, '超级管理员角色关联菜单17012', 'admin', NOW(), 'admin', NOW(), 0),
(187, 1, 17013, '超级管理员角色关联菜单17013', 'admin', NOW(), 'admin', NOW(), 0),
(188, 1, 17014, '超级管理员角色关联菜单17014', 'admin', NOW(), 'admin', NOW(), 0),
(189, 1, 17021, '超级管理员角色关联菜单17021', 'admin', NOW(), 'admin', NOW(), 0),
(190, 1, 17022, '超级管理员角色关联菜单17022', 'admin', NOW(), 'admin', NOW(), 0),
(191, 1, 17023, '超级管理员角色关联菜单17023', 'admin', NOW(), 'admin', NOW(), 0),
(192, 1, 17024, '超级管理员角色关联菜单17024', 'admin', NOW(), 'admin', NOW(), 0),
(193, 1, 17031, '超级管理员角色关联菜单17031', 'admin', NOW(), 'admin', NOW(), 0),
(194, 1, 17032, '超级管理员角色关联菜单17032', 'admin', NOW(), 'admin', NOW(), 0),
(195, 1, 17033, '超级管理员角色关联菜单17033', 'admin', NOW(), 'admin', NOW(), 0),
(196, 1, 17034, '超级管理员角色关联菜单17034', 'admin', NOW(), 'admin', NOW(), 0),
(197, 1, 17041, '超级管理员角色关联菜单17041', 'admin', NOW(), 'admin', NOW(), 0),
(198, 1, 17042, '超级管理员角色关联菜单17042', 'admin', NOW(), 'admin', NOW(), 0),
(199, 1, 17043, '超级管理员角色关联菜单17043', 'admin', NOW(), 'admin', NOW(), 0),
(200, 1, 17044, '超级管理员角色关联菜单17044', 'admin', NOW(), 'admin', NOW(), 0),
(201, 1, 17051, '超级管理员角色关联菜单17051', 'admin', NOW(), 'admin', NOW(), 0),
(202, 1, 17052, '超级管理员角色关联菜单17052', 'admin', NOW(), 'admin', NOW(), 0),
(203, 1, 17053, '超级管理员角色关联菜单17053', 'admin', NOW(), 'admin', NOW(), 0),
(204, 1, 17054, '超级管理员角色关联菜单17054', 'admin', NOW(), 'admin', NOW(), 0),
(205, 1, 18011, '超级管理员角色关联菜单18011', 'admin', NOW(), 'admin', NOW(), 0),
(206, 1, 18012, '超级管理员角色关联菜单18012', 'admin', NOW(), 'admin', NOW(), 0),
(207, 1, 18013, '超级管理员角色关联菜单18013', 'admin', NOW(), 'admin', NOW(), 0),
(208, 1, 18014, '超级管理员角色关联菜单18014', 'admin', NOW(), 'admin', NOW(), 0),
(209, 1, 18021, '超级管理员角色关联菜单18021', 'admin', NOW(), 'admin', NOW(), 0),
(210, 1, 18022, '超级管理员角色关联菜单18022', 'admin', NOW(), 'admin', NOW(), 0),
(211, 1, 18023, '超级管理员角色关联菜单18023', 'admin', NOW(), 'admin', NOW(), 0),
(212, 1, 18024, '超级管理员角色关联菜单18024', 'admin', NOW(), 'admin', NOW(), 0),
(213, 1, 18031, '超级管理员角色关联菜单18031', 'admin', NOW(), 'admin', NOW(), 0),
(214, 1, 18032, '超级管理员角色关联菜单18032', 'admin', NOW(), 'admin', NOW(), 0),
(215, 1, 18033, '超级管理员角色关联菜单18033', 'admin', NOW(), 'admin', NOW(), 0),
(216, 1, 18034, '超级管理员角色关联菜单18034', 'admin', NOW(), 'admin', NOW(), 0),
(217, 1, 18041, '超级管理员角色关联菜单18041', 'admin', NOW(), 'admin', NOW(), 0),
(218, 1, 18042, '超级管理员角色关联菜单18042', 'admin', NOW(), 'admin', NOW(), 0),
(219, 1, 18043, '超级管理员角色关联菜单18043', 'admin', NOW(), 'admin', NOW(), 0),
(220, 1, 18044, '超级管理员角色关联菜单18044', 'admin', NOW(), 'admin', NOW(), 0),
(221, 1, 18051, '超级管理员角色关联菜单18051', 'admin', NOW(), 'admin', NOW(), 0),
(222, 1, 18052, '超级管理员角色关联菜单18052', 'admin', NOW(), 'admin', NOW(), 0),
(223, 1, 18053, '超级管理员角色关联菜单18053', 'admin', NOW(), 'admin', NOW(), 0),
(224, 1, 18054, '超级管理员角色关联菜单18054', 'admin', NOW(), 'admin', NOW(), 0),
(225, 1, 19011, '超级管理员角色关联菜单19011', 'admin', NOW(), 'admin', NOW(), 0),
(226, 1, 19012, '超级管理员角色关联菜单19012', 'admin', NOW(), 'admin', NOW(), 0),
(227, 1, 19013, '超级管理员角色关联菜单19013', 'admin', NOW(), 'admin', NOW(), 0),
(228, 1, 19014, '超级管理员角色关联菜单19014', 'admin', NOW(), 'admin', NOW(), 0),
(229, 1, 19021, '超级管理员角色关联菜单19021', 'admin', NOW(), 'admin', NOW(), 0),
(230, 1, 19022, '超级管理员角色关联菜单19022', 'admin', NOW(), 'admin', NOW(), 0),
(231, 1, 19023, '超级管理员角色关联菜单19023', 'admin', NOW(), 'admin', NOW(), 0),
(232, 1, 19024, '超级管理员角色关联菜单19024', 'admin', NOW(), 'admin', NOW(), 0),
(233, 1, 19031, '超级管理员角色关联菜单19031', 'admin', NOW(), 'admin', NOW(), 0),
(234, 1, 19032, '超级管理员角色关联菜单19032', 'admin', NOW(), 'admin', NOW(), 0),
(235, 1, 19033, '超级管理员角色关联菜单19033', 'admin', NOW(), 'admin', NOW(), 0),
(236, 1, 19034, '超级管理员角色关联菜单19034', 'admin', NOW(), 'admin', NOW(), 0),
(237, 1, 20011, '超级管理员角色关联菜单20011', 'admin', NOW(), 'admin', NOW(), 0),
(238, 1, 20012, '超级管理员角色关联菜单20012', 'admin', NOW(), 'admin', NOW(), 0),
(239, 1, 20013, '超级管理员角色关联菜单20013', 'admin', NOW(), 'admin', NOW(), 0),
(240, 1, 20014, '超级管理员角色关联菜单20014', 'admin', NOW(), 'admin', NOW(), 0),
(241, 1, 21011, '超级管理员角色关联菜单21011', 'admin', NOW(), 'admin', NOW(), 0),
(242, 1, 21012, '超级管理员角色关联菜单21012', 'admin', NOW(), 'admin', NOW(), 0),
(243, 1, 21013, '超级管理员角色关联菜单21013', 'admin', NOW(), 'admin', NOW(), 0),
(244, 1, 21014, '超级管理员角色关联菜单21014', 'admin', NOW(), 'admin', NOW(), 0),
(245, 1, 21015, '超级管理员角色关联菜单21015', 'admin', NOW(), 'admin', NOW(), 0),
(246, 1, 21021, '超级管理员角色关联菜单21021', 'admin', NOW(), 'admin', NOW(), 0),
(247, 1, 21022, '超级管理员角色关联菜单21022', 'admin', NOW(), 'admin', NOW(), 0),
(248, 1, 21023, '超级管理员角色关联菜单21023', 'admin', NOW(), 'admin', NOW(), 0),
(249, 1, 21024, '超级管理员角色关联菜单21024', 'admin', NOW(), 'admin', NOW(), 0),
(250, 1, 21031, '超级管理员角色关联菜单21031', 'admin', NOW(), 'admin', NOW(), 0),
(251, 1, 21032, '超级管理员角色关联菜单21032', 'admin', NOW(), 'admin', NOW(), 0),
(252, 1, 21033, '超级管理员角色关联菜单21033', 'admin', NOW(), 'admin', NOW(), 0),
(253, 1, 21034, '超级管理员角色关联菜单21034', 'admin', NOW(), 'admin', NOW(), 0),
(254, 1, 21041, '超级管理员角色关联菜单21041', 'admin', NOW(), 'admin', NOW(), 0),
(255, 1, 21042, '超级管理员角色关联菜单21042', 'admin', NOW(), 'admin', NOW(), 0),
(256, 1, 21043, '超级管理员角色关联菜单21043', 'admin', NOW(), 'admin', NOW(), 0),
(257, 1, 21044, '超级管理员角色关联菜单21044', 'admin', NOW(), 'admin', NOW(), 0),
(258, 1, 21051, '超级管理员角色关联菜单21051', 'admin', NOW(), 'admin', NOW(), 0),
(259, 1, 21052, '超级管理员角色关联菜单21052', 'admin', NOW(), 'admin', NOW(), 0),
(260, 1, 21053, '超级管理员角色关联菜单21053', 'admin', NOW(), 'admin', NOW(), 0),
(261, 1, 21054, '超级管理员角色关联菜单21054', 'admin', NOW(), 'admin', NOW(), 0),
(262, 1, 21061, '超级管理员角色关联菜单21061', 'admin', NOW(), 'admin', NOW(), 0),
(263, 1, 21062, '超级管理员角色关联菜单21062', 'admin', NOW(), 'admin', NOW(), 0),
(264, 1, 21063, '超级管理员角色关联菜单21063', 'admin', NOW(), 'admin', NOW(), 0),
(265, 1, 21064, '超级管理员角色关联菜单21064', 'admin', NOW(), 'admin', NOW(), 0),
(266, 1, 21071, '超级管理员角色关联菜单21071', 'admin', NOW(), 'admin', NOW(), 0),
(267, 1, 21072, '超级管理员角色关联菜单21072', 'admin', NOW(), 'admin', NOW(), 0),
(268, 1, 21073, '超级管理员角色关联菜单21073', 'admin', NOW(), 'admin', NOW(), 0),
(269, 1, 21081, '超级管理员角色关联菜单21081', 'admin', NOW(), 'admin', NOW(), 0),
(270, 1, 21082, '超级管理员角色关联菜单21082', 'admin', NOW(), 'admin', NOW(), 0),
(271, 1, 21083, '超级管理员角色关联菜单21083', 'admin', NOW(), 'admin', NOW(), 0);

-- -----------------------------------------------------------
-- 8. 字典数据
-- -----------------------------------------------------------

-- ========== sys_normal_status (正常/停用) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1, '系统状态', 'sys_normal_status', 1, '系统通用状态：正常/停用', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(101, 1, 'sys_normal_status', '正常', '0', 1, 1, '状态-正常', 'admin', NOW(), 'admin', NOW(), 0),
(102, 1, 'sys_normal_status', '停用', '1', 2, 1, '状态-停用', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== sys_show_hide (显示/隐藏) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (2, '显示隐藏', 'sys_show_hide', 1, '菜单或字段是否显示', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(201, 2, 'sys_show_hide', '显示', '0', 1, 1, '显示', 'admin', NOW(), 'admin', NOW(), 0),
(202, 2, 'sys_show_hide', '隐藏', '1', 2, 1, '隐藏', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== sys_user_sex (男/女/未知) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (3, '用户性别', 'sys_user_sex', 1, '用户性别列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(301, 3, 'sys_user_sex', '男',   '1', 1, 1, '性别-男',   'admin', NOW(), 'admin', NOW(), 0),
(302, 3, 'sys_user_sex', '女',   '2', 2, 1, '性别-女',   'admin', NOW(), 'admin', NOW(), 0),
(303, 3, 'sys_user_sex', '未知', '0', 3, 1, '性别-未知', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== sys_menu_type (目录/菜单/按钮) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (4, '菜单类型', 'sys_menu_type', 1, '菜单类型列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(401, 4, 'sys_menu_type', '目录', '1', 1, 1, '菜单类型-目录', 'admin', NOW(), 'admin', NOW(), 0),
(402, 4, 'sys_menu_type', '菜单', '2', 2, 1, '菜单类型-菜单', 'admin', NOW(), 'admin', NOW(), 0),
(403, 4, 'sys_menu_type', '按钮', '3', 3, 1, '菜单类型-按钮', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== safety_event_type (安全事件类型) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (5, '安全事件类型', 'safety_event_type', 1, '安全事件类型列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(501, 5, 'safety_event_type', '闯入',         '1', 1, 1, '闯入',         'admin', NOW(), 'admin', NOW(), 0),
(502, 5, 'safety_event_type', '翻越',         '2', 2, 1, '翻越',         'admin', NOW(), 'admin', NOW(), 0),
(503, 5, 'safety_event_type', '违规逗留',     '3', 3, 1, '违规逗留',     'admin', NOW(), 'admin', NOW(), 0),
(504, 5, 'safety_event_type', '异常聚集',     '4', 4, 1, '异常聚集',     'admin', NOW(), 'admin', NOW(), 0),
(505, 5, 'safety_event_type', '未戴安全帽',   '5', 5, 1, '未戴安全帽',   'admin', NOW(), 'admin', NOW(), 0),
(506, 5, 'safety_event_type', '烟火',         '6', 6, 1, '烟火',         'admin', NOW(), 'admin', NOW(), 0),
(507, 5, 'safety_event_type', '违规用电',     '7', 7, 1, '违规用电',     'admin', NOW(), 'admin', NOW(), 0),
(508, 5, 'safety_event_type', '堵塞消防通道', '8', 8, 1, '堵塞消防通道', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== safety_event_level (安全事件等级) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (6, '安全事件等级', 'safety_event_level', 1, '安全事件等级列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(601, 6, 'safety_event_level', '一般', '1', 1, 1, '一般', 'admin', NOW(), 'admin', NOW(), 0),
(602, 6, 'safety_event_level', '较大', '2', 2, 1, '较大', 'admin', NOW(), 'admin', NOW(), 0),
(603, 6, 'safety_event_level', '重大', '3', 3, 1, '重大', 'admin', NOW(), 'admin', NOW(), 0),
(604, 6, 'safety_event_level', '特大', '4', 4, 1, '特大', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== emergency_event_level (应急事件等级) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (7, '应急事件等级', 'emergency_event_level', 1, '应急事件等级列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(701, 7, 'emergency_event_level', '一般', '1', 1, 1, '一般', 'admin', NOW(), 'admin', NOW(), 0),
(702, 7, 'emergency_event_level', '较大', '2', 2, 1, '较大', 'admin', NOW(), 'admin', NOW(), 0),
(703, 7, 'emergency_event_level', '重大', '3', 3, 1, '重大', 'admin', NOW(), 'admin', NOW(), 0),
(704, 7, 'emergency_event_level', '特大', '4', 4, 1, '特大', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== asset_type (资产类型) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (8, '资产类型', 'asset_type', 1, '资产类型列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(801, 8, 'asset_type', '不动产',     '1', 1, 1, '不动产',     'admin', NOW(), 'admin', NOW(), 0),
(802, 8, 'asset_type', '智能化设备', '2', 2, 1, '智能化设备', 'admin', NOW(), 'admin', NOW(), 0),
(803, 8, 'asset_type', '公共设施',   '3', 3, 1, '公共设施',   'admin', NOW(), 'admin', NOW(), 0),
(804, 8, 'asset_type', '管网',       '4', 4, 1, '管网',       'admin', NOW(), 'admin', NOW(), 0),
(805, 8, 'asset_type', '电力',       '5', 5, 1, '电力',       'admin', NOW(), 'admin', NOW(), 0),
(806, 8, 'asset_type', '安防',       '6', 6, 1, '安防',       'admin', NOW(), 'admin', NOW(), 0),
(807, 8, 'asset_type', '应急',       '7', 7, 1, '应急',       'admin', NOW(), 'admin', NOW(), 0),
(808, 8, 'asset_type', '环境监测',   '8', 8, 1, '环境监测',   'admin', NOW(), 'admin', NOW(), 0);

-- ========== asset_status (资产状态) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (9, '资产状态', 'asset_status', 1, '资产状态列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(901, 9, 'asset_status', '在用', '1', 1, 1, '在用', 'admin', NOW(), 'admin', NOW(), 0),
(902, 9, 'asset_status', '闲置', '2', 2, 1, '闲置', 'admin', NOW(), 'admin', NOW(), 0),
(903, 9, 'asset_status', '维修', '3', 3, 1, '维修', 'admin', NOW(), 'admin', NOW(), 0),
(904, 9, 'asset_status', '报废', '4', 4, 1, '报废', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== energy_meter_type (能源计量类型) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (10, '能源计量类型', 'energy_meter_type', 1, '能源计量类型列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(1001, 10, 'energy_meter_type', '电',       '1', 1, 1, '电',       'admin', NOW(), 'admin', NOW(), 0),
(1002, 10, 'energy_meter_type', '水',       '2', 2, 1, '水',       'admin', NOW(), 'admin', NOW(), 0),
(1003, 10, 'energy_meter_type', '燃气',     '3', 3, 1, '燃气',     'admin', NOW(), 'admin', NOW(), 0),
(1004, 10, 'energy_meter_type', '蒸汽',     '4', 4, 1, '蒸汽',     'admin', NOW(), 'admin', NOW(), 0),
(1005, 10, 'energy_meter_type', '压缩空气', '5', 5, 1, '压缩空气', 'admin', NOW(), 'admin', NOW(), 0),
(1006, 10, 'energy_meter_type', '热力',     '6', 6, 1, '热力',     'admin', NOW(), 'admin', NOW(), 0),
(1007, 10, 'energy_meter_type', '制冷',     '7', 7, 1, '制冷',     'admin', NOW(), 'admin', NOW(), 0);

-- ========== env_point_type (环境监测点位类型) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (11, '环境监测点位类型', 'env_point_type', 1, '环境监测点位类型列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(1101, 11, 'env_point_type', '大气', '1', 1, 1, '大气', 'admin', NOW(), 'admin', NOW(), 0),
(1102, 11, 'env_point_type', '水质', '2', 2, 1, '水质', 'admin', NOW(), 'admin', NOW(), 0),
(1103, 11, 'env_point_type', '噪音', '3', 3, 1, '噪音', 'admin', NOW(), 'admin', NOW(), 0),
(1104, 11, 'env_point_type', '扬尘', '4', 4, 1, '扬尘', 'admin', NOW(), 'admin', NOW(), 0),
(1105, 11, 'env_point_type', '土壤', '5', 5, 1, '土壤', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== property_fee_type (物业费用类型) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (12, '物业费用类型', 'property_fee_type', 1, '物业费用类型列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(1201, 12, 'property_fee_type', '物业费', '1', 1, 1, '物业费', 'admin', NOW(), 'admin', NOW(), 0),
(1202, 12, 'property_fee_type', '水电费', '2', 2, 1, '水电费', 'admin', NOW(), 'admin', NOW(), 0),
(1203, 12, 'property_fee_type', '停车费', '3', 3, 1, '停车费', 'admin', NOW(), 'admin', NOW(), 0),
(1204, 12, 'property_fee_type', '租赁费', '4', 4, 1, '租赁费', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== property_wo_type (物业工单类型) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (13, '物业工单类型', 'property_wo_type', 1, '物业工单类型列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(1301, 13, 'property_wo_type', '保洁', '1', 1, 1, '保洁', 'admin', NOW(), 'admin', NOW(), 0),
(1302, 13, 'property_wo_type', '绿化', '2', 2, 1, '绿化', 'admin', NOW(), 'admin', NOW(), 0),
(1303, 13, 'property_wo_type', '安保', '3', 3, 1, '安保', 'admin', NOW(), 'admin', NOW(), 0),
(1304, 13, 'property_wo_type', '会务', '4', 4, 1, '会务', 'admin', NOW(), 'admin', NOW(), 0),
(1305, 13, 'property_wo_type', '维修', '5', 5, 1, '维修', 'admin', NOW(), 'admin', NOW(), 0),
(1306, 13, 'property_wo_type', '其他', '6', 6, 1, '其他', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== property_wo_priority (物业工单优先级) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (14, '物业工单优先级', 'property_wo_priority', 1, '物业工单优先级列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(1401, 14, 'property_wo_priority', '低',   '1', 1, 1, '低',   'admin', NOW(), 'admin', NOW(), 0),
(1402, 14, 'property_wo_priority', '中',   '2', 2, 1, '中',   'admin', NOW(), 'admin', NOW(), 0),
(1403, 14, 'property_wo_priority', '高',   '3', 3, 1, '高',   'admin', NOW(), 'admin', NOW(), 0),
(1404, 14, 'property_wo_priority', '紧急', '4', 4, 1, '紧急', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== investment_asset_status (招商房源状态) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (15, '招商房源状态', 'investment_asset_status', 1, '招商房源状态列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(1501, 15, 'investment_asset_status', '可租',   '1', 1, 1, '可租',   'admin', NOW(), 'admin', NOW(), 0),
(1502, 15, 'investment_asset_status', '已租',   '2', 2, 1, '已租',   'admin', NOW(), 'admin', NOW(), 0),
(1503, 15, 'investment_asset_status', '预留',   '3', 3, 1, '预留',   'admin', NOW(), 'admin', NOW(), 0),
(1504, 15, 'investment_asset_status', '不可租', '4', 4, 1, '不可租', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== investment_customer_status (招商客户状态) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (16, '招商客户状态', 'investment_customer_status', 1, '招商客户状态列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(1601, 16, 'investment_customer_status', '公海',   '1', 1, 1, '公海',   'admin', NOW(), 'admin', NOW(), 0),
(1602, 16, 'investment_customer_status', '跟进中', '2', 2, 1, '跟进中', 'admin', NOW(), 'admin', NOW(), 0),
(1603, 16, 'investment_customer_status', '已签约', '3', 3, 1, '已签约', 'admin', NOW(), 'admin', NOW(), 0),
(1604, 16, 'investment_customer_status', '已流失', '4', 4, 1, '已流失', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== investment_contract_status (招商合同状态) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (17, '招商合同状态', 'investment_contract_status', 1, '招商合同状态列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(1701, 17, 'investment_contract_status', '草稿',   '1', 1, 1, '草稿',   'admin', NOW(), 'admin', NOW(), 0),
(1702, 17, 'investment_contract_status', '审批中', '2', 2, 1, '审批中', 'admin', NOW(), 'admin', NOW(), 0),
(1703, 17, 'investment_contract_status', '生效中', '3', 3, 1, '生效中', 'admin', NOW(), 'admin', NOW(), 0),
(1704, 17, 'investment_contract_status', '已终止', '4', 4, 1, '已终止', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== report_cycle (报告周期) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (18, '报告周期', 'report_cycle', 1, 'AI智能报告周期列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(1801, 18, 'report_cycle', '日', '1', 1, 1, '日', 'admin', NOW(), 'admin', NOW(), 0),
(1802, 18, 'report_cycle', '周', '2', 2, 1, '周', 'admin', NOW(), 'admin', NOW(), 0),
(1803, 18, 'report_cycle', '月', '3', 3, 1, '月', 'admin', NOW(), 'admin', NOW(), 0),
(1804, 18, 'report_cycle', '季', '4', 4, 1, '季', 'admin', NOW(), 'admin', NOW(), 0),
(1805, 18, 'report_cycle', '年', '5', 5, 1, '年', 'admin', NOW(), 'admin', NOW(), 0);

-- ========== report_module (报告模块) ==========
INSERT INTO `sys_dict` (`id`, `dict_name`, `dict_type`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (19, '报告模块', 'report_module', 1, 'AI智能报告模块列表', 'admin', NOW(), 'admin', NOW(), 0);

INSERT INTO `sys_dict_item` (`id`, `dict_id`, `dict_type`, `item_label`, `item_value`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES
(1901, 19, 'report_module', '安全', '1',  1, 1, '安全', 'admin', NOW(), 'admin', NOW(), 0),
(1902, 19, 'report_module', '应急', '2',  2, 1, '应急', 'admin', NOW(), 'admin', NOW(), 0),
(1903, 19, 'report_module', '能源', '3',  3, 1, '能源', 'admin', NOW(), 'admin', NOW(), 0),
(1904, 19, 'report_module', '环境', '4',  4, 1, '环境', 'admin', NOW(), 'admin', NOW(), 0),
(1905, 19, 'report_module', '经济', '5',  5, 1, '经济', 'admin', NOW(), 'admin', NOW(), 0),
(1906, 19, 'report_module', '企业', '6',  6, 1, '企业', 'admin', NOW(), 'admin', NOW(), 0),
(1907, 19, 'report_module', '物业', '7',  7, 1, '物业', 'admin', NOW(), 'admin', NOW(), 0),
(1908, 19, 'report_module', '招商', '8',  8, 1, '招商', 'admin', NOW(), 'admin', NOW(), 0),
(1909, 19, 'report_module', '设备', '9',  9, 1, '设备', 'admin', NOW(), 'admin', NOW(), 0),
(1910, 19, 'report_module', '综合', '10', 10, 1, '综合', 'admin', NOW(), 'admin', NOW(), 0);


-- -----------------------------------------------------------
-- 9. 图标库（12个内置图标）
-- -----------------------------------------------------------
INSERT INTO `twin_icon_library` (`id`, `icon_name`, `icon_type`, `file_url`, `file_size`, `width`, `height`, `is_builtin`, `status`, `sort_order`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES
(1,  '办公楼A',   'building', '/icons/building-a.svg',   1024, 32, 32, 1, 1, 1,  'system', NOW(), '', NOW(), 0),
(2,  '办公楼B',   'building', '/icons/building-b.svg',   1024, 32, 32, 1, 1, 2,  'system', NOW(), '', NOW(), 0),
(3,  '摄像头',    'camera',   '/icons/camera.svg',        768,  32, 32, 1, 1, 3,  'system', NOW(), '', NOW(), 0),
(4,  '温度传感器', 'sensor',  '/icons/sensor-temp.svg',   512,  32, 32, 1, 1, 4,  'system', NOW(), '', NOW(), 0),
(5,  '湿度传感器', 'sensor',  '/icons/sensor-humidity.svg',512, 32, 32, 1, 1, 5,  'system', NOW(), '', NOW(), 0),
(6,  '消防栓',    'fire',     '/icons/fire-hydrant.svg',  640,  32, 32, 1, 1, 6,  'system', NOW(), '', NOW(), 0),
(7,  '门禁',      'access',   '/icons/access-control.svg', 896, 32, 32, 1, 1, 7,  'system', NOW(), '', NOW(), 0),
(8,  '配电箱',    'power',    '/icons/power-box.svg',     768,  32, 32, 1, 1, 8,  'system', NOW(), '', NOW(), 0),
(9,  '充电桩',    'power',    '/icons/charging-pile.svg', 768,  32, 32, 1, 1, 9,  'system', NOW(), '', NOW(), 0),
(10, '停车位',    'custom',   '/icons/parking.svg',       512,  32, 32, 1, 1, 10, 'system', NOW(), '', NOW(), 0),
(11, '绿化',      'custom',   '/icons/greenery.svg',      512,  32, 32, 1, 1, 11, 'system', NOW(), '', NOW(), 0),
(12, '路灯',      'custom',   '/icons/street-light.svg',  512,  32, 32, 1, 1, 12, 'system', NOW(), '', NOW(), 0);

-- -----------------------------------------------------------
-- 10. 点位样式（1个默认样式）
-- -----------------------------------------------------------
INSERT INTO `twin_point_style` (`id`, `style_name`, `icon_type`, `size`, `color`, `opacity`, `glow_color`, `glow_intensity`, `breath_speed`, `warn_color`, `warn_flash_freq`, `alarm_color`, `alarm_flash_freq`, `offline_color`, `offline_opacity`, `hover_scale`, `hover_highlight`, `hover_tooltip`, `is_default`, `create_time`, `update_time`, `deleted`) VALUES
(1, '默认样式', '', 32, '#00E5FF', 1.00, '#00E5FF', 50, 3, '#FFA500', 2, '#FF3333', 4, '#666666', 0.40, 1.30, 1, 1, 1, NOW(), NOW(), 0);

-- -----------------------------------------------------------
-- 11. 底图管理（5个默认底图）
-- -----------------------------------------------------------
INSERT INTO `twin_base_map` (`id`, `map_name`, `map_type`, `bind_id`, `file_url`, `file_size`, `width`, `height`, `show_grid`, `status`, `sort_order`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES
(1, '园区总览底图',   'campus',   NULL, '/maps/campus-overview.svg',   204800, 1920, 1080, 1, 1, 1, '园区整体鸟瞰底图',   'admin', NOW(), 'admin', NOW(), 0),
(2, 'A栋办公楼底图', 'building', 1,    '/maps/building-a-floor1.svg',  102400, 800,  600,  1, 1, 2, 'A栋1层平面底图',     'admin', NOW(), 'admin', NOW(), 0),
(3, 'B栋办公楼底图', 'building', 2,    '/maps/building-b-floor1.svg',  102400, 800,  600,  1, 1, 3, 'B栋1层平面底图',     'admin', NOW(), 'admin', NOW(), 0),
(4, 'C栋研发楼底图', 'building', 3,    '/maps/building-c-floor1.svg',  102400, 800,  600,  1, 1, 4, 'C栋1层平面底图',     'admin', NOW(), 'admin', NOW(), 0),
(5, '地下车库底图',   'floor',    NULL, '/maps/underground-parking.svg', 153600, 1200, 800, 1, 1, 5, '地下车库平面底图',   'admin', NOW(), 'admin', NOW(), 0);

-- -----------------------------------------------------------
-- 12. 配置版本（4个示例版本）
-- -----------------------------------------------------------
INSERT INTO `twin_config_version` (`id`, `version_no`, `config_snapshot`, `publish_status`, `publish_time`, `publish_by`, `rollback_time`, `rollback_by`, `change_log`, `check_result`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`) VALUES
(1, 'v1.0.0', '{"baseMapId":1,"points":[],"layers":[]}', 1, '2026-04-20 10:00:00', 'admin', NULL, NULL, '初始版本：园区总览底图配置', '{"status":"passed","errors":[]}', 1, 'admin', '2026-04-20 10:00:00', 'admin', NOW(), 0),
(2, 'v1.1.0', '{"baseMapId":1,"points":[{"id":1,"name":"A栋","x":35,"y":40}],"layers":[]}', 1, '2026-04-21 14:30:00', 'admin', NULL, NULL, '新增A栋建筑点位', '{"status":"passed","errors":[]}', 1, 'admin', '2026-04-21 14:30:00', 'admin', NOW(), 0),
(3, 'v1.2.0', '{"baseMapId":1,"points":[{"id":1,"name":"A栋","x":35,"y":40},{"id":2,"name":"B栋","x":60,"y":45}],"layers":[]}', 1, '2026-04-22 09:15:00', 'admin', NULL, NULL, '新增B栋建筑点位', '{"status":"passed","errors":[]}', 1, 'admin', '2026-04-22 09:15:00', 'admin', NOW(), 0),
(4, 'v2.0.0', '{"baseMapId":1,"points":[{"id":1,"name":"A栋","x":35,"y":40},{"id":2,"name":"B栋","x":60,"y":45},{"id":3,"name":"C栋","x":50,"y":25}],"layers":[{"name":"building","visible":true}]}', 0, NULL, NULL, NULL, NULL, '开发中：新增C栋和图层配置', NULL, 1, 'admin', '2026-04-23 08:00:00', 'admin', NOW(), 0);

-- -----------------------------------------------------------
-- 13. 配置操作日志（示例日志）
-- -----------------------------------------------------------
INSERT INTO `twin_config_log` (`id`, `module`, `action_type`, `action_detail`, `target_id`, `target_name`, `operator`, `operator_ip`, `create_time`) VALUES
(1, 'basemap',  'upload',   '{"mapName":"园区总览底图","fileUrl":"/maps/campus-overview.svg"}',       1, '园区总览底图',   'admin', '127.0.0.1', '2026-04-20 09:50:00'),
(2, 'config',   'publish',  '{"versionNo":"v1.0.0"}',                                              1, 'v1.0.0',        'admin', '127.0.0.1', '2026-04-20 10:00:00'),
(3, 'point',    'create',   '{"pointName":"A栋","x":35,"y":40}',                                    1, 'A栋',           'admin', '127.0.0.1', '2026-04-21 14:20:00'),
(4, 'config',   'publish',  '{"versionNo":"v1.1.0"}',                                              2, 'v1.1.0',        'admin', '127.0.0.1', '2026-04-21 14:30:00'),
(5, 'point',    'create',   '{"pointName":"B栋","x":60,"y":45}',                                    2, 'B栋',           'admin', '127.0.0.1', '2026-04-22 09:10:00'),
(6, 'config',   'publish',  '{"versionNo":"v1.2.0"}',                                              3, 'v1.2.0',        'admin', '127.0.0.1', '2026-04-22 09:15:00'),
(7, 'point',    'create',   '{"pointName":"C栋","x":50,"y":25}',                                    3, 'C栋',           'admin', '127.0.0.1', '2026-04-23 07:50:00'),
(8, 'config',   'update',   '{"versionNo":"v2.0.0","changeLog":"新增C栋和图层配置"}',                4, 'v2.0.0',        'admin', '127.0.0.1', '2026-04-23 08:00:00');

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 初始化完成
-- 默认管理员账号: admin / admin123
-- ============================================================
