-- ============================================================
-- 身份等级升级体系 - 数据库变更脚本
-- 创建时间: 2026-03-20
-- ============================================================

-- ------------------------------------------------------------
-- 1. 改造 la_distribution_level（身份等级表）
-- ------------------------------------------------------------
ALTER TABLE `la_distribution_level`
    ADD COLUMN `level_code`        varchar(32)          NOT NULL DEFAULT '' COMMENT '等级标识码 如 VIP1/VIP2' AFTER `name`,
    ADD COLUMN `direct_ratio`      float unsigned       NOT NULL DEFAULT '0' COMMENT '直推佣金比例(%)' AFTER `second_ratio`,
    ADD COLUMN `repurchase_ratio`  float unsigned       NOT NULL DEFAULT '0' COMMENT '复购佣金比例(%)' AFTER `direct_ratio`,
    ADD COLUMN `price_diff_ratio`  float unsigned       NOT NULL DEFAULT '0' COMMENT '价差佣金比例(%)' AFTER `repurchase_ratio`,
    ADD COLUMN `cultivate_ratio`   float unsigned       NOT NULL DEFAULT '0' COMMENT '培育佣金比例(%)' AFTER `price_diff_ratio`;


-- ------------------------------------------------------------
-- 2. 扩展 la_user（用户表）
-- ------------------------------------------------------------
ALTER TABLE `la_user`
    ADD COLUMN `identity_level`   int(11) unsigned     NOT NULL DEFAULT '0' COMMENT '身份等级ID，关联 la_distribution_level.id' AFTER `inviter_id`,
    ADD COLUMN `frozen_balance`   decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '冻结余额' AFTER `earnings`,
    ADD COLUMN `total_sales`      decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '累计销售额' AFTER `frozen_balance`;


-- ------------------------------------------------------------
-- 3. 扩展 la_order（订单表）
-- ------------------------------------------------------------
ALTER TABLE `la_order`
    ADD COLUMN `region_id`          int(10) unsigned     NOT NULL DEFAULT '0' COMMENT '订单归属区域ID，关联 la_dev_region.id' AFTER `order_type`,
    ADD COLUMN `region_agent_id`    int(10) unsigned     NOT NULL DEFAULT '0' COMMENT '区域代理用户ID，关联 la_user.id' AFTER `region_id`,
    ADD COLUMN `is_repurchase`      tinyint(1) unsigned  NOT NULL DEFAULT '0' COMMENT '是否复购: [0=否, 1=是]' AFTER `region_agent_id`,
    ADD COLUMN `commission_settled` tinyint(1) unsigned  NOT NULL DEFAULT '0' COMMENT '佣金是否已结算: [0=否, 1=是]' AFTER `is_repurchase`;


-- ------------------------------------------------------------
-- 4. 扩展 la_goods（商品表）
-- ------------------------------------------------------------
ALTER TABLE `la_goods`
    ADD COLUMN `is_package` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否身份套餐商品: [0=否, 1=是]' AFTER `is_activity`;


-- ------------------------------------------------------------
-- 5. 新建 la_identity_package（身份套餐定义表）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `la_identity_package`;
CREATE TABLE `la_identity_package` (
    `id`          int(11) unsigned    NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `goods_id`    int(11) unsigned    NOT NULL DEFAULT '0' COMMENT '关联商品ID la_goods.id',
    `level_id`    int(11) unsigned    NOT NULL DEFAULT '0' COMMENT '购买后升级到的等级ID la_distribution_level.id',
    `is_renew`    tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否续费包: [0=首购, 1=续费]',
    `sort`        int(11) unsigned    NOT NULL DEFAULT '0' COMMENT '排序',
    `status`      tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '状态: [0=下架, 1=上架]',
    `create_time` int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time` int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '更新时间',
    `delete_time` int(10) unsigned    NOT NULL DEFAULT '0' COMMENT '删除时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_goods_id` (`goods_id`) USING BTREE,
    KEY `idx_level_id` (`level_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='身份套餐定义表';


-- ------------------------------------------------------------
-- 6. 新建 la_commission_record（分佣记录表）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `la_commission_record`;
CREATE TABLE `la_commission_record` (
    `id`               int(11) unsigned      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`          int(11) unsigned      NOT NULL DEFAULT '0' COMMENT '获佣用户ID',
    `from_user_id`     int(11) unsigned      NOT NULL DEFAULT '0' COMMENT '消费用户ID',
    `order_id`         int(11) unsigned      NOT NULL DEFAULT '0' COMMENT '来源订单ID la_order.id',
    `order_goods_id`   int(11) unsigned      NOT NULL DEFAULT '0' COMMENT '来源订单商品ID la_order_goods.id',
    `commission_type`  tinyint(3) unsigned   NOT NULL DEFAULT '1' COMMENT '佣金类型: [1=直推, 2=复购, 3=价差, 4=培育, 5=区域分红]',
    `level_id`         int(11) unsigned      NOT NULL DEFAULT '0' COMMENT '获佣时的等级ID',
    `ratio`            decimal(5,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '佣金比例(%)',
    `goods_money`      decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '商品金额',
    `commission_money` decimal(10,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '佣金金额',
    `status`           tinyint(1) unsigned   NOT NULL DEFAULT '0' COMMENT '状态: [0=待结算, 1=已结算, 2=已取消]',
    `settle_time`      int(10) unsigned      NOT NULL DEFAULT '0' COMMENT '结算时间',
    `remark`           varchar(255)          NOT NULL DEFAULT '' COMMENT '备注',
    `create_time`      int(10) unsigned      NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time`      int(10) unsigned      NOT NULL DEFAULT '0' COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_user_id`   (`user_id`) USING BTREE,
    KEY `idx_order_id`  (`order_id`) USING BTREE,
    KEY `idx_status`    (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分佣记录表';


-- ------------------------------------------------------------
-- 7. 新建 la_region_agent（区域代理表）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `la_region_agent`;
CREATE TABLE `la_region_agent` (
    `id`           int(11) unsigned      NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`      int(11) unsigned      NOT NULL DEFAULT '0' COMMENT '代理用户ID la_user.id',
    `region_id`    int(10)               NOT NULL DEFAULT '0' COMMENT '区域ID la_dev_region.id',
    `region_level` tinyint(1) unsigned   NOT NULL DEFAULT '2' COMMENT '区域级别: [1=省, 2=市, 3=区县]',
    `ratio`        decimal(5,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '区域分红比例(%)',
    `expire_time`  int(10) unsigned      NOT NULL DEFAULT '0' COMMENT '到期时间，0=永久',
    `status`       tinyint(1) unsigned   NOT NULL DEFAULT '1' COMMENT '状态: [0=禁用, 1=启用]',
    `create_time`  int(10) unsigned      NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time`  int(10) unsigned      NOT NULL DEFAULT '0' COMMENT '更新时间',
    `delete_time`  int(10) unsigned      NOT NULL DEFAULT '0' COMMENT '删除时间',
    PRIMARY KEY (`id`) USING BTREE,
    KEY `idx_user_id`   (`user_id`) USING BTREE,
    KEY `idx_region_id` (`region_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域代理表';


-- ------------------------------------------------------------
-- 8. 新建 la_dividend_pool（分红池月度记录表）
-- ------------------------------------------------------------
DROP TABLE IF EXISTS `la_dividend_pool`;
CREATE TABLE `la_dividend_pool` (
    `id`           int(11) unsigned       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `period`       varchar(7)             NOT NULL DEFAULT '' COMMENT '期次，格式 YYYY-MM',
    `total_sales`  decimal(15,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '当期总销售额',
    `pool_ratio`   decimal(5,2) unsigned  NOT NULL DEFAULT '0.00' COMMENT '分红池提取比例(%)',
    `pool_money`   decimal(15,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '分红池金额',
    `status`       tinyint(1) unsigned    NOT NULL DEFAULT '0' COMMENT '状态: [0=待分配, 1=已分配]',
    `settle_time`  int(10) unsigned       NOT NULL DEFAULT '0' COMMENT '分配时间',
    `create_time`  int(10) unsigned       NOT NULL DEFAULT '0' COMMENT '创建时间',
    `update_time`  int(10) unsigned       NOT NULL DEFAULT '0' COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_period` (`period`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分红池月度记录表';
