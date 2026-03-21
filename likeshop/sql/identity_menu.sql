-- ============================================================
-- 身份等级升级体系 - 后台菜单权限数据
-- 创建时间: 2026-03-21
-- 说明: 在 la_system_auth_menu 中插入身份系统相关菜单
--       ID 从 960 开始，避免与现有菜单冲突（现有最大ID为 954）
--       父级挂载在 应用管理（id=809）下
-- ============================================================

INSERT INTO `la_system_auth_menu`
    (`id`, `pid`, `menu_type`, `menu_name`, `menu_icon`, `menu_sort`, `perms`, `paths`, `component`, `selected`, `params`, `is_cache`, `is_show`, `is_disable`, `create_time`, `update_time`)
VALUES
-- 一级目录：身份系统（挂在应用管理 id=809 下）
(960,  809, 'M', '身份系统',       'el-icon-Medal',         50,  '',                            'identity',           '',                               '', '', 0, 1, 0, 1742515200, 1742515200),

-- 身份套餐管理
(961,  960, 'C', '身份套餐',       '',                       0,   'identity:package:list',       'package',            'identity/package/index',         '', '', 0, 1, 0, 1742515200, 1742515200),
(962,  961, 'A', '新增套餐',       '',                       0,   'identity:package:add',        '',                   '',                               '', '', 0, 1, 0, 1742515200, 1742515200),
(963,  961, 'A', '编辑套餐',       '',                       0,   'identity:package:edit',       '',                   '',                               '', '', 0, 1, 0, 1742515200, 1742515200),
(964,  961, 'A', '删除套餐',       '',                       0,   'identity:package:del',        '',                   '',                               '', '', 0, 1, 0, 1742515200, 1742515200),
(965,  961, 'A', '套餐详情',       '',                       0,   'identity:package:detail',     '',                   '',                               '', '', 0, 1, 0, 1742515200, 1742515200),

-- 分佣记录
(966,  960, 'C', '分佣记录',       '',                       0,   'identity:commission:list',    'commission',         'identity/commission/index',       '', '', 0, 1, 0, 1742515200, 1742515200),

-- 区域代理管理
(967,  960, 'C', '区域代理',       '',                       0,   'identity:regionAgent:list',   'region-agent',       'identity/region_agent/index',     '', '', 0, 1, 0, 1742515200, 1742515200),
(968,  967, 'A', '新增代理',       '',                       0,   'identity:regionAgent:add',    '',                   '',                               '', '', 0, 1, 0, 1742515200, 1742515200),
(969,  967, 'A', '编辑代理',       '',                       0,   'identity:regionAgent:edit',   '',                   '',                               '', '', 0, 1, 0, 1742515200, 1742515200),
(970,  967, 'A', '删除代理',       '',                       0,   'identity:regionAgent:del',    '',                   '',                               '', '', 0, 1, 0, 1742515200, 1742515200),
(971,  967, 'A', '代理详情',       '',                       0,   'identity:regionAgent:detail', '',                   '',                               '', '', 0, 1, 0, 1742515200, 1742515200),

-- 分红池管理
(972,  960, 'C', '分红池',         '',                       0,   'identity:dividendPool:list',  'dividend-pool',      'identity/dividend_pool/index',    '', '', 0, 1, 0, 1742515200, 1742515200),
(973,  972, 'A', '执行分配',       '',                       0,   'identity:dividendPool:settle','',                   '',                               '', '', 0, 1, 0, 1742515200, 1742515200);
