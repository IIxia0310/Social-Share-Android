/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : sharinglifeapp

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 17/05/2025 19:56:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `admin_id` int(0) NOT NULL AUTO_INCREMENT,
  `admin_user` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `portrait_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 124 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (123, 'Admin123', '123', '1747290415617_temp2507125101942420749.tmp');

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `collect_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '收藏记录唯一标识',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `note_id` bigint(0) NULL DEFAULT NULL COMMENT '被收藏笔记的id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`collect_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES (1, 18278919041, 1, '2024-01-18 03:25:40');
INSERT INTO `collect` VALUES (3, 18278919041, 3, '2025-04-30 01:25:16');
INSERT INTO `collect` VALUES (4, 18278919041, 14, '2025-04-30 16:25:25');
INSERT INTO `collect` VALUES (6, 18278919041, 16, '2025-04-30 20:46:09');
INSERT INTO `collect` VALUES (8, 2, 22, '2025-05-15 17:39:35');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `comment_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '关注记录唯一标识',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `note_id` bigint(0) NULL DEFAULT NULL COMMENT '被评论笔记的id',
  `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评论内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (2, 15923678014, 18, '超喜欢这篇！', '2024-03-20 14:45:08');
INSERT INTO `comment` VALUES (3, 17634589012, 25, '写得太棒啦！', '2023-11-08 09:16:33');
INSERT INTO `comment` VALUES (4, 12456789015, 3, '学到很多，感谢分享！', '2024-08-15 16:09:22');
INSERT INTO `comment` VALUES (5, 16879012349, 12, '给个大大的赞！', '2023-07-28 11:30:40');
INSERT INTO `comment` VALUES (6, 14789012368, 22, '期待更多内容～', '2024-02-03 13:55:10');
INSERT INTO `comment` VALUES (7, 19023456781, 9, '很有启发，爱了！', '2023-04-10 15:42:25');
INSERT INTO `comment` VALUES (8, 18567890123, 28, '简直太赞了！', '2024-10-01 08:58:38');
INSERT INTO `comment` VALUES (9, 15345678902, 15, '干货满满，收藏了！', '2023-09-19 17:12:11');
INSERT INTO `comment` VALUES (10, 17456789013, 6, '非常不错，支持！', '2024-06-22 12:05:06');
INSERT INTO `comment` VALUES (11, 13678901245, 21, '很有帮助，感谢！', '2023-12-05 09:33:55');
INSERT INTO `comment` VALUES (12, 16901234567, 16, '内容很实用呢！', '2024-01-12 14:28:44');
INSERT INTO `comment` VALUES (13, 14890123456, 32, '写得很用心！', '2023-06-20 10:50:20');
INSERT INTO `comment` VALUES (14, 18234567890, 1, '必须点赞！', '2024-09-05 13:47:15');
INSERT INTO `comment` VALUES (15, 15789012345, 23, '很喜欢这种风格！', '2023-02-25 15:22:30');
INSERT INTO `comment` VALUES (16, 13256789014, 10, '分享得很及时！', '2024-07-18 08:11:09');
INSERT INTO `comment` VALUES (17, 15890123456, 20, '超棒的内容！', '2023-08-09 16:36:46');
INSERT INTO `comment` VALUES (18, 17901234567, 4, '对我很有帮助！', '2024-11-10 09:43:22');
INSERT INTO `comment` VALUES (19, 12678901234, 19, '真的很不错！', '2023-01-15 11:55:11');
INSERT INTO `comment` VALUES (20, 16345678901, 26, '给作者点赞！', '2024-04-08 14:02:08');
INSERT INTO `comment` VALUES (21, 13111111111, 11, '内容超赞！', '2023-05-28 10:27:33');
INSERT INTO `comment` VALUES (22, 13222222222, 13, '太棒了，爱了！', '2024-03-14 15:40:40');
INSERT INTO `comment` VALUES (23, 13333333333, 30, '非常好的分享！', '2023-10-03 09:18:15');
INSERT INTO `comment` VALUES (24, 13444444444, 1, '很有价值！', '2024-12-01 16:52:28');
INSERT INTO `comment` VALUES (25, 13555555555, 27, '写得很赞！', '2023-03-18 11:39:09');
INSERT INTO `comment` VALUES (26, 13666666666, 14, '内容很精彩！', '2024-08-22 14:21:44');
INSERT INTO `comment` VALUES (27, 13777777777, 33, '支持一下！', '2023-06-10 08:55:30');
INSERT INTO `comment` VALUES (28, 13888888888, 2, '很实用的内容！', '2024-01-25 13:10:16');
INSERT INTO `comment` VALUES (29, 13999999999, 17, '点赞点赞！', '2023-09-22 15:07:42');
INSERT INTO `comment` VALUES (30, 14000000000, 31, '内容很有趣！', '2024-05-16 10:44:25');
INSERT INTO `comment` VALUES (31, 14111111111, 24, '非常好！', '2023-12-12 12:29:08');
INSERT INTO `comment` VALUES (60, 14000000000, 15, '看着很好吃', '2025-04-30 16:50:36');
INSERT INTO `comment` VALUES (61, 18278919041, 14, '哇哇哇', '2025-04-30 16:55:15');
INSERT INTO `comment` VALUES (63, 18278919041, 40, '111', '2025-04-30 18:33:47');
INSERT INTO `comment` VALUES (68, 18278919041, 3, '卖相好好', '2025-05-06 01:09:04');
INSERT INTO `comment` VALUES (69, 18234567890, 14, '谢谢大家的喜欢！', '2025-05-06 01:09:04');
INSERT INTO `comment` VALUES (70, 14890123456, 14, '真的很不错！', '2023-12-12 12:29:08');
INSERT INTO `comment` VALUES (71, 14000000000, 14, '必须点赞！', '2025-04-30 16:50:36');
INSERT INTO `comment` VALUES (72, 15789012345, 14, '超棒的内容！', '2025-04-30 16:55:15');
INSERT INTO `comment` VALUES (73, 13256789014, 14, '哇哇哇', '2025-04-30 18:33:47');
INSERT INTO `comment` VALUES (74, 15890123456, 14, '看着很好吃', '2025-05-06 01:09:04');
INSERT INTO `comment` VALUES (75, 17901234567, 14, '写得很赞！', '2023-12-12 12:29:08');
INSERT INTO `comment` VALUES (76, 12678901234, 14, '分享得很及时！', '2025-04-30 16:50:36');
INSERT INTO `comment` VALUES (77, 16345678901, 14, '很喜欢这种风格！', '2025-04-30 16:55:15');

-- ----------------------------
-- Table structure for follow
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow`  (
  `follow_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '关注记录唯一标识',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '关注者的用户id',
  `follow_user` bigint(0) NULL DEFAULT NULL COMMENT '被关注者的用户id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`follow_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 374 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of follow
-- ----------------------------
INSERT INTO `follow` VALUES (3, 15923678014, 13777777777, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (4, 17634589012, 13666666666, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (5, 12456789015, 13555555555, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (6, 16879012349, 13444444444, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (7, 15345678902, 13333333333, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (8, 19023456781, 13222222222, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (9, 18567890123, 13111111111, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (10, 16901234567, 16345678901, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (11, 17456789013, 12678901234, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (12, 13678901245, 17901234567, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (13, 14789012368, 18278919041, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (14, 14890123456, 18278919041, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (15, 18234567890, 18278919041, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (16, 15789012345, 18278919041, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (17, 13256789014, 18278919041, '2025-05-11 11:45:00');
INSERT INTO `follow` VALUES (18, 15890123456, 18278919041, '2025-05-06 11:45:00');
INSERT INTO `follow` VALUES (19, 17901234567, 13678901245, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (20, 12678901234, 17456789013, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (21, 16345678901, 16901234567, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (22, 13111111111, 18567890123, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (23, 13222222222, 19023456781, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (24, 13333333333, 15345678902, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (25, 13444444444, 16879012349, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (26, 13555555555, 12456789015, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (27, 13666666666, 17634589012, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (28, 13777777777, 15923678014, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (29, 13888888888, 15923678014, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (30, 13999999999, 14222222222, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (31, 14000000000, 14222222222, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (32, 14111111111, 14222222222, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (33, 14222222222, 15923678014, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (34, 16901234567, 14222222222, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (35, 17456789013, 14222222222, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (36, 16345678901, 14222222222, '2025-05-05 11:45:00');
INSERT INTO `follow` VALUES (356, 18278919041, 15789012345, '2025-05-02 20:57:01');
INSERT INTO `follow` VALUES (357, 18278919041, 15890123456, '2025-05-02 20:57:05');
INSERT INTO `follow` VALUES (359, 18278919041, 14890123456, '2025-05-02 20:57:12');
INSERT INTO `follow` VALUES (360, 18278919041, 17901234567, '2025-05-02 20:57:15');
INSERT INTO `follow` VALUES (364, 18278919041, 13999999999, '2025-05-05 00:31:15');
INSERT INTO `follow` VALUES (367, 18278919041, 18234567890, '2025-05-05 01:13:41');
INSERT INTO `follow` VALUES (370, 18278919041, 13256789014, '2025-05-12 00:00:00');
INSERT INTO `follow` VALUES (371, 2, 16345678901, '2025-05-15 16:00:07');
INSERT INTO `follow` VALUES (373, 2, 17901234567, '2025-05-15 17:36:07');

-- ----------------------------
-- Table structure for like
-- ----------------------------
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like`  (
  `like_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '点赞记录唯一标识',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户id',
  `note_id` bigint(0) NULL DEFAULT NULL COMMENT '被点赞笔记的id',
  `comment_id` bigint(0) NULL DEFAULT NULL COMMENT '被点赞的评论id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '点赞时间',
  PRIMARY KEY (`like_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 104 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of like
-- ----------------------------
INSERT INTO `like` VALUES (1, 13054789216, 11, NULL, '2023-03-15 12:34:56');
INSERT INTO `like` VALUES (2, 15923678014, 25, NULL, '2024-08-20 09:12:30');
INSERT INTO `like` VALUES (4, 12456789015, 3, NULL, '2024-05-10 08:08:08');
INSERT INTO `like` VALUES (5, 16879012349, 18, NULL, '2023-07-22 20:15:10');
INSERT INTO `like` VALUES (6, 14789012368, 22, NULL, '2024-02-14 14:20:10');
INSERT INTO `like` VALUES (7, 19023456781, 5, NULL, '2023-04-28 11:33:44');
INSERT INTO `like` VALUES (8, 18567890123, 29, NULL, '2024-10-03 07:55:15');
INSERT INTO `like` VALUES (9, 15345678902, 14, NULL, '2023-09-17 13:22:33');
INSERT INTO `like` VALUES (10, 17456789013, 6, NULL, '2024-06-06 15:40:20');
INSERT INTO `like` VALUES (11, 13678901245, 27, NULL, '2023-12-25 05:10:15');
INSERT INTO `like` VALUES (12, 16901234567, 16, NULL, '2024-01-18 03:25:40');
INSERT INTO `like` VALUES (13, 14890123456, 32, NULL, '2023-06-13 18:30:00');
INSERT INTO `like` VALUES (14, 18234567890, 9, NULL, '2024-09-09 02:11:22');
INSERT INTO `like` VALUES (15, 15789012345, 23, NULL, '2023-02-28 10:05:05');
INSERT INTO `like` VALUES (16, 13256789014, 10, NULL, '2024-07-11 17:50:30');
INSERT INTO `like` VALUES (17, 15890123456, 20, NULL, '2023-08-05 14:18:45');
INSERT INTO `like` VALUES (18, 17901234567, 4, NULL, '2024-11-22 06:30:10');
INSERT INTO `like` VALUES (19, 12678901234, 19, NULL, '2023-01-19 09:45:50');
INSERT INTO `like` VALUES (20, 16345678901, 26, NULL, '2024-04-04 12:22:22');
INSERT INTO `like` VALUES (21, 13111111111, 7, NULL, '2023-05-23 19:15:35');
INSERT INTO `like` VALUES (22, 13222222222, 13, NULL, '2024-03-03 04:55:00');
INSERT INTO `like` VALUES (23, 13333333333, 30, NULL, '2023-10-10 01:10:10');
INSERT INTO `like` VALUES (24, 13444444444, 1, NULL, '2024-12-12 16:20:20');
INSERT INTO `like` VALUES (25, 13555555555, 21, NULL, '2023-03-31 11:35:40');
INSERT INTO `like` VALUES (26, 13666666666, 15, NULL, '2024-08-18 08:50:15');
INSERT INTO `like` VALUES (64, 18278919041, 4, NULL, '2025-04-29 22:52:16');
INSERT INTO `like` VALUES (65, 18278919041, 11, NULL, '2025-04-29 22:52:16');
INSERT INTO `like` VALUES (66, 18278919041, 24, NULL, '2025-04-29 22:52:53');
INSERT INTO `like` VALUES (72, 18278919041, 2, NULL, '2025-04-30 01:16:20');
INSERT INTO `like` VALUES (75, 18278919041, 1, NULL, '2025-04-30 04:45:20');
INSERT INTO `like` VALUES (79, 18278919041, NULL, 14, '2025-04-30 06:02:26');
INSERT INTO `like` VALUES (80, 18278919041, NULL, 24, '2025-04-30 06:02:26');
INSERT INTO `like` VALUES (86, 18278919041, 16, NULL, '2025-04-30 20:46:09');
INSERT INTO `like` VALUES (88, 18278919041, 6, NULL, '2025-05-10 17:16:19');
INSERT INTO `like` VALUES (89, 18278919041, 32, NULL, '2025-05-10 17:27:59');
INSERT INTO `like` VALUES (90, 18278919041, 23, NULL, '2025-05-10 17:27:59');
INSERT INTO `like` VALUES (91, 18278919041, 21, NULL, '2025-05-10 17:28:26');
INSERT INTO `like` VALUES (92, 18278919041, 13, NULL, '2025-05-10 17:29:08');
INSERT INTO `like` VALUES (93, 18278919041, 26, NULL, '2025-05-10 17:29:34');
INSERT INTO `like` VALUES (94, 18278919041, 22, NULL, '2025-05-10 17:30:37');
INSERT INTO `like` VALUES (95, 18278919041, 30, NULL, '2025-05-10 17:30:37');
INSERT INTO `like` VALUES (96, 18278919041, 20, NULL, '2025-05-10 17:30:37');
INSERT INTO `like` VALUES (101, 2, 14, NULL, '2025-05-15 17:38:55');
INSERT INTO `like` VALUES (102, 2, 22, NULL, '2025-05-15 17:39:24');
INSERT INTO `like` VALUES (103, 18278919041, NULL, 68, '2025-05-15 19:44:22');

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `message_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '信息记录唯一标识',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '发送信息的用户id',
  `message_user_id` bigint(0) NULL DEFAULT NULL COMMENT '接收信息的用户id',
  `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对话内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '时间',
  `is_read` int(0) NULL DEFAULT NULL COMMENT '判断是否阅读',
  PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (1, 18278919041, 17901234567, '哈喽，吃了吗，在干嘛？', '2023-01-19 09:45:50', 0);
INSERT INTO `message` VALUES (2, 17901234567, 18278919041, 'hollow，还没吃，在写作业，你呢，吃了吗?吃了什么', '2023-02-28 10:05:05', 1);
INSERT INTO `message` VALUES (3, 18278919041, 17901234567, '我吃了，我吃了鸡公煲。鸡公煲~鸡公煲~~经过我的胃~', '2023-03-15 12:34:56', 0);
INSERT INTO `message` VALUES (4, 17901234567, 18278919041, '哈哈哈哈哈，挺好的', '2023-03-31 11:35:40', 1);
INSERT INTO `message` VALUES (5, 13777777777, 18278919041, '救命！！发现一家人均 30 吃到撑的火锅店！冰汤圆免费续，毛肚脆到能打乒乓球！！', '2023-04-28 11:33:44', 1);
INSERT INTO `message` VALUES (6, 18278919041, 13777777777, '啊啊啊在哪！！这个价格在市中心根本不敢想！', '2023-05-23 19:15:35', 0);
INSERT INTO `message` VALUES (7, 13777777777, 18278919041, '就在 XX 路地铁口！记得早点去，我们 6 点到的都排了半小时队！', '2023-06-13 18:30:00', 1);
INSERT INTO `message` VALUES (8, 18278919041, 13777777777, '：辣锅牛油香吗？本四川人不能接受寡淡锅底！', '2023-07-22 20:15:10', 0);
INSERT INTO `message` VALUES (9, 13777777777, 18278919041, '香到舔碗！辣度分三种，我吃中辣直接爽到起飞🌶️ 冲就完事了！', '2023-08-05 14:18:45', 1);
INSERT INTO `message` VALUES (10, 18278919041, 13444444444, '计划去西双版纳！姐妹们有避雷的网红店吗🥺 不想踩坑！！', '2023-09-17 13:22:33', 0);
INSERT INTO `message` VALUES (11, 18278919041, 13444444444, '别去星光夜市那家 XXX！我花 88 拍的傣服写真全是大头照，修图丑到亲妈不认🙅🏻♀️', '2023-10-10 01:10:10', 0);
INSERT INTO `message` VALUES (12, 13444444444, 18278919041, '强推告庄的老挝冰咖啡！20 块一大袋，斑斓味巨浓郁🧊 喝完还能拿袋子当配饰！', '2023-12-25 05:10:15', 1);
INSERT INTO `message` VALUES (13, 18278919041, 13444444444, '太感谢了！已加入收藏夹！！！❤️', '2024-01-18 03:25:40', 0);
INSERT INTO `message` VALUES (14, 15923678014, 18278919041, '家人们，我发现了一个超牛的学习方法，成绩真的蹭蹭往上涨！😎', '2024-02-14 14:20:10', 1);
INSERT INTO `message` VALUES (15, 18278919041, 15923678014, '哇，真的吗？快说说是什么方法呀，我还在为学习发愁呢。', '2024-04-04 12:22:22', 0);
INSERT INTO `message` VALUES (16, 15923678014, 18278919041, '就是制定详细的学习计划，然后把大目标分解成一个个小目标，完成一个就打个勾，超有成就感的！🤩', '2024-05-10 08:08:08', 1);
INSERT INTO `message` VALUES (17, 18278919041, 15923678014, '听起来好不错啊，那你是怎么安排时间的呢？我总是觉得时间不够用。', '2024-06-06 15:40:20', 0);
INSERT INTO `message` VALUES (18, 15923678014, 18278919041, '我会根据课程表和自己的生物钟来安排，比如早上记忆力好，就背背单词和课文，晚上做一些数学题，这样效率很高哦。👍', '2024-07-11 17:50:30', 1);
INSERT INTO `message` VALUES (19, 18278919041, 16879012349, '哈哈哈哈，我家狗狗今天太搞笑了，它居然把我的袜子当成玩具，满屋子跑！🐶', '2024-08-18 08:50:15', 0);
INSERT INTO `message` VALUES (20, 16879012349, 18278919041, '哈哈哈，好可爱呀！我家猫咪也经常干这种事，把我的数据线当成它的猎物。', '2024-08-20 09:12:30', 1);
INSERT INTO `message` VALUES (21, 18278919041, 16879012349, '是呀，这些小家伙们总是能给我们带来很多欢乐。我家狗狗还特别喜欢黏着我，我走到哪它跟到哪。', '2024-09-09 02:11:22', 0);
INSERT INTO `message` VALUES (22, 16879012349, 18278919041, '好羡慕呀，我也想养一只这么可爱的狗狗。不过养狗狗是不是很麻烦呀？要注意些什么呢？', '2024-10-03 07:55:15', 1);
INSERT INTO `message` VALUES (23, 18278919041, 16879012349, '其实还好啦，就是要按时给它喂食、带它出去遛弯，还要定期给它打疫苗、洗澡，只要用心照顾，就会收获很多快乐哦', '2024-11-22 06:30:10', 0);
INSERT INTO `message` VALUES (24, 13999999999, 18278919041, '姐妹们，我终于搞定了一个超难的项目，在职场上又成长了一步', '2024-12-12 16:20:20', 1);
INSERT INTO `message` VALUES (25, 18278919041, 13999999999, '哇，恭喜呀！快说说你是怎么做到的，我最近也遇到了一个很棘手的项目，头疼死了。', '2025-04-29 22:52:16', 0);
INSERT INTO `message` VALUES (26, 13999999999, 18278919041, '我先是把项目的目标和要求理清楚，然后把任务分配给团队成员，大家分工合作。遇到问题的时候，我们一起讨论解决方案，不要自己一个人扛着。', '2025-04-29 22:52:16', 1);
INSERT INTO `message` VALUES (27, 18278919041, 13999999999, '嗯，你说得很有道理。那在跟领导和客户沟通方面，有没有什么技巧呢？', '2025-04-29 22:52:53', 0);
INSERT INTO `message` VALUES (28, 13999999999, 18278919041, '要及时汇报项目进度，让领导和客户了解情况。说话要简洁明了，突出重点，有什么问题或者需求也要及时提出来，不要憋在心里哦。', '2025-04-30 01:16:20', 1);
INSERT INTO `message` VALUES (32, 18278919041, 13444444444, '嘿嘿', '2025-05-04 01:32:31', 0);
INSERT INTO `message` VALUES (40, 18278919041, 13999999999, '收到', '2025-05-15 19:42:00', 0);
INSERT INTO `message` VALUES (41, 18278919041, 1, '你好', '2025-05-17 14:57:06', 0);
INSERT INTO `message` VALUES (42, 18278919041, 1, 'Hello', '2025-05-17 15:12:07', 0);
INSERT INTO `message` VALUES (43, 18278919041, 13777777777, 'gogogo！出发咯！', '2025-05-17 19:47:12', 0);

-- ----------------------------
-- Table structure for note
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note`  (
  `note_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '笔记ID',
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `page_views` bigint(0) NULL DEFAULT NULL COMMENT '笔记浏览量',
  `visibility` int(0) NULL DEFAULT NULL COMMENT '可见性（1-公开，2-私密，3-互关）',
  `draft` int(0) NULL DEFAULT NULL COMMENT ' 草稿（1-是，2-否）',
  `interest` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '兴趣分类',
  `title` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容',
  `image_urls` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '笔记图片',
  `video_url` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '笔记视频',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`note_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 99 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of note
-- ----------------------------
INSERT INTO `note` VALUES (1, 18278919041, 31, 1, 2, '美食', '超诱人西兰花炒虾仁，解锁味蕾新体验', '鲜嫩虾仁与翠绿西兰花的绝美邂逅，每一口都是幸福的滋味。西兰花脆爽可口，虾仁 Q 弹多汁，两者搭配简直完美！ 做法超简单，厨房小白也能轻松上手，营养美味两不误！ 食材：西兰花、虾 调料：葱姜蒜、食用油、生抽、料酒、胡椒碎 制作步骤： 虾去皮去核去虾线加姜葱，加生抽料酒各一勺，加胡椒碎少许搅拌均匀腌制 10 分钟 西兰花切成小朵焯水 2 分钟 锅中油烧热倒入虾煎熟 倒入西兰花加生抽、蚝油和胡椒碎翻炒一分钟出锅', 'ms1.tmp', NULL, '2025-04-25 03:35:47', '2025-04-25 03:35:47');
INSERT INTO `note` VALUES (2, 15923678014, 1, 1, 2, '美食', '周六 17 点的仪式感｜把「痛点」揉成草莓卷的甜', '终于把痛点梳理，迭代抓手锁进周五的最后一封邮件。让发酵的奶油与烤箱的暖光接管主场，看面皮裹着草莓酱卷成完美的螺旋… 我想所有 KPI 的终点 都该通往让自己嘴角上扬 15° 的甜吧', 'ms2.tmp', NULL, '2025-04-25 03:36:03', '2025-04-25 03:36:03');
INSERT INTO `note` VALUES (3, 17634589012, 14, 1, 2, '美食', '油焖大虾太香了～', '裹满酱汁香到连虾壳都想吃掉 虾肉很鲜嫩，酱汁酸酸甜甜浓郁入味 可以多放蒜末和酱汁炒完很香', 'ms3.tmp', NULL, '2025-04-25 04:51:31', '2025-04-25 04:51:31');
INSERT INTO `note` VALUES (4, 12456789015, 4, 1, 2, '美食', '想要一个全是蛋挞图的评论区', '蛋挞你！！！怎么这么好吃 每次吃上都要呆一秒 怎么吃不腻啊！！谁说蛋挞不好吃！吃一口原地复活 这也太好吃了', 'ms4.tmp', NULL, '2025-04-25 04:55:28', '2025-04-25 04:55:28');
INSERT INTO `note` VALUES (5, 16879012349, 1, 1, 2, '美食', '春天的色彩与味道', '用美食表达浪漫', 'ms5.tmp', NULL, '2025-04-25 04:56:49', '2025-04-25 04:56:49');
INSERT INTO `note` VALUES (6, 14789012368, 5, 1, 2, '美食', '到底是什么做的啊', '这也太好吃了吧 买了新口味 草莓麻薯的太好吃了 这次直接包圆剩下的两个 上次的口味评价放 p4 了', 'ms6.tmp', NULL, '2025-04-25 04:59:52', '2025-04-25 04:59:52');
INSERT INTO `note` VALUES (7, 19023456781, 1, 1, 2, '美食', '极限冰激凌四选一', '选一选', 'ms7.tmp', NULL, '2025-04-25 05:01:13', '2025-04-25 05:01:13');
INSERT INTO `note` VALUES (8, 18567890123, 1, 1, 2, '美食', '成都二仙桥！！！个人觉得无法超越。。。。', '给好多人推荐过的！！在放假前又跑去吃… 最爱她家的鸡爪和土豆片了！！心目中第一的烧烤了 555…', 'ms8.tmp', NULL, '2025-04-25 05:04:36', '2025-04-25 05:04:36');
INSERT INTO `note` VALUES (9, 15345678902, 1, 1, 2, '美食', '<可商素材> 蛋糕素材实物素材', '素材可商 ，提拉米苏 ，可商用素材 ，素材分享 ，高质素材 ，设计素材 ，手账素材', 'ms_yz1_1.tmp,ms_yz1_2.tmp,ms_yz1_3.tmp,ms_yz1_4.tmp,ms_yz1_5.tmp', NULL, '2025-04-25 05:07:03', '2025-04-25 05:07:03');
INSERT INTO `note` VALUES (10, 17456789013, 1, 1, 2, '美食', '有个会做饭的男朋友真幸福！', '超级下饭的家常菜，有个会做饭的男朋友，会做饭的男人最帅 #男朋友做饭的日常，我的日常', 'ms_yz2_1.tmp,ms_yz2_2.tmp,ms_yz2_3.tmp,ms_yz2_4.tmp,ms_yz2_5.tmp', NULL, '2025-04-25 05:07:10', '2025-04-25 05:07:10');
INSERT INTO `note` VALUES (11, 13678901245, 1, 1, 2, '美食', '桂林… 到底是谁泄露的！！害我下班排队去吃', '分享这家土著们经常来吃的破烂小店 我排队都要来吃这家桂林本地的牛八宝火锅 新鲜现切的牛肉好新鲜！！ 全牛宴真的好幸福啊啊啊啊😭 谁懂这一顿的含金量… 我一周来 3 次了', 'ms_yz3_1.tmp,ms_yz3_2.tmp,ms_yz3_3.tmp,ms_yz3_4.tmp,ms_yz3_5.tmp,ms_yz3_6.tmp,ms_yz3_7.tmp', NULL, '2025-04-25 05:07:18', '2025-04-25 05:07:18');
INSERT INTO `note` VALUES (12, 16901234567, 2, 1, 2, '美食', '桂林 首家鱼头火锅，酸汤太上头了！', '今天打卡了桂林老表强推的一家火锅店【唐记美味多鱼头】！ 老板是湖南人，偏湘菜口味，还称是桂林首家鱼头火锅！ 点了招牌酸辣鱼头锅，酸汤真的绝了！酸辣开胃，鱼头鲜嫩入味，搭配什么都好吃！另外还加了一份田螺，吸满酸汤的田螺也很不错！ 来桂林耍的滴小伙伴，不妨来此一探究竟！ 【唐记美味多鱼头】 桂林市七星区普陀路 7-6 号三里店（长城酒店对面） 你们还晓得桂林哪些隐藏美食？', 'ms_yz4_1.tmp,ms_yz4_2.tmp,ms_yz4_3.tmp', NULL, '2020-03-25 05:12:01', '2025-04-25 05:12:01');
INSERT INTO `note` VALUES (13, 14890123456, 2, 1, 2, '美食', '桂林大学生都快来吃！！人均 15+！！', '便宜也能吃到好东西！ 新开的这家餐厅真的是平价好吃分量足！', 'ms_yz5_1.tmp,ms_yz5_2tmp,ms_yz5_3.tmp,ms_yz5_3.tmpms_yz5_4.tmp,ms_yz5_5.tmp', NULL, '2025-04-25 05:16:57', '2025-04-25 05:16:57');
INSERT INTO `note` VALUES (14, 18234567890, 71, 1, 2, '美食', '​出行记ᯓ，美食分享', '肆意 舒适 自在 临桂吾悦广场 甜园 打卡 (舒芙蕾不错好吃。值得推荐) 相伴豆花 (料给得很多很足)', 'ms_yz6_1.tmp,ms_yz6_2.tmp', NULL, '2025-04-25 06:34:37', '2025-04-25 06:34:37');
INSERT INTO `note` VALUES (15, 15789012345, 10, 1, 2, '美食', '美食时间到', '很内向 吃饱了也不说 一直吃', 'ms9.tmp', NULL, '2025-04-25 06:34:37', '2025-04-25 06:34:37');
INSERT INTO `note` VALUES (16, 13256789014, 9, 1, 2, '美食', '南宁穷鬼五一都快去吃啊啊啊（人均 60）', '尊的好美，坐在里面吃饭食欲都大增啊', 'ms10.tmp', NULL, '2025-04-25 06:34:37', '2025-04-25 06:34:37');
INSERT INTO `note` VALUES (17, 15890123456, 2, 1, 2, '美食', '南宁...... 好吃的穷鬼漂亮饭（不骗人）！', '这家店确实让我印象深刻， 中古风的装修，是我爱的风格，出片很得！ 73.... 这一桌有点牛...... 牛排沙拉很扎实好吃，意式炸酱面也很不错， 椰奶大满贯份量味道很得！ 会一直来。。。。。', 'ms11.tmp', NULL, '2025-04-25 06:34:37', '2025-04-25 06:34:37');
INSERT INTO `note` VALUES (18, 17901234567, 7, 1, 2, '美食', '南宁这家老牌餐厅，真的是家庭聚会的首选！', '本地人推荐的餐厅', 'ms12.tmp', NULL, '2025-04-25 06:34:37', '2025-04-25 06:34:37');
INSERT INTO `note` VALUES (19, 12678901234, 2, 1, 2, '发型', '自然的微卷搭配轻盈碎发', '还在纠结发型的姐妹，别犹豫，快冲这款层次感锁骨发，保证让你惊艳众人！', 'fx1.tmp', NULL, '2025-04-25 04:45:45', '2025-04-25 04:45:45');
INSERT INTO `note` VALUES (20, 16345678901, 28, 1, 2, '发型', '微卷修饰脸型', '打理起来也不复杂，早上用梳子梳几下，再喷点护发精油，就能美美的出门啦。不管是搭配简约的衬衫，还是休闲的卫衣，都能轻松 hold 住。', '1747296678225_temp5001068769744358514.tmp', NULL, '2025-04-25 04:51:31', '2025-04-25 04:51:31');
INSERT INTO `note` VALUES (21, 13111111111, 5, 1, 2, '发型', '男生烫发发型推荐', '氛围感 ，少年感发型', 'fx3.tmp', NULL, '2025-04-25 04:55:28', '2025-04-25 04:55:28');
INSERT INTO `note` VALUES (22, 13222222222, 3, 1, 2, '发型', '根据脸型选发型 / 男士发型参考 / 男生短发发型', '男生发型打理', 'fx4.tmp', NULL, '2025-04-25 04:56:49', '2025-04-25 04:56:49');
INSERT INTO `note` VALUES (23, 13333333333, 6, 1, 2, '发型', '微分碎盖', '适合菱形脸，方脸，下巴短，额头窄的男生推荐试一下 偏瘦的男生两侧不建议剪太短 刘海长度比两侧稍微长一些，从侧面看是前长后短的效果，能有效拉长头型，让脸型看起来更立体，下巴更好看！针对发量特别多的男生，因为侧分会出现一边头发多一边头发少的问题，所以如果侧分的话，头顶层次一定要剪出来', 'fx5.tmp', NULL, '2025-04-25 04:59:52', '2025-04-25 04:59:52');
INSERT INTO `note` VALUES (24, 13444444444, 1, 1, 2, '头像', '头像越怪～人越可爱～', '脑̾袋̾冒̾烟̾儿̾ 肚子在 ₅ ₅ 叫 ᵎ ᵎ ᵎ 地球旋转我吃饭૮₍ ˊᗜˋ₎ა 冒个泡 oOooOooooO 怼个脸・͈ᴗ・͈ 小猫困困的 我也是 Z☡zᶻ ˁ῁̭ˀˁ῁̮ˀˁ῁̱ˀˁ῁̥ˀˁ῁̼ˀˁ῁̩ˀˁ῁̬ˀ ɢᴏ ɢᴏ ɢᴏ 𓂃𓅷𓏲 粗去丸噜 ∩˃o˂∩ ᵎᵎ', 'tx1.tmp', NULL, '2025-04-25 05:07:03', '2025-04-25 05:07:03');
INSERT INTO `note` VALUES (25, 13555555555, 4, 1, 2, '头像', '头像越怪 人越可爱', '奇奇怪怪的头像', 'tx2.tmp', NULL, '2025-04-25 05:07:10', '2025-04-25 05:07:10');
INSERT INTO `note` VALUES (26, 18278919041, 4, 1, 2, '头像', '头像｜绿色系可爱头像', '春日必备绿色系可爱头像', 'tx3.tmp', NULL, '2025-04-25 05:07:18', '2025-04-25 05:07:18');
INSERT INTO `note` VALUES (27, 13777777777, 11, 1, 2, '头像', '�𝒆𝑪𝒉𝒂𝒕｜可爱搞怪头像', '今天你换头像了吗', 'tx4.tmp', NULL, '2025-04-25 05:12:01', '2025-04-25 05:12:01');
INSERT INTO `note` VALUES (28, 13888888888, 3, 1, 2, '头像', 'WeChat 头像｜猫猫头像', 'WeChat｜猫猫头像 ​​​ 没人能拒绝可爱的猫猫吧', 'tx5.tmp', NULL, '2025-04-25 05:16:57', '2025-04-25 05:16:57');
INSERT INTO `note` VALUES (29, 13999999999, 10, 1, 2, '婚礼', '森系婚礼，与自然共舞', '阳光穿透林间枝叶，洒落细碎光斑，白色纱幔随风轻扬。在满目的绿意中，交换誓言，每一朵野花、每一缕清风，都见证着我们的浪漫时刻。', 'hl_yz1_1.tmp,hl_yz1_2.tmp,hl_yz1_3.tmp', NULL, '2025-05-01 10:00:00', '2025-05-01 10:00:00');
INSERT INTO `note` VALUES (30, 14000000000, 2, 1, 2, '婚礼', '复古欧式婚礼，复刻经典浪漫', '雕花烛台、鎏金餐具，搭配巴洛克风格的婚纱与燕尾服。在古典宫殿般的场景里，我们携手走过红毯，仿佛穿越回中世纪，续写属于我们的爱情传奇。', 'hl_yz2_1.tmp,hl_yz2_2.tmp,hl_yz2_3.tmp', NULL, '2025-05-02 14:30:00', '2025-05-02 14:30:00');
INSERT INTO `note` VALUES (31, 14111111111, 1, 1, 2, '婚礼', '海边婚礼，聆听浪的祝福', '蔚蓝大海与金色沙滩为幕，海风裹挟着誓言飘向远方。赤脚踩在细软的沙滩上，我们相拥而吻，这一刻，天地都是甜蜜的见证。', 'hl_yz3_1.tmp,hl_yz3_2.tmp,hl_yz3_3.tmp', NULL, '2025-05-03 09:15:00', '2025-05-03 09:15:00');
INSERT INTO `note` VALUES (32, 14222222222, 1, 1, 2, '婚礼', '中式新国潮婚礼，传承千年浪漫', '秀禾服与龙凤褂的华美，搭配红绸喜字、中式灯笼。传统的拜堂仪式与现代设计碰撞，既保留古韵，又彰显个性，让婚礼满是东方韵味。', 'hl_yz4_1.tmp,hl_yz4_2.tmp,hl_yz4_3.tmp', NULL, '2025-05-04 16:20:00', '2025-05-04 16:20:00');
INSERT INTO `note` VALUES (33, 14000000000, 8, 1, 2, '婚礼', '梦幻童话婚礼，圆一场公主梦', '粉色城堡、缤纷气球，身着蓬蓬婚纱的我与帅气王子般的你，在童话般的场景里翩翩起舞。这里没有童话的结局，因为我们的故事才刚刚开始。', 'hl_yz5_1.tmp,hl_yz5_2.tmp,hl_yz5_3.tmp', NULL, '2025-05-05 11:45:00', '2025-05-05 11:45:00');
INSERT INTO `note` VALUES (83, 14789012368, 0, 1, 2, '宠物', '泥嚎！吃不吃苹狗', 'Hi姨姨！你爱吃苹果吗!\n是宝宝介个甜甜的小苹狗哦！\n	\n妈就很爱吃苹果🍎妈说它清清甜甜！是属于春天的味道！\n	\n所以这次爷爷不泡茶新出的苹果系列新品奶茶！妈一杯不落的全尝过啦！嘿嘿！果然是春天味！\n	\n妈还说宝撞脸奶茶杯上的苹果小狗了捏！带上头套简直一模一样！这尊嘟不是按照宝来设计的吗妈还连喝几天把苹果小狗周边全都拿下啦！\n	\n姨姨们！你们说！\n包装上的小斗尊嘟和宝长的很像吗？', '1747296868794_temp7154554873545823059.tmp,1747296875619_temp332757961753582330.tmp,1747296875622_temp2762100149737724179.tmp', NULL, '2025-05-15 16:14:15', '2025-05-15 16:14:15');
INSERT INTO `note` VALUES (84, 19023456781, 1, 1, 2, '宠物', '歪！是公主小猫嘛！', '什么运气啊，能养到一只漂亮又乖巧的梦中情猫！\n养了白白之后我也做了很多攻略，当时刚养猫，除了基础喂养，我最关心的就是健康问题了，尤其是驱虫🐛，专业知识复杂，可选择的品牌也多，最后选了品牌有保障，驱虫范围广的驱虫药💊作为日常驱虫使用\n我家现在用的是妙宠爱，是之前用的大宠爱的升级版，跟妙三多同厂，大厂品质有保障，用的人多口碑也很不错，成分温和安全，新手也能闭眼入。驱虫谱也广，都清楚地列在包装上啦，跳蚤、蜱虫、虱子、耳螨、蛔虫、钩虫、心丝虫，这7种常见又重要的寄生虫一次搞定～还能驱除绦虫宿主，一支就能搞定体内外的寄生虫，我家一直按时驱虫确实没发现过虫子，真省心！\n它的滴管的设计操作方便，新手也容易上手，拨开毛发，滴在后脖子上就行，而且不油腻，快干易吸收，成分也很安全，不用怕小猫咪舔舐，用完很快就吸收了，这下终于让白白摆脱伊丽莎白圈了。而且它用完不用特意去打理毛发，新手自己在家也能轻松操作！\n虽然小猫不咋出门，但是感染途径也很多，比如人出门就可能会带回来虫卵，所以想要健康漂亮的小猫，定期驱虫跟体检也要记得安排好哦～～', '1747296942538_temp3445272702042523527.tmp,1747296942554_temp3335874897454053998.tmp,1747296942561_temp2432973367108220749.tmp', NULL, '2025-05-15 16:15:33', '2025-05-15 16:15:33');
INSERT INTO `note` VALUES (85, 15789012345, 0, 1, 2, '宠物', '小狗和好天气˙˖°⋆', '小狗和好天气\n阳光和爱\n会是牢固的春天\n狗狗时间好像我们精神上的爪爪按摩\n心情都跟着小狗一起明媚起来啦\n让我们一起谢谢小狗๑ᵔ⤙ᵔ๑ ⸝⸝', '1747297136872_temp5832719022606179793.tmp,1747297141735_temp3340264014403501417.tmp,1747297141784_temp2192085403359369538.tmp,1747297141799_temp477899582427933542.tmp', NULL, '2025-05-15 16:18:40', '2025-05-15 16:18:40');
INSERT INTO `note` VALUES (86, 13256789014, 2, 1, 2, '宠物', '世界上最最最可爱的宝宝！！', 'This is my cattax！Her name is Molly~Nice to meet u!', '1747297180608_temp8902048492704962482.tmp,1747297180651_temp7207883957184344497.tmp,1747297180654_temp1569804561351679410.tmp', NULL, '2025-05-15 16:19:31', '2025-05-15 16:19:31');
INSERT INTO `note` VALUES (87, 15890123456, 1, 1, 2, '宠物', 'ee快来看超绝儿童小手！！！', '\n\nLIVE\n\nLIVE\n\nLIVE\n\nLIVE\n1/4\n\n邦邦小宝\n关注\nee快来看超绝儿童小手！！！\n这跟现实版彼得兔有什么区别！！！', '1747297237853_temp8386263671023528608.tmp', '1747297237919_temp1330421588135832357.tmp', '2025-05-15 16:20:32', '2025-05-15 16:20:32');
INSERT INTO `note` VALUES (88, 17901234567, 1, 1, 2, '宠物', '这一年 拍到了猫生的18张照片߹𖥦߹', '年度照片来咯\nee们是从哪张图认识奶米的呀~\n玩小红书第三年啦 也增加了新的家庭成员舒舒、茜茜\n这一年奶米也从去年懵懂少年变成了臭脸小猫\n谢谢大家的喜欢\n希望每一个爱猫猫的人都事事顺利！！！', '1747297337205_temp1144632800215830396.tmp,1747297342304_temp2438149651978546448.tmp,1747297342331_temp3049328836528994188.tmp,1747297342340_temp3983333086911964366.tmp,1747297342561_temp6830960024572460351.tmp,1747297342608_temp2680872475135568995.tmp', NULL, '2025-05-15 16:22:08', '2025-05-15 16:22:08');
INSERT INTO `note` VALUES (89, 12678901234, 1, 1, 2, '影视', '爆甜预警⚠️53部现偶爱情剧', '荐 #浪漫爱情剧 #新剧推送 #高甜宠剧 #宝藏电视剧 #爱情剧 #新剧推荐', '1747297564166_temp6390136934550703267.tmp,1747297568483_temp3994685247504418340.tmp,1747297568522_temp2331197002316418554.tmp', NULL, '2025-05-15 16:25:55', '2025-05-15 16:25:55');
INSERT INTO `note` VALUES (90, 16345678901, 0, 1, 2, '影视', 'Netflix犯罪惊悚新剧《211号牢房', '🎬︎剧集名称：《211号牢房》\n🔍︎创作类型：剧情 / 惊悚 / 犯罪\n📍制片国家/地区：墨西哥', '1747297615349_temp7244448542905884271.tmp,1747297615363_temp6356032554870062591.tmp,1747297615394_temp7542347253307548898.tmp,1747297615403_temp2564485755092501742.tmp', NULL, '2025-05-15 16:26:43', '2025-05-15 16:26:43');
INSERT INTO `note` VALUES (91, 13111111111, 0, 1, 2, '影视', '6部古装轻喜剧！下饭剧一口气刷完笑疯🤣', '真的太爱这种古装背景下的家庭群像喜剧啦\n女性成长➕亲情羁绊，笑点密集hin抓马！\n姐妹互怼的爆笑名场面，真是一边爆笑，一边', '1747297716224_temp4957828579429518651.tmp,1747297718300_temp4750978511737648289.tmp,1747297723098_temp3441894535300878481.tmp', NULL, '2025-05-15 16:28:27', '2025-05-15 16:28:27');
INSERT INTO `note` VALUES (92, 13222222222, 1, 1, 2, '影视', '播了播了‼️第一集就笑疯了', '笑疯了，我怎么才看这部古装喜剧，直接炫全集‼️把先婚后爱这个老套路玩出了新花样，剧情笑到头掉。赵昭仪演的花溶可爱到犯规，她是将门之后，怀揣着“侠女梦”闯荡江湖，性格直爽，武力值啦满，可一遇到男主秦尚城，就开启了相爱相杀的爆笑日常。', '1747297749213_temp8535427581808504214.tmp,1747297749229_temp610073361404073678.tmp,1747297749272_temp7888302195100062152.tmp,1747297749275_temp8745276165188137164.tmp', NULL, '2025-05-15 16:29:03', '2025-05-15 16:29:03');
INSERT INTO `note` VALUES (93, 13333333333, 0, 1, 2, '音乐', '旺自己歌单第二弹来了!循环3天能量都增强', '旺自己歌单第二弹来了!循环3天能量都增强了', '1747298003427_temp4667953372619462847.tmp,1747298009640_temp8895151784583256738.tmp,1747298009662_temp3588379026584428163.tmp,1747298009672_temp6522079586714490028.tmp', NULL, '2025-05-15 16:29:31', '2025-05-15 16:29:31');
INSERT INTO `note` VALUES (94, 13444444444, 0, 1, 2, '音乐', '适合清唱的100首歌', '#我的宝藏歌单 #歌曲推荐 #音乐分享 #好歌推荐 #歌曲 #清唱 #日常唱歌 #歌曲分享 #好歌分享 #好听的音乐 #晒出我的歌单 #我的私藏歌单 #歌曲串烧 #好听的歌 #宝藏歌曲 #歌单分享', '1747298037269_temp1718652322266045449.tmp,1747298037269_temp1718652322266045449.tmp,1747298044341_temp1349236515554182075.tmp,1747298044351_temp3619971721922495185.tmp', NULL, '2025-05-15 16:33:44', '2025-05-15 16:33:44');
INSERT INTO `note` VALUES (95, 13555555555, 0, 1, 2, '音乐', '现在的00后都在听什么歌', '00后的歌单', '1747298154750_temp5590112826479280804.tmp,1747298158995_temp3074082598291191114.tmp,1747298159016_temp4707655178809736836.tmp,1747298159017_temp866206526436750760.tmp', NULL, '2025-05-15 16:35:40', '2025-05-15 16:35:40');
INSERT INTO `note` VALUES (96, 18278919041, NULL, 2, 2, '音乐', '钢琴练多了人真的会变！', '钢琴 | 成年人的治愈系浪漫✨\n家人们，谁懂啊！我彻底栽进钢琴的温柔乡出不来了😭！\n自从学了钢琴，整个人都变得超有艺术氛围感～感觉连灵魂都被艺术气息浸润，优雅 buff 直接叠满！而且它简直是压力粉碎机，下班回家弹上几曲，疲惫和焦虑瞬间被琴音驱散，沉浸在音乐里的时光超治愈～不知不觉中，手指灵活度和专注力都变强了，这波真的血赚！\n或许你会担心零基础学不会，或者觉得成年人学琴太晚。但相信我，我从一个五线谱都认不全的小白，到现在能流畅弹曲子，只要肯迈出第一步，就能解锁钢琴带来的无限惊喜！\n💕真心安利给所有对钢琴心动的宝子，让我们在琴键的世界里，奏响属于自己的浪漫乐章～', '1747298188430_temp1157576712327199810.tmp,1747298188450_temp4442261605782526933.tmp,1747298188487_temp3997498280352470084.tmp,1747298188493_temp8825239416364930897.tmp', NULL, '2025-05-17 19:17:00', '2025-05-17 19:17:00');
INSERT INTO `note` VALUES (97, 2, NULL, 1, 2, '情感', '1', '1', '1747301872813_temp6007030825314036040.tmp', NULL, '2025-05-15 17:40:16', '2025-05-15 17:40:16');
INSERT INTO `note` VALUES (98, 18278919041, 1, 1, 1, '摄影', '1', '1', '1747309273918_temp3162652304152805569.tmp', NULL, '2025-05-15 19:41:01', '2025-05-15 19:41:01');
INSERT INTO `note` VALUES (99, 18278919041, 2, 1, 2, '绘画', '阿呆', '阿呆简笔画', '1747480471316_temp7067554287295532241.tmp', NULL, '2025-05-17 19:12:44', '2025-05-17 19:12:44');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  `username` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `theme` int(0) NULL DEFAULT NULL,
  `sex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户性别',
  `interests` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户兴趣',
  `signature` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户个性签名',
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地理位置',
  `portrait_image` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户 头像',
  `background_image` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户 背景',
  `birthday_time` date NULL DEFAULT NULL COMMENT '用户出生日期',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '用户创建时间',
  INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (18278919041, 'Li123321', '孤舟渡', 1, '男', '美食, 摄影, 影视, 头像, 婚礼, 宠物', '孤舟破风浪，踏歌向远方', '北京市·北京市', 'tx_nan1.tmp', 'bj1.tmp', '1996-07-10', '2025-05-05 11:45:00');
INSERT INTO `user` VALUES (15923678014, 'Li11111111111111', '砚中雪', 1, '男', '旅游, 游戏, 影视, 机车, 绘画', '砚墨染白雪，绘尽人间色', NULL, 'tx_nan2.tmp', 'bj2.tmp', '1999-03-22', '2020-04-18 00:00:00');
INSERT INTO `user` VALUES (17634589012, 'Li11111111111111', '听松客', 1, '男', '音乐, 健身, 社交, 明星, 美食', '松下听风语，食里品人生', NULL, 'tx_nan3.tmp', 'bj3.tmp', '2000-09-08', '2021-07-25 00:00:00');
INSERT INTO `user` VALUES (12456789015, 'Li11111111111111', '鹤别青山', 1, '男', '美食, 旅游, 家居, 摄影, 母婴', '鹤去青山远，艺海任驰骋', NULL, 'tx_nan4.tmp', 'bj4.tmp', '1997-11-15', '2019-08-12 00:00:00');
INSERT INTO `user` VALUES (16879012349, 'Li11111111111111', '山月不知', 1, '男', '游戏, 学习, 运动, 社交, 汽车', '山月藏心事，镜头记温情。', NULL, 'tx_nan5.tmp', 'bj5.tmp', '1998-05-28', '2020-11-09 00:00:00');
INSERT INTO `user` VALUES (15345678902, 'Li11111111111111', '星垂平野', 1, '男', '运动, 健身, 舞蹈, 音乐, 时尚', '星垂映平野，墨韵染家馨', NULL, 'tx_nan9.tmp', 'bj9.tmp', '2000-01-17', '2021-05-10 00:00:00');
INSERT INTO `user` VALUES (19023456781, 'Li11111111111111', '野鹤', 1, '男', '机车, 旅游,  运动, 汽车, 明星', '野鹤栖天地，音画抒自由', NULL, 'tx_nan7.tmp', 'bj7.tmp', '1996-04-03', '2018-06-18 00:00:00');
INSERT INTO `user` VALUES (18567890123, 'Li11111111111111', '青崖白鹿', 1, '男', '绘画, 摄影, 书法, 音乐, 家居', '白鹿踏青崖，追星驭风驰', NULL, 'tx_nan8.tmp', 'bj8.tmp', '1999-08-11', '2020-09-22 00:00:00');
INSERT INTO `user` VALUES (16901234567, 'Li11111111111111', '半醒', 1, '男', '美食, 旅游, 摄影, 音乐, 情感', '半醒观世相，游艺会知音', NULL, 'tx_nan12.tmp', 'bj12.tmp', '2001-03-18', '2022-04-27 00:00:00');
INSERT INTO `user` VALUES (17456789013, 'Li11111111111111', '拾荒旅人', 1, '男', '汽车, 机车, 露营, 旅游, 摄影', '拾荒寻奇遇，舞乐逐潮流', NULL, 'tx_nan10.tmp', 'bj10.tmp', '1997-06-24', '2019-10-03 00:00:00');
INSERT INTO `user` VALUES (13678901245, 'Li11111111111111', '雾隐', 1, '男', '游戏, 影视, 绘画, 运动, 社交', '雾隐寻幽径，车影踏征途', NULL, 'tx_nan11.tmp', 'bj11.tmp', '1998-12-09', '2020-02-15 00:00:00');
INSERT INTO `user` VALUES (14789012368, 'Li11111111111111', '鲸落南北', 1, '男', '露营, 摄影, 绘画, 宠物, 音乐', '鲸落馈万物，学海勇扬帆', NULL, 'tx_nan6.tmp', 'bj6.tmp', '2001-02-14', '2022-03-20 00:00:00');
INSERT INTO `user` VALUES (14890123456, 'Li11111111111111', '枕上雪', 1, '男', '旅游, 摄影, 影视, 绘画, 运动', '雪落枕边思，食旅写情诗', NULL, 'tx_nan13.tmp', 'bj13.tmp', '1996-09-05', '2018-07-21 00:00:00');
INSERT INTO `user` VALUES (18234567890, 'Li11111111111111', '踏歌行', 1, '男', '绘画, 摄影, 游戏, 音乐, 明星', '踏歌山水间，影画记流年', NULL, 'tx_nan14.tmp', 'bj14.tmp', '1999-05-20', '2020-10-14 00:00:00');
INSERT INTO `user` VALUES (15789012345, 'Li11111111111111', '云间鹤', 1, '男', '护肤, 彩妆, 绘画, 摄影, 时尚', '鹤舞云间处，音画追星光', NULL, 'tx_nan15.tmp', 'bj15.tmp', '2000-04-13', '2021-06-19 00:00:00');
INSERT INTO `user` VALUES (13256789014, 'Li11111111111111', '晚风吻尽', 1, '女', '家居, 母婴, 美食, 旅游, 手工', '晚风吻尽处，美妆绘时尚', NULL, 'tx_nv1.tmp', 'bj16.tmp', '1998-08-16', '2020-01-08 00:00:00');
INSERT INTO `user` VALUES (15890123456, 'Li11111111111111', '渡鹤影', 1, '女', '宠物, 音乐, 舞蹈, 影视, 绘画', '鹤影渡星河，巧手筑暖家', NULL, 'tx_nv2.tmp', 'bj17.tmp', '1999-02-23', '2020-05-15 00:00:00');
INSERT INTO `user` VALUES (17901234567, 'Li11111111111111', '揽星河', 1, '女', '音乐, 舞蹈, 摄影, 旅游, 情感', '揽尽星河梦，艺海绘萌情', NULL, 'tx_nv3.tmp', 'bj18.tmp', '2000-07-09', '2021-08-22 00:00:00');
INSERT INTO `user` VALUES (12678901234, 'Li11111111111111', '独行客', 1, '女', '美食, 旅游, 家居, 摄影, 母婴', '独行天地间，情韵舞流年', NULL, 'tx_nv4.tmp', 'bj19.tmp', '1997-10-12', '2019-09-05 00:00:00');
INSERT INTO `user` VALUES (16345678901, 'Li11111111111111', '人间惊鸿客', 1, '女', '旅游, 摄影, 运动, 音乐', '惊鸿踏人间，镜头记甘甜', NULL, 'tx_nv5.tmp', 'bj20.tmp', '1998-04-28', '2020-11-18 00:00:00');
INSERT INTO `user` VALUES (13111111111, 'Li11111111111111', '逸舟', 1, '女', '绘画, 游戏, 机车, 摄影', '逸舟逐浪，摄下运动旋律', NULL, 'tx_nv6.tmp', 'bj21.tmp', '1997-02-11', '2019-07-11 00:00:00');
INSERT INTO `user` VALUES (13222222222, 'Li11111111111111', '墨影', 1, '女', '音乐, 健身, 旅游, 明星', '墨影绘梦，机车驰骋光影', NULL, 'tx_nv7.tmp', 'bj22.tmp', '1998-04-18', '2020-03-18 00:00:00');
INSERT INTO `user` VALUES (13333333333, 'Li11111111111111', '听风吟', 1, '女', '舞蹈, 绘画, 运动, 游戏', '听风吟曲，健行追星之旅', NULL, 'tx_nv8.tmp', 'bj23.tmp', '1999-06-25', '2021-02-25 00:00:00');
INSERT INTO `user` VALUES (13444444444, 'Li11111111111111', '鹤羽', 1, '女', '美食, 旅游, 摄影, 家居', '鹤羽蹁跹，绘写运动游戏', NULL, 'tx_nv9.tmp', 'bj24.tmp', '2000-08-03', '2022-01-03 00:00:00');
INSERT INTO `user` VALUES (13555555555, 'Li11111111111111', '月隐', 1, '女', '游戏, 学习, 摄影, 汽车', '月隐寻味，摄下家居旅韵', NULL, 'tx_nv10.tmp', 'bj25.tmp', '1996-10-10', '2018-05-10 00:00:00');
INSERT INTO `user` VALUES (13666666666, 'Li11111111111111', '鲸跃', 1, '女', '露营, 摄影, 音乐, 宠物', '鲸跃学海，摄下汽车游戏', NULL, 'tx_nv11.tmp', 'bj26.tmp', '1997-12-17', '2019-08-17 00:00:00');
INSERT INTO `user` VALUES (13777777777, 'Li11111111111111', '松韵', 1, '女', '美食, 旅游, 家居, 摄影, 母婴', '松韵悠悠，摄录露营萌宠', NULL, 'tx_nv12.tmp', 'bj27.tmp', '1998-01-24', '2020-06-24 00:00:00');
INSERT INTO `user` VALUES (13888888888, 'Li11111111111111', '鹿鸣', 1, '女', '游戏,  学习,  运动,  社交,  汽车', '鹿鸣旷野，追星运动之旅', NULL, 'tx_nv13.tmp', 'bj28.tmp', '1999-03-31', '2021-05-31 00:00:00');
INSERT INTO `user` VALUES (13999999999, 'Li11111111111111', '星澜', 1, '女', '露营, 摄影, 绘画, 宠物 , 音乐', '星澜绘墨，书就家居光影', NULL, 'tx_nv14.tmp', 'bj29.tmp', '2000-05-08', '2022-04-08 00:00:00');
INSERT INTO `user` VALUES (14000000000, 'Li11111111111111', '拾光', 1, '女', '机车, 旅游,  运动, 汽车,  明星', '拾光逐健，奏响时尚乐音', NULL, 'tx_nv15.tmp', 'bj30.tmp', '1996-11-15', '2018-04-15 00:00:00');
INSERT INTO `user` VALUES (14111111111, 'Li11111111111111', '雾行', 1, '女', '绘画, 摄影, 书法, 音乐, 家居', '雾行探路，车影记录旅途', NULL, 'tx_nv16.tmp', 'bj31.tmp', '1997-09-22', '2019-07-22 00:00:00');
INSERT INTO `user` VALUES (14222222222, 'Li11111111111111', '晓星', 1, '女', '游戏，影视，绘画，社交', '晓星破晓，绘影游戏社交', NULL, 'tx_nv17.tmp', 'bj32.tmp', '1998-11-29', '2020-10-29 00:00:00');
INSERT INTO `user` VALUES (1, '1', '管理员123', 1, '男', '社交', '于生活细微处采撷光芒，在这方天地分享喜乐，愿成为你记录生活、连接他人的温暖纽带。', '', '1747290415617_temp2507125101942420749.tmp', 'image_background.tmp', '1920-01-01', '2025-05-15 13:51:09');

SET FOREIGN_KEY_CHECKS = 1;
