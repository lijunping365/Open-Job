/*
 Navicat Premium Data Transfer

 Source Server         : open_job
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : open_job

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 17/08/2024 14:07:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for open_job
-- ----------------------------
DROP TABLE IF EXISTS `open_job`;
CREATE TABLE `open_job`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` bigint(20) NOT NULL COMMENT '应用 id',
  `job_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `handler_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '绑定的 handler 的名字',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'cron 表达式',
  `sharding` tinyint(4) NULL DEFAULT 0 COMMENT '是否采用分片执行 0：故障转移， 1：分片执行',
  `params` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `script` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '脚本',
  `script_lang` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '脚本语言：bash、python、php、node、powershell',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '任务执行状态（1 启动，0 停止）',
  `executor_timeout` int(8) NULL DEFAULT NULL COMMENT '任务执行超时时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '任务创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '任务更新时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '任务创建人',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '任务更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '爬虫任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for open_job_alarm_record
-- ----------------------------
DROP TABLE IF EXISTS `open_job_alarm_record`;
CREATE TABLE `open_job_alarm_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) NULL DEFAULT NULL COMMENT '应用 id',
  `job_id` bigint(20) NULL DEFAULT NULL COMMENT '任务 id',
  `server_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行器 id',
  `message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报警消息',
  `receiver` bigint(20) NULL DEFAULT NULL COMMENT '报警接收者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for open_job_app
-- ----------------------------
DROP TABLE IF EXISTS `open_job_app`;
CREATE TABLE `open_job_app`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用名称（英文） ',
  `app_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '应用描述',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_key`(`app_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for open_job_instance
-- ----------------------------
DROP TABLE IF EXISTS `open_job_instance`;
CREATE TABLE `open_job_instance`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) NULL DEFAULT NULL COMMENT '应用 id',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `port` int(8) NULL DEFAULT NULL COMMENT '端口',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态',
  `weight` int(8) NULL DEFAULT NULL COMMENT '权重',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_id`(`app_id`, `address`, `port`) USING BTREE COMMENT '唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for open_job_log
-- ----------------------------
DROP TABLE IF EXISTS `open_job_log`;
CREATE TABLE `open_job_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` bigint(20) NOT NULL COMMENT '应用 id',
  `job_id` bigint(20) NOT NULL COMMENT '任务 id',
  `server_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '调度节点id',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '任务状态',
  `cause` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '失败原因',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '任务启动时间',
  `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '任务完成时间',
  `take_time` bigint(20) NULL DEFAULT NULL COMMENT '任务执行用时，单位为毫秒',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for open_job_prompt
-- ----------------------------
DROP TABLE IF EXISTS `open_job_prompt`;
CREATE TABLE `open_job_prompt`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `prompt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用名称（英文） ',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `update_user` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for open_job_report
-- ----------------------------
DROP TABLE IF EXISTS `open_job_report`;
CREATE TABLE `open_job_report`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) NOT NULL COMMENT '任务 id',
  `task_exec_total_count` bigint(20) NULL DEFAULT NULL COMMENT '执行总次数',
  `task_exec_success_count` bigint(20) NULL DEFAULT NULL COMMENT '执行成功总次数',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for open_job_user
-- ----------------------------
DROP TABLE IF EXISTS `open_job_user`;
CREATE TABLE `open_job_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '用户状态（0：正常，1锁定）',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '爬虫系统用户表' ROW_FORMAT = Dynamic;

INSERT INTO `open_job_user` VALUES (1, 'admin', '$2a$10$3oNlO/vvXV3FPsmimv0x3ePTcwpe/E1xl86TDC0iLKwukWkJoRIyK', '18242076871', 0, '2023-05-22 23:38:02', '2023-05-22 23:38:05');

SET FOREIGN_KEY_CHECKS = 1;
