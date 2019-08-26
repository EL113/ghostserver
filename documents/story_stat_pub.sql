/*
 Navicat Premium Data Transfer

 Source Server         : crm_local
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 127.0.0.1:3306
 Source Schema         : gstest

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 23/08/2019 16:49:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for story_stat_pub
-- ----------------------------
DROP TABLE IF EXISTS `story_stat_pub`;
CREATE TABLE `story_stat_pub`  (
  `id` int(11) UNSIGNED NOT NULL,
  `stat_date` datetime(0) NOT NULL COMMENT '统计日期',
  `stat_count` int(5) UNSIGNED NOT NULL DEFAULT 0 COMMENT '统计数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
