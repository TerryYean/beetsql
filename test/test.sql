/*
Navicat MySQL Data Transfer

Source Server         : sue
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2015-08-14 09:42:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `age` int(4) DEFAULT NULL,
  `userName` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'name1', '11', 'username1');
INSERT INTO `user` VALUES ('2', 'name2', '12', 'username2');
INSERT INTO `user` VALUES ('3', 'name3', '13', 'username3');

-- ----------------------------
-- Table structure for user1
-- ----------------------------
DROP TABLE IF EXISTS `user1`;
CREATE TABLE `user1` (
  `id` int(11) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `age` int(64) DEFAULT NULL,
  `sex` char(2) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `deletedate` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user1
-- ----------------------------
INSERT INTO `user1` VALUES ('1', 'name1', '11', 'M', '2015-08-09 14:07:28', '2015-08-09 14:07:30');
INSERT INTO `user1` VALUES ('2', 'name2', '12', 'F', '2015-08-09 14:07:31', '2015-08-09 14:07:35');

-- ----------------------------
-- Table structure for user2
-- ----------------------------
DROP TABLE IF EXISTS `user2`;
CREATE TABLE `user2` (
  `id` int(11) NOT NULL,
  `t_name` varchar(64) DEFAULT NULL,
  `t_age` int(64) DEFAULT NULL,
  `t_sex` char(2) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `delete_date` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user2
-- ----------------------------
INSERT INTO `user2` VALUES ('1', 'name1', '11', 'M', '2015-08-12 14:11:46', '2015-08-09 14:11:48');
INSERT INTO `user2` VALUES ('2', 'name2', '12', 'F', '2015-08-22 14:11:49', '2015-08-20 14:11:56');

-- ----------------------------
-- Table structure for user3
-- ----------------------------
DROP TABLE IF EXISTS `user3`;
CREATE TABLE `user3` (
  `t_varchar` varchar(255) NOT NULL,
  `t_char` char(255) DEFAULT NULL,
  `t_blob` blob,
  `t_tinyblob` tinyblob,
  `t_mediumblob` mediumblob,
  `t_longblob` longblob,
  `t_text` text,
  `t_tinytext` tinytext,
  `t_mediumtext` mediumtext,
  `t_longtext` longtext,
  `t_integer` int(255) DEFAULT NULL,
  `t_tinyint` tinyint(255) DEFAULT NULL,
  `t_smallint` smallint(255) DEFAULT NULL,
  `t_mediumint` mediumint(255) DEFAULT NULL,
  `t_bit` bit(1) DEFAULT NULL,
  `t_bigint` bigint(20) DEFAULT NULL,
  `t_float` float(4,2) DEFAULT NULL,
  `t_double` double(22,0) DEFAULT NULL,
  `t_decimal` decimal(11,0) DEFAULT NULL,
  `t_date` date DEFAULT NULL,
  `t_time` time(6) DEFAULT NULL,
  `t_datetime` datetime DEFAULT NULL,
  `t_timestamp` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `t_year` year(4) DEFAULT NULL,
  PRIMARY KEY (`t_varchar`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user3
-- ----------------------------
INSERT INTO `user3` VALUES ('beetlSql', 'M', null, null, null, null, '这是text类型', '这是tinytext类型', '合适mediumtext类型', '这是longtext类型', '1000000001', '101', '10001', '1000001', '', '1000000000000000001', '12.35', '12', '123456789', '2015-08-20', '20:01:51.000000', '2015-08-18 20:01:56', '2015-08-09 20:02:09', '2015');


-- 测试主键
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1
