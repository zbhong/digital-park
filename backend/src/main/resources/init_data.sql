-- ============================================================
-- 数字园区综合管理平台 - 初始数据
-- 数据库: digital_park
-- 说明: 管理员用户、默认角色、用户角色关联、默认组织、字典数据
-- ============================================================

USE `digital_park`;

-- -----------------------------------------------------------
-- 1. 管理员用户
--    username: admin
--    password: BCrypt加密的 admin123
--    nickname: 超级管理员
-- -----------------------------------------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `avatar`, `email`, `phone`, `gender`, `org_id`, `status`, `login_ip`, `login_time`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', '', 'admin@digitalpark.com', '13800000000', 1, 1, 0, '', NULL, '超级管理员', 'admin', NOW(), 'admin', NOW(), 0);

-- -----------------------------------------------------------
-- 2. 默认角色
--    id=1, role_name: 超级管理员, role_code: admin, data_scope: 1
-- -----------------------------------------------------------
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `data_scope`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1, '超级管理员', 'admin', 1, 1, 0, '超级管理员，拥有所有权限', 'admin', NOW(), 'admin', NOW(), 0);

-- -----------------------------------------------------------
-- 3. 用户角色关联
--    user_id=1, role_id=1
-- -----------------------------------------------------------
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1, 1, 1, '管理员关联超级管理员角色', 'admin', NOW(), 'admin', NOW(), 0);

-- -----------------------------------------------------------
-- 4. 默认组织
--    id=1, org_name: 银河数字园区, parent_id=0
-- -----------------------------------------------------------
INSERT INTO `sys_organization` (`id`, `parent_id`, `org_name`, `org_code`, `leader`, `phone`, `email`, `sort_order`, `status`, `remark`, `create_by`, `create_time`, `update_by`, `update_time`, `deleted`)
VALUES (1, 0, '银河数字园区', 'YH_DIGITAL_PARK', '超级管理员', '13800000000', 'admin@digitalpark.com', 1, 1, '园区总部组织', 'admin', NOW(), 'admin', NOW(), 0);

-- -----------------------------------------------------------
-- 5. 字典数据
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
