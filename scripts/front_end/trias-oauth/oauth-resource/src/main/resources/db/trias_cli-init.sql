/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : trias_cli

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2019-07-22 15:53:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for trias_cli_user
-- ----------------------------
DROP TABLE IF EXISTS `trias_cli_user`;
CREATE TABLE `trias_cli_user` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT '',
  `private_key` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_username` (`username`),
  UNIQUE KEY `idx_unique_account` (`address`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trias_cli_user
-- ----------------------------
INSERT INTO `trias_cli_user` VALUES ('0000000001', 'niming_test', '1', null, '0', 'bac@123.com', '2019-05-28 15:43:20', '2019-07-03 16:56:24');
INSERT INTO `trias_cli_user` VALUES ('0000000002', 'niming1', 'qwewqewq123', null, '1', '12312@123.com', '2019-05-29 15:29:41', '2019-05-29 23:15:53');
INSERT INTO `trias_cli_user` VALUES ('0000000003', 'niming2', 'test', null, '0', 'qqq@123.com', '2019-05-30 14:30:55', '2019-07-03 16:55:43');
INSERT INTO `trias_cli_user` VALUES ('0000000004', 'admin', 'testAccount', null, '1', 'new@163.com', '2019-06-12 16:48:21', '2019-06-18 15:49:49');
INSERT INTO `trias_cli_user` VALUES ('0000000005', 'niming', '153kzokHD7WDfd3mtuEjt5w8b1pXZQMVq1', 'KyUXPn6hduF8TPhng8tZTcVHxyAJCqA9Sjt3cYD1oqzURqzEfPSC', '1', 'niming_2009@163.com', '2019-06-13 10:40:54', '2019-07-22 14:35:47');

-- ----------------------------
-- Table structure for trias_resource
-- ----------------------------
DROP TABLE IF EXISTS `trias_resource`;
CREATE TABLE `trias_resource` (
  `id` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `root_name` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL,
  `del_flag` int(1) NOT NULL DEFAULT '0',
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trias_resource
-- ----------------------------
INSERT INTO `trias_resource` VALUES ('0000000001', 'user', '/user/leviatom', '0', null);
INSERT INTO `trias_resource` VALUES ('0000000002', 'user', '/user/streamNet', '0', null);
INSERT INTO `trias_resource` VALUES ('0000000003', 'user', '/user/netCoin', '0', null);
INSERT INTO `trias_resource` VALUES ('0000000004', 'server', '/server/devOps', '0', null);
INSERT INTO `trias_resource` VALUES ('0000000005', 'server', '/server/deployment', '0', null);
INSERT INTO `trias_resource` VALUES ('0000000006', 'server', '/server/experiment', '0', null);
INSERT INTO `trias_resource` VALUES ('0000000007', 'server', '/server/user', '0', '');
INSERT INTO `trias_resource` VALUES ('0000000008', 'management', '/management/userManagement', '0', null);

-- ----------------------------
-- Table structure for trias_role
-- ----------------------------
DROP TABLE IF EXISTS `trias_role`;
CREATE TABLE `trias_role` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `role_type` varchar(255) NOT NULL,
  `description` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trias_role
-- ----------------------------
INSERT INTO `trias_role` VALUES ('00000000001', 'ROLE_ADMIN', null);
INSERT INTO `trias_role` VALUES ('00000000002', 'ROLE_USER', null);

-- ----------------------------
-- Table structure for trias_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `trias_role_resource`;
CREATE TABLE `trias_role_resource` (
  `id` int(11) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `resource_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trias_role_resource
-- ----------------------------
INSERT INTO `trias_role_resource` VALUES ('00000000001', '1', '1');
INSERT INTO `trias_role_resource` VALUES ('00000000002', '1', '2');
INSERT INTO `trias_role_resource` VALUES ('00000000003', '1', '3');
INSERT INTO `trias_role_resource` VALUES ('00000000004', '1', '4');
INSERT INTO `trias_role_resource` VALUES ('00000000005', '1', '5');
INSERT INTO `trias_role_resource` VALUES ('00000000006', '1', '6');
INSERT INTO `trias_role_resource` VALUES ('00000000007', '2', '1');
INSERT INTO `trias_role_resource` VALUES ('00000000008', '2', '2');
INSERT INTO `trias_role_resource` VALUES ('00000000009', '2', '3');
INSERT INTO `trias_role_resource` VALUES ('00000000010', '1', '7');
INSERT INTO `trias_role_resource` VALUES ('00000000011', '1', '8');

-- ----------------------------
-- Table structure for trias_user_role
-- ----------------------------
DROP TABLE IF EXISTS `trias_user_role`;
CREATE TABLE `trias_user_role` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(10) DEFAULT NULL,
  `role_id` int(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of trias_user_role
-- ----------------------------
INSERT INTO `trias_user_role` VALUES ('1', '4', '1');
