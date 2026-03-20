ALTER TABLE `la_withdraw_apply`
ADD `package_info` varchar(191) COLLATE 'utf8mb4_general_ci' NOT NULL DEFAULT '' COMMENT '微信支付收款页的package信息';

ALTER TABLE `la_withdraw_apply`
ADD `sub_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '子状态: 0-无需处理 1-微信待用户收款 2. 微信用户收款操作完成,待微信回调 3.已收微信回调';

ALTER TABLE `la_withdraw_apply`
ADD `wx_transfer_time` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '微信开始转账时间';

INSERT INTO `la_notice_setting` (`scene`, `name`, `remarks`, `recipient`, `type`, `system_notice`, `sms_notice`, `oa_notice`, `mnp_notice`, `is_delete`, `create_time`, `update_time`, `delete_time`)
SELECT '109', '确认收款通知', '确认收款通知', '1', '1', '{}', '{\"type\":\"sms\",\"templateId\":\" \",\"content\":\"${nickname}您好！您申请的提现已通过，金额${withdraw_money},请前往商城提现记录中确认收款\",\"tips\":\"[\\\"可选变量 昵称:nickname: 提现金额:withdraw_money\\\",\\\"示例：${nickname}您好！您申请的提现已通过，金额${withdraw_money},请前往商城提现记录中确认收款\\\"]\",\"status\":\"0\"}', '{}', '{}', '0', '1648696695', '1683536634', '0'
FROM `la_notice_setting`
WHERE ((`id` = '8'));


ALTER TABLE `la_withdraw_apply`
ADD `wx_transfer_fail_reason` varchar(191) COLLATE 'utf8mb4_general_ci' NOT NULL DEFAULT '' COMMENT '微信转账失败原因';

SET NAMES utf8mb4;

INSERT INTO `la_system_auth_menu` (`id`, `pid`, `menu_type`, `menu_name`, `menu_icon`, `menu_sort`, `perms`, `paths`, `component`, `selected`, `params`, `is_cache`, `is_show`, `is_disable`, `create_time`, `update_time`) VALUES
(954,	953,	'A',	'保存',	'',	0,	'deliver:config:setLogisticsConfig',	'',	'',	'',	'',	0,	1,	0,	1681094581,	1681094581),
(953,	800,	'C',	'物流接口',	'',	0,	'deliver:config:getLogisticsConfig',	'delivery',	'setting/delivery/index',	'',	'',	0,	1,	0,	1681094526,	1681094526),
(952,	804,	'A',	'删除',	'',	0,	'deliver:express:del',	'',	'',	'',	'',	0,	1,	0,	1681094581,	1681094581),
(951,	816,	'A',	'保存',	'',	0,	'setting:trade:save',	'',	'',	'',	'',	0,	1,	0,	1693793119,	1693793119),
(950,	710,	'A',	'保存',	'',	0,	'channel:mp:save',	'',	'',	'',	'',	0,	1,	0,	1693793119,	1693793119),
(949,	879,	'A',	'保存',	'',	0,	'setting:withdraw:save',	'',	'',	'',	'',	0,	1,	0,	1693793119,	1693793119);
