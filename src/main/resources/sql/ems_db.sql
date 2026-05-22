/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50730 (5.7.30-log)
 Source Host           : localhost:3306
 Source Schema         : ems_db

 Target Server Type    : MySQL
 Target Server Version : 50730 (5.7.30-log)
 File Encoding         : 65001

 Date: 19/06/2025 16:29:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for emp
-- ----------------------------
DROP TABLE IF EXISTS `emp`;
CREATE TABLE `emp`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '员工Id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '工号',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电子邮箱',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `id_card` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `birthday` date NULL DEFAULT NULL COMMENT '出生日期',
  `sex` int(11) NULL DEFAULT 0 COMMENT '性别(0-未知 1-男 2-女)',
  `nation_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '民族',
  `political_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `native_place` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '籍贯',
  `graduate_school` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '毕业院校',
  `major_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专业',
  `highest_education_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最高学历',
  `highest_degree_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最高学位',
  `habitation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '现居住地',
  `marital_status` int(11) NULL DEFAULT 0 COMMENT '婚姻状态(0-未知 1-未婚 2-已婚 3-离异 )',
  `entry_date` date NULL DEFAULT NULL COMMENT '入职日期',
  `leave_date` date NULL DEFAULT NULL COMMENT '离职日期',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '任职部门Id',
  `post_level_id` bigint(20) NULL DEFAULT NULL COMMENT '岗位岗级Id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '绑定用户Id',
  `role_type` int(11) NULL DEFAULT NULL COMMENT '角色类型',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态(0-在职 1-离职)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code`) USING BTREE,
  UNIQUE INDEX `uk_id_card`(`id_card`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emp
-- ----------------------------
INSERT INTO `emp` VALUES (1, '张三', 'EMP001', 'zhangsan@example.com', '13800138001', '110101199001011234', '1990-01-01', 1, '01', '01', '四川省', '北京大学', 'CS', '01', '01', '吉林省', 1, '2020-01-01', NULL, 1, 1, 1, 1, 0);
INSERT INTO `emp` VALUES (2, '李四', 'EMP002', 'lisi@example.com', '13800138002', '110101199002022345', '1990-02-02', 1, '01', '02', '河北省', '复旦大学', 'EE', '02', '02', '河南省', 2, '2020-02-01', NULL, 2, 2, 2, 1, 0);
INSERT INTO `emp` VALUES (3, '王五', 'EMP003', 'wangwu@example.com', '13800138003', '110101199003033456', '1990-03-03', 1, '02', '01', '江苏省', '中山大学', 'ME', '03', '03', '湖北省', 1, '2020-03-01', NULL, 3, 3, 3, 2, 0);
INSERT INTO `emp` VALUES (4, '赵六', 'EMP004', 'zhaoliu@example.com', '13800138004', '110101199004044567', '1990-04-04', 2, '01', '02', '湖北省', '深圳大学', 'CS', '01', '01', '河南省', 2, '2020-04-01', '2025-06-14', 4, 4, 4, 1, 1);
INSERT INTO `emp` VALUES (5, '钱七', 'EMP005', 'qianqi@example.com', '13800138005', '110101199005055678', '1990-05-05', 2, '02', '01', '河南省', '浙江大学', 'EE', '02', '02', '江西省', 1, '2020-05-01', NULL, 5, 5, 5, 2, 0);

-- ----------------------------
-- Table structure for emp_attendance_record
-- ----------------------------
DROP TABLE IF EXISTS `emp_attendance_record`;
CREATE TABLE `emp_attendance_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '考勤记录Id',
  `user_id` bigint(20) NOT NULL COMMENT '打卡人Id',
  `attendance_date` date NOT NULL COMMENT '考勤日期',
  `check_in_time` time NULL DEFAULT NULL COMMENT '签到时间',
  `check_out_time` time NULL DEFAULT NULL COMMENT '签退时间',
  `state` int(11) NULL DEFAULT 0 COMMENT '考勤状态(0-正常 1-迟到 2-早退 3-迟到且早退)',
  `department_id` bigint(20) NULL DEFAULT NULL COMMENT '部门Id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_date`(`user_id`, `attendance_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 170 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工考勤记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emp_attendance_record
-- ----------------------------
INSERT INTO `emp_attendance_record` VALUES (135, 1, '2025-06-19', '08:30:00', '18:00:00', 0, 1, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (136, 2, '2025-06-19', '09:00:00', '17:00:00', 1, 2, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (137, 3, '2025-06-19', '08:45:00', '17:30:00', 2, 3, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (138, 4, '2025-06-19', '08:50:00', '18:10:00', 3, 1, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (139, 5, '2025-06-19', '09:10:00', '17:20:00', 0, 2, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (140, 1, '2025-06-18', '08:30:00', '18:00:00', 1, 1, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (141, 2, '2025-06-18', '09:00:00', '17:00:00', 0, 2, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (142, 3, '2025-06-18', '08:45:00', '17:30:00', 3, 3, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (143, 4, '2025-06-18', '08:50:00', '18:10:00', 2, 1, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (144, 5, '2025-06-18', '09:10:00', '17:20:00', 1, 2, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (145, 1, '2025-06-17', '08:30:00', '18:00:00', 2, 1, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (146, 2, '2025-06-17', '09:00:00', '17:00:00', 3, 2, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (147, 3, '2025-06-17', '08:45:00', '17:30:00', 0, 3, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (148, 4, '2025-06-17', '08:50:00', '18:10:00', 1, 1, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (149, 5, '2025-06-17', '09:10:00', '17:20:00', 2, 2, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (150, 1, '2025-06-16', '08:30:00', '18:00:00', 3, 1, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (151, 2, '2025-06-16', '09:00:00', '17:00:00', 2, 2, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (152, 3, '2025-06-16', '08:45:00', '17:30:00', 1, 3, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (153, 4, '2025-06-16', '08:50:00', '18:10:00', 0, 1, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (154, 5, '2025-06-16', '09:10:00', '17:20:00', 3, 2, '2025-06-19 11:59:43', '2025-06-19 11:59:43');
INSERT INTO `emp_attendance_record` VALUES (155, 1, '2025-06-15', '08:30:00', '18:00:00', 0, 1, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (156, 2, '2025-06-15', '09:00:00', '17:00:00', 1, 2, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (157, 3, '2025-06-15', '08:45:00', '17:30:00', 2, 3, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (158, 4, '2025-06-15', '08:50:00', '18:10:00', 3, 1, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (159, 5, '2025-06-15', '09:10:00', '17:20:00', 0, 2, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (160, 1, '2025-06-14', '08:30:00', '18:00:00', 1, 1, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (161, 2, '2025-06-14', '09:00:00', '17:00:00', 0, 2, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (162, 3, '2025-06-14', '08:45:00', '17:30:00', 3, 3, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (163, 4, '2025-06-14', '08:50:00', '18:10:00', 2, 1, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (164, 5, '2025-06-14', '09:10:00', '17:20:00', 1, 2, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (165, 1, '2025-06-13', '08:30:00', '18:00:00', 2, 1, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (166, 2, '2025-06-13', '09:00:00', '17:00:00', 3, 2, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (167, 3, '2025-06-13', '08:45:00', '17:30:00', 0, 3, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (168, 4, '2025-06-13', '08:50:00', '18:10:00', 1, 1, '2025-06-19 12:01:36', '2025-06-19 12:01:36');
INSERT INTO `emp_attendance_record` VALUES (169, 5, '2025-06-13', '09:10:00', '17:20:00', 2, 2, '2025-06-19 12:01:36', '2025-06-19 12:01:36');

-- ----------------------------
-- Table structure for emp_leave_record
-- ----------------------------
DROP TABLE IF EXISTS `emp_leave_record`;
CREATE TABLE `emp_leave_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '请假记录Id',
  `user_id` bigint(20) NOT NULL COMMENT '请假人Id',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `leave_start_time` datetime NOT NULL COMMENT '请假开始时间',
  `leave_end_time` datetime NOT NULL COMMENT '请假结束时间',
  `leave_reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假事由',
  `leave_type_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请假类型',
  `approve_user_id` bigint(20) NULL DEFAULT NULL COMMENT '审批人Id',
  `approve_time` datetime NULL DEFAULT NULL COMMENT '审批时间',
  `approve_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批意见',
  `status` int(11) NULL DEFAULT 0 COMMENT '审批状态(0-待提交 1-待审核 2-审批通过 3-驳回 9-销假)',
  `real_end_time` datetime NULL DEFAULT NULL COMMENT '销假时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工请假记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emp_leave_record
-- ----------------------------
INSERT INTO `emp_leave_record` VALUES (2, 6, '2023-01-01 11:00:00', '2023-01-02 16:00:00', '2023-01-03 16:00:00', '事假', '02', 1, '2025-06-19 16:16:21', '11', 1, NULL);
INSERT INTO `emp_leave_record` VALUES (3, 3, '2023-01-01 12:00:00', '2023-01-04 00:00:00', '2023-01-05 00:00:00', '年假', '03', 6, '2023-01-01 13:00:00', '同意', 2, NULL);
INSERT INTO `emp_leave_record` VALUES (4, 6, '2023-01-01 13:00:00', '2023-01-04 16:00:00', '2023-01-05 16:00:00', '病假1', '01', 1, '2025-06-19 16:16:17', '1', 2, NULL);
INSERT INTO `emp_leave_record` VALUES (5, 5, '2023-01-01 14:00:00', '2023-01-06 00:00:00', '2023-01-07 00:00:00', '事假', '02', 6, '2023-01-01 15:00:00', '同意', 9, NULL);
INSERT INTO `emp_leave_record` VALUES (6, 5, '2025-06-18 22:29:34', '2025-06-13 22:29:16', '2025-06-18 22:28:13', '病假', '01', 6, '2025-06-28 22:28:37', '同意', 2, NULL);
INSERT INTO `emp_leave_record` VALUES (9, 1, NULL, '2025-06-09 16:16:00', '2025-06-29 16:23:00', '11', '03', 1, '2025-06-19 16:26:32', '1', 2, NULL);

-- ----------------------------
-- Table structure for emp_post_level
-- ----------------------------
DROP TABLE IF EXISTS `emp_post_level`;
CREATE TABLE `emp_post_level`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位岗级Id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `level` int(11) NULL DEFAULT NULL COMMENT '岗级',
  `salary` decimal(10, 2) NULL DEFAULT NULL COMMENT '岗位工资',
  `sort_code` int(11) NULL DEFAULT NULL COMMENT '排序码',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态(0-正常 1-禁用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位岗级表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emp_post_level
-- ----------------------------
INSERT INTO `emp_post_level` VALUES (1, '初级工程师', 1, 5000.00, 1, 0);
INSERT INTO `emp_post_level` VALUES (2, '中级工程师', 2, 8000.00, 2, 0);
INSERT INTO `emp_post_level` VALUES (3, '高级工程师', 3, 12000.00, 3, 0);
INSERT INTO `emp_post_level` VALUES (4, '项目经理', 4, 15000.00, 4, 0);
INSERT INTO `emp_post_level` VALUES (5, '技术总监', 5, 20000.00, 5, 0);
INSERT INTO `emp_post_level` VALUES (6, '管理员', 6, 0.00, 6, 0);

-- ----------------------------
-- Table structure for emp_salary
-- ----------------------------
DROP TABLE IF EXISTS `emp_salary`;
CREATE TABLE `emp_salary`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `emp_id` bigint(20) NOT NULL COMMENT '员工ID',
  `salary_year_month` varchar(7) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '薪资年月，格式YYYY-MM',
  `base_salary` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '基本工资',
  `post_salary` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '岗位工资',
  `performance_salary` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '绩效工资',
  `overtime_salary` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '加班工资',
  `allowance` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '补贴',
  `bonus` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '奖金',
  `deduction` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '扣款',
  `actual_salary` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '实发工资',
  `status` int(11) NULL DEFAULT 0 COMMENT '发放状态(0-未发放 1-已发放)',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '发放时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_emp_id_salary_year_month`(`emp_id`, `salary_year_month`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工薪资表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emp_salary
-- ----------------------------
INSERT INTO `emp_salary` VALUES (1, 1, '2024-01', 5000.00, 2000.00, 1000.00, 500.00, 300.00, 1000.00, 200.00, 9600.00, 1, '2024-01-15 10:00:00', '1月工资', '2025-06-17 09:28:42', '2025-06-17 09:28:42');
INSERT INTO `emp_salary` VALUES (2, 2, '2024-01', 6000.00, 2500.00, 1200.00, 600.00, 400.00, 1200.00, 300.00, 11600.00, 1, '2024-01-15 10:00:00', '1月工资', '2025-06-17 09:28:42', '2025-06-17 09:28:42');
INSERT INTO `emp_salary` VALUES (3, 3, '2024-01', 7000.00, 3000.00, 1500.00, 700.00, 500.00, 1500.00, 400.00, 13800.00, 1, '2024-01-15 10:00:00', '1月工资', '2025-06-17 09:28:42', '2025-06-17 09:28:42');
INSERT INTO `emp_salary` VALUES (4, 4, '2024-01', 8000.00, 3500.00, 1800.00, 800.00, 600.00, 1800.00, 500.00, 16000.00, 1, '2024-01-15 10:00:00', '1月工资', '2025-06-17 09:28:42', '2025-06-17 09:28:42');
INSERT INTO `emp_salary` VALUES (5, 5, '2024-01', 9000.00, 4000.00, 2000.00, 900.00, 700.00, 2000.00, 600.00, 18000.00, 1, '2024-01-15 10:00:00', '1月工资', '2025-06-17 09:28:42', '2025-06-17 09:28:42');
INSERT INTO `emp_salary` VALUES (6, 1, '2024-02', 5000.00, 2000.00, 1000.00, 500.00, 300.00, 1000.00, 200.00, 9600.00, 1, '2024-02-15 10:00:00', '2月工资', '2025-06-18 10:00:00', '2025-06-18 10:00:00');
INSERT INTO `emp_salary` VALUES (7, 2, '2024-02', 6000.00, 2500.00, 1200.00, 600.00, 400.00, 1200.00, 300.00, 11600.00, 1, '2024-02-15 10:00:00', '2月工资', '2025-06-18 10:00:00', '2025-06-18 10:00:00');
INSERT INTO `emp_salary` VALUES (8, 1, '2024-03', 5000.00, 2000.00, 1000.00, 500.00, 300.00, 1000.00, 200.00, 9600.00, 1, '2024-03-15 10:00:00', '3月工资', '2025-06-18 10:00:00', '2025-06-18 10:00:00');
INSERT INTO `emp_salary` VALUES (9, 2, '2024-03', 6000.00, 2500.00, 1200.00, 600.00, 400.00, 1200.00, 300.00, 11600.00, 1, '2024-03-15 10:00:00', '3月工资', '2025-06-18 10:00:00', '2025-06-18 10:00:00');
INSERT INTO `emp_salary` VALUES (10, 3, '2024-04', 7000.00, 3000.00, 1500.00, 700.00, 500.00, 1500.00, 400.00, 13800.00, 1, '2024-04-15 10:00:00', '4月工资', '2025-06-18 10:00:00', '2025-06-18 10:00:00');
INSERT INTO `emp_salary` VALUES (11, 4, '2024-04', 8000.00, 3500.00, 1800.00, 800.00, 600.00, 1800.00, 500.00, 16000.00, 1, '2024-04-15 10:00:00', '4月工资', '2025-06-18 10:00:00', '2025-06-18 10:00:00');
INSERT INTO `emp_salary` VALUES (12, 3, '2024-05', 7000.00, 3000.00, 1500.00, 700.00, 500.00, 1500.00, 400.00, 13800.00, 1, '2024-05-15 10:00:00', '5月工资', '2025-06-18 10:00:00', '2025-06-18 10:00:00');
INSERT INTO `emp_salary` VALUES (13, 4, '2024-05', 8000.00, 3500.00, 1800.00, 800.00, 600.00, 1800.00, 500.00, 16000.00, 1, '2024-05-15 10:00:00', '5月工资', '2025-06-18 10:00:00', '2025-06-18 10:00:00');
INSERT INTO `emp_salary` VALUES (14, 5, '2024-06', 9000.00, 4000.00, 2000.00, 900.00, 700.00, 2000.00, 600.00, 18000.00, 1, '2024-06-15 10:00:00', '6月工资', '2025-06-18 10:00:00', '2025-06-18 10:00:00');
INSERT INTO `emp_salary` VALUES (15, 1, '2024-06', 5000.00, 2000.00, 1000.00, 500.00, 300.00, 1000.00, 200.00, 9600.00, 1, '2024-06-15 10:00:00', '6月工资', '2025-06-18 10:00:00', '2025-06-18 10:00:00');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门Id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `manager_user_id` bigint(20) NULL DEFAULT NULL COMMENT '负责人Id',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级部门Id',
  `sort_code` int(11) NULL DEFAULT NULL COMMENT '排序码',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态(0-正常 1-禁用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, '研发部', 1, 6, 1, 0);
INSERT INTO `sys_dept` VALUES (2, '市场部', 2, 6, 2, 0);
INSERT INTO `sys_dept` VALUES (3, '财务部', 3, 6, 3, 0);
INSERT INTO `sys_dept` VALUES (4, '人事部', 4, 6, 4, 0);
INSERT INTO `sys_dept` VALUES (5, '行政部', 5, 6, 5, 0);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典项Id',
  `dict_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类别',
  `dict_item_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项编码',
  `dict_item_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典项值',
  `sort_code` int(11) NULL DEFAULT NULL COMMENT '排序码',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态(0-正常 1-禁用)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type_key`(`dict_type`, `dict_item_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统字典' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, '性别', '1', '男', 1, 0);
INSERT INTO `sys_dict` VALUES (2, '性别', '2', '女', 2, 0);
INSERT INTO `sys_dict` VALUES (3, '婚姻状态', '1', '未婚', 1, 0);
INSERT INTO `sys_dict` VALUES (4, '婚姻状态', '2', '已婚', 2, 0);
INSERT INTO `sys_dict` VALUES (5, '婚姻状态', '3', '离异', 3, 0);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户Id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `account` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录账户',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录密码',
  `status` int(11) NULL DEFAULT 0 COMMENT '状态(0-正常 1-禁用)',
  `role_type` int(11) NULL DEFAULT 1 COMMENT '角色类型（1-普通员工 2-主管 3-管理员）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_account`(`account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '张三', 'user1', '123456', 0, 1);
INSERT INTO `sys_user` VALUES (2, '李四', 'user2', '123456', 0, 1);
INSERT INTO `sys_user` VALUES (3, '王五', 'user3', '123456', 0, 1);
INSERT INTO `sys_user` VALUES (4, '赵六', 'user4', '123456', 0, 1);
INSERT INTO `sys_user` VALUES (5, '孙七', 'user5', '123456', 0, 1);
INSERT INTO `sys_user` VALUES (6, 'admin', 'admin', '123456', 0, 3);

SET FOREIGN_KEY_CHECKS = 1;
