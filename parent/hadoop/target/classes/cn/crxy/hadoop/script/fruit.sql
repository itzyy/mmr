/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.155
Source Server Version : 50173
Source Host           : 192.168.1.155:3306
Source Database       : fruit

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2016-04-05 09:49:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_stat_app_login_tbl
-- ----------------------------
DROP TABLE IF EXISTS `t_stat_app_login_tbl`;
CREATE TABLE `t_stat_app_login_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `appid` int(11) DEFAULT '-1',
  `uv` int(11) DEFAULT '0',
  `pv` int(11) DEFAULT '0',
  `stat_date` date DEFAULT NULL COMMENT '统计时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for t_stat_app_one_remain_tbl
-- ----------------------------
DROP TABLE IF EXISTS `t_stat_app_one_remain_tbl`;
CREATE TABLE `t_stat_app_one_remain_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `appid` int(11) DEFAULT '-1',
  `uv` int(11) DEFAULT '0',
  `stat_date` date DEFAULT NULL COMMENT '统计时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for t_stat_app_reg_tbl
-- ----------------------------
DROP TABLE IF EXISTS `t_stat_app_reg_tbl`;
CREATE TABLE `t_stat_app_reg_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `appid` int(11) DEFAULT '-1',
  `uv` int(11) DEFAULT '0',
  `pv` int(11) DEFAULT '0',
  `stat_date` date DEFAULT NULL COMMENT '统计时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for t_stat_app_seven_remain_tbl
-- ----------------------------
DROP TABLE IF EXISTS `t_stat_app_seven_remain_tbl`;
CREATE TABLE `t_stat_app_seven_remain_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `appid` int(11) DEFAULT '-1',
  `uv` int(11) DEFAULT '0',
  `stat_date` date DEFAULT NULL COMMENT '统计时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for t_stat_app_uv_tbl
-- ----------------------------
DROP TABLE IF EXISTS `t_stat_app_uv_tbl`;
CREATE TABLE `t_stat_app_uv_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `appid` int(11) DEFAULT '-1',
  `uv` int(11) DEFAULT '0',
  `pv` int(11) DEFAULT '0',
  `stat_date` date DEFAULT NULL COMMENT '统计时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for t_view_app_core_stat_tbl
-- ----------------------------
DROP TABLE IF EXISTS `t_view_app_core_stat_tbl`;
CREATE TABLE `t_view_app_core_stat_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `appid` int(11) DEFAULT '-1' COMMENT 'appid web:1000 android 1001 ios 1002 全部:-1',
  `uv` int(11) DEFAULT '0' COMMENT '访问人数',
  `pv` bigint(22) DEFAULT '0' COMMENT '访问次数',
  `login` int(11) DEFAULT '0' COMMENT '登录人数',
  `reg` int(11) DEFAULT '0' COMMENT '注册人数',
  `one_remain` int(11) DEFAULT '0' COMMENT '1日留存',
  `seven_remain` int(11) DEFAULT '0' COMMENT '7日留存',
  `stat_date` date DEFAULT NULL COMMENT '统计时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for t_view_recharge_online_tbl
-- ----------------------------
DROP TABLE IF EXISTS `t_view_recharge_online_tbl`;
CREATE TABLE `t_view_recharge_online_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_num` int(11) DEFAULT '0',
  `rmb` bigint(22) DEFAULT '0',
  `stat_time` datetime DEFAULT NULL COMMENT '统计时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_view_app_recharge_online_tbl
-- ----------------------------
DROP TABLE IF EXISTS `t_view_app_recharge_online_tbl`;
CREATE TABLE `t_view_app_recharge_online_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `appid` int(11) DEFAULT '-1' COMMENT 'appid web:1000 android 1001 ios 1002 全部:-1',
  `user_num` int(11) DEFAULT '0',
  `rmb` bigint(22) DEFAULT '0',
  `stat_time` datetime DEFAULT NULL COMMENT '统计时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;