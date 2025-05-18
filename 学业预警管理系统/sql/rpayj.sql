/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50710
Source Host           : localhost:3306
Source Database       : rpayj

Target Server Type    : MYSQL
Target Server Version : 50710
File Encoding         : 65001

Date: 2023-04-27 18:18:00
*/
create
database RPAyj;
use
RPAyj;
SET
FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app_menu
-- ----------------------------
DROP TABLE IF EXISTS `app_menu`;
CREATE TABLE `app_menu`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id24',
    `name`        varchar(20)  DEFAULT NULL COMMENT '菜单名称',
    `href`        varchar(255) DEFAULT NULL COMMENT '路由地址',
    `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间4',
    `update_time` varchar(255) DEFAULT NULL COMMENT '更新时间4',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=220 DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- ----------------------------
-- Records of app_menu
-- ----------------------------
INSERT INTO `app_menu`
VALUES ('207', '菜单管理', 'login_href/idx_app_menu', '2023-02-25 21:07:19', '2023-02-25 21:07:19');
INSERT INTO `app_menu`
VALUES ('208', '学生管理', 'login_href/idx_app_user', '2023-02-25 21:07:19', '2023-02-25 21:07:19');
INSERT INTO `app_menu`
VALUES ('209', '班级管理', 'login_href/idx_class_manage', '2023-02-25 21:07:19', '2023-02-25 21:07:19');
INSERT INTO `app_menu`
VALUES ('210', '课程管理', 'login_href/idx_course_manage', '2023-02-25 21:07:19', '2023-02-25 21:07:19');
INSERT INTO `app_menu`
VALUES ('211', '权限管理', 'login_href/idx_jurisdiction_manage', '2023-02-25 21:07:19', '2023-02-25 21:07:19');
INSERT INTO `app_menu`
VALUES ('212', '公告管理', 'login_href/idx_news_manage', '2023-02-25 21:07:19', '2023-02-25 21:07:19');
INSERT INTO `app_menu`
VALUES ('213', '成绩管理', 'login_href/idx_result_manage', '2023-02-25 21:07:19', '2023-02-25 21:07:19');
INSERT INTO `app_menu`
VALUES ('214', '角色管理', 'login_href/idx_role_manage', '2023-02-25 21:07:19', '2023-02-25 21:07:19');
INSERT INTO `app_menu`
VALUES ('215', '学年管理', 'login_href/idx_session_manage', '2023-02-25 21:07:19', '2023-02-25 21:07:19');
INSERT INTO `app_menu`
VALUES ('216', '专业管理', 'login_href/idx_special_subject', '2023-02-25 21:07:19', '2023-02-25 21:07:19');
INSERT INTO `app_menu`
VALUES ('217', '违纪处分管理', 'login_href/idx_stu_violation', '2023-02-25 21:07:19', '2023-02-25 21:07:19');
INSERT INTO `app_menu`
VALUES ('218', '用户管理', 'login_href/idx_sys_user', '2023-02-25 21:07:19', '2023-02-25 21:07:19');
INSERT INTO `app_menu`
VALUES ('219', '违纪处分类型', 'login_href/idx_violation_type', '2023-02-25 21:07:19', '2023-02-25 21:07:19');

-- ----------------------------
-- Table structure for app_user
-- ----------------------------
DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user`
(
    `id`                 int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id24',
    `username`           varchar(50)  DEFAULT NULL COMMENT '用户名',
    `password`           varchar(50)  DEFAULT NULL COMMENT '密码',
    `nickname`           varchar(50)  DEFAULT NULL COMMENT '姓名',
    `sex`                int(2) DEFAULT NULL COMMENT '性别',
    `phone`              varchar(20)  DEFAULT NULL COMMENT '联系方式',
    `CET_4`              int(2) DEFAULT NULL COMMENT '四级',
    `class_manage_id`    int(11) DEFAULT NULL COMMENT '班级',
    `special_subject_id` int(11) DEFAULT NULL COMMENT '专业',
    `early_warning`      int(2) DEFAULT NULL COMMENT '预警（正常/一级/二级/三级）',
    `id_card`            varchar(255) DEFAULT NULL COMMENT '身份证号',
    `dormitory_number`   varchar(255) DEFAULT NULL COMMENT '宿舍号',
    `credit`             varchar(255) DEFAULT NULL COMMENT '学分',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='学生管理';

-- ----------------------------
-- Records of app_user
-- ----------------------------
INSERT INTO `app_user`
VALUES ('1', '20191122', '1', '张三', '1', '15802030203', '2', '2', '2', '1', null, null, '0.0');
INSERT INTO `app_user`
VALUES ('8', '20191234', '1', '李德生', '1', '18977889111', '2', '1', '1', '4', null, null, '13.25');
INSERT INTO `app_user`
VALUES ('9', '20201234', '1', '张珊珊', '2', '18912345698', '1', '2', '2', '1', null, null, '0.0');
INSERT INTO `app_user`
VALUES ('10', 'admin', '1', '1', '1', '111', null, null, null, '1', '1', '1', '0.0');
INSERT INTO `app_user`
VALUES ('11', 'admin', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '0.0');
INSERT INTO `app_user`
VALUES ('12', '20230427', '1', 'wxl', '1', '13837520190', '1', '1', '1', '2', '410403199603225654', '604', '30.0');

-- ----------------------------
-- Table structure for class_manage
-- ----------------------------
DROP TABLE IF EXISTS `class_manage`;
CREATE TABLE `class_manage`
(
    `id`                 int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
    `name`               varchar(255) DEFAULT NULL COMMENT '班级名称',
    `is_graduate`        int(2) DEFAULT NULL COMMENT '是否毕业班',
    `special_subject_id` int(11) DEFAULT NULL COMMENT '所属专业',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='班级管理';

-- ----------------------------
-- Records of class_manage
-- ----------------------------
INSERT INTO `class_manage`
VALUES ('1', '2023级计科1班', '1', '1');
INSERT INTO `class_manage`
VALUES ('2', '2024级软件工程1班', '2', '2');
INSERT INTO `class_manage`
VALUES ('3', '2019级计科2班', '1', '1');

-- ----------------------------
-- Table structure for course_manage
-- ----------------------------
DROP TABLE IF EXISTS `course_manage`;
CREATE TABLE `course_manage`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
    `name`        varchar(255) DEFAULT NULL COMMENT '课程名称',
    `credit`      int(3) DEFAULT NULL COMMENT '学分',
    `grade_point` float(5, 2
) DEFAULT NULL COMMENT '绩点',
  `content` varchar(2000) DEFAULT NULL COMMENT '课程简介',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `course_code` varchar(255) DEFAULT NULL COMMENT '课程编码',
  `planned_term` varchar(255) DEFAULT NULL COMMENT '计划学期',
  `commencement_term` varchar(255) DEFAULT NULL COMMENT '开课学期',
  `course_type` varchar(255) DEFAULT NULL COMMENT '课程类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='课程管理';

-- ----------------------------
-- Records of course_manage
-- ----------------------------
INSERT INTO `course_manage`
VALUES ('1', '计算机基础与应用', '4', '2.00',
        '介绍计算机基础知识，包括计算机的发展与应用、计算机中数据的表示方法、计算机硬件系统与软件系统、微型计算机的硬件系统、多媒体技术基础知识以及虚拟现实技术等内容。',
        '2023-02-25 23:02:02', null, null, null, null);
INSERT INTO `course_manage`
VALUES ('2', 'C语言程序设计', '4', '2.00', 'C语言程序设计', '2023-02-25 23:03:38', null, null, null, null);
INSERT INTO `course_manage`
VALUES ('3', '计算机组成原理', '2', '1.00', '计算机组成原理', '2023-02-25 23:04:24', null, null, null, null);
INSERT INTO `course_manage`
VALUES ('4', '微机原理及汇编语言', '2', '1.00', '微机原理及汇编语言', '2023-02-25 23:04:54', null, null, null, null);
INSERT INTO `course_manage`
VALUES ('5', '分布式系统', '2', '1.00', '分布式系统', '2023-02-25 23:05:05', null, null, null, null);
INSERT INTO `course_manage`
VALUES ('6', '软件项目管理', '2', '1.00', '软件项目管理', '2023-02-25 23:05:14', null, null, null, null);
INSERT INTO `course_manage`
VALUES ('7', 'Oracle数据库系统', '3', '1.50', 'Oracle数据库系统', '2023-02-25 23:05:23', null, null, null, null);
INSERT INTO `course_manage`
VALUES ('8', '高级语言程序设计', '4', '2.00', '高级语言程序设计', '2023-02-25 23:05:37', null, null, null, null);
INSERT INTO `course_manage`
VALUES ('9', '面向对象程序设计', '3', '1.50', '面向对象程序设计', '2023-02-25 23:05:52', null, null, null, null);
INSERT INTO `course_manage`
VALUES ('10', '1', '1', null, '3', '2023-04-26 11:42:58', '1', '2', '2', '1');
INSERT INTO `course_manage`
VALUES ('11', '2', '2', null, '2', '2023-04-26 14:16:44', '2', '2', '2', '2');

-- ----------------------------
-- Table structure for jurisdiction_manage
-- ----------------------------
DROP TABLE IF EXISTS `jurisdiction_manage`;
CREATE TABLE `jurisdiction_manage`
(
    `id`             int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID24',
    `role_manage_id` int(11) DEFAULT NULL COMMENT '角色ID',
    `app_menu_tree`  text COMMENT '菜单权限集合',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='权限管理';

-- ----------------------------
-- Records of jurisdiction_manage
-- ----------------------------
INSERT INTO `jurisdiction_manage`
VALUES ('1', '1', '207,208,209,210,211,212,213,214,215,216,217,218,219');
INSERT INTO `jurisdiction_manage`
VALUES ('5', '4', '208,210,212,213,217');

-- ----------------------------
-- Table structure for news_manage
-- ----------------------------
DROP TABLE IF EXISTS `news_manage`;
CREATE TABLE `news_manage`
(
    `id`              int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
    `title`           varchar(255) DEFAULT NULL COMMENT '标题',
    `content_details` longtext COMMENT '内容',
    `create_time`     varchar(20)  DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='公告管理';

-- ----------------------------
-- Records of news_manage
-- ----------------------------
INSERT INTO `news_manage`
VALUES ('1', '关于做好2022—2023学年第二学期本科教学有关工作的通知',
        '<p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px;\"><span style=\"font-family: 仿宋; font-size: 21px;\">各学院（部、中心），全体本科生：</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; text-align: justify; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋; font-size: 21px;\">根据学校安排，为做好春季学期教学工作，现将有关安排通知如下：</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 黑体; font-size: 21px;\">一、2022—2023学年第一学期考试安排</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">1.</span><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">上学期期末考试时间安排。全校考试原则上安排在2023年2月13—19日和教学校历第一、二周周末2023年2月25—26日，3月4—5日（个别另有调整的由开课学院安排）。学生具体考试以“综合教学服务平台”中发布的时间及地点为准。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; text-indent: 43px;\"><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">2.</span><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">试题送印。各学院组织任课教师在开考前10天做好试题送印工作。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">3.</span><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">监考安排。各学院于开考前1周在“综合教学服务平台”的考试批次中完成监考安排，并组织好考务工作。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">4.</span><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">组织辅导答疑。为了帮助同学们做好复习备考工作，大面积公共基础课程由开课学院组织任课教师以精讲、串讲的形式，开展针对性地辅导和答疑，具体安排见教务处主页。其他课程由任课教师根据情况自行安排。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">5.</span><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">组织考试巡查。各开课学院组织落实好每一门考试的考务工作，督促监考老师严抓考试纪律，严防学生违纪违规，并针对考试秩序进行专项检查。教务处将组织教学督导、教学工作人员做好考试巡查工作。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">6.</span><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">补考（缓考）安排。学生因病等原因无法按期返校者、代表学校参赛者可在“综合教学服务平台”申请缓考。待开学初所有初考完成、任课教师将成绩录入后，由教务处统一生成补考（缓考）课程，各开课学院进行补考（缓考）安排。补考时间预计安排在2023年3月底到4月中上旬。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 黑体; font-size: 21px;\">二、2022—2023学年第二学期上课教学安排</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">1.2</span><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">月20日起，学生按照教学校历和教务处“综合教学服务平台”中的课表安排正常线下上课。因故未按时返校学生可通过在线平台（http://class.xjtu.edu.cn/）学习。任课教师也可同步使用长江雨课堂、腾讯课堂等第三方平台进行授课。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">2.</span><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">继续利用思源学堂（syxt.xjtu.edu.cn）开展课前、课后教学活动。课程主讲教师应提前通过思源学堂发布课程公告、<span style=\"color: rgb(192, 0, 0);\">课程联系群</span>、课程大纲、教学日历、考核方式、教学资源，布置并批改作业，发布过程考核成绩等，落实完整的教学过程，保证教学质量。教务处将继续开展课程建设质量情况检查工作。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">3.</span><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">做好学生考勤工作。请任课教师用好电子考勤系统，做好学生出勤情况统计和缺勤、请假记录（电子考勤使用网址：bkkq.xjtu.edu.cn）。任课教师可在教务系统“我的教学任务”中查看学生到校情况，学生名单中“已注册”的为已返校学生。改选课程的学生，电子考勤自改选之日起统计。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">4.</span><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">做好教学秩序检查和教学服务保障。各学院组织落实好每一门课的教学保障工作，督促任课教师严守课堂教学纪律，严防发生教学事故。教务处将组织教学督导、教学工作人员做好开学教学秩序检查工作。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; text-align: justify; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 黑体; font-size: 21px;\">三、实验实践类课程教学安排</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; text-align: justify; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋; font-size: 21px;\">2</span><span style=\"font-family: 仿宋; font-size: 21px;\">月20日（第一周）起开展2022-2023学年第二学期实验实践教学工作。同时，于3月12日（第三周）前完成2022-2023学年第一学期未结课实验课程的教学、考核及成绩登录工作。因故未按时返校的学生，待返校后联系各实验教学中心补上实验课程。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; text-align: justify; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋; font-size: 21px;\">各实验教学中心提前做好实验教学准备，完成实验室环境检查、教学任务安排、实验设备及实验材料准备等工作，确保实验教学活动有序开展。要讲好、学好实验室安全教育第一课，严格遵守并执行实验室的各项规章制度和操作规程。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; text-align: justify; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 黑体; font-size: 21px;\">四、其他说明</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; text-align: justify; line-height: 37px; text-indent: 43px;\"><strong><span style=\"font-family: 仿宋; font-size: 21px;\">1.</span></strong><strong><span style=\"font-family: 仿宋; font-size: 21px;\">做好个人防护。</span></strong><span style=\"font-family: 仿宋; font-size: 21px;\">请全体师生做好个人防护和健康管理，科学规范佩戴口罩，保持健康卫生的生活习惯。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 36px; text-indent: 43px;\"><strong><span style=\"font-family: 仿宋; font-size: 21px;\">2.</span></strong><strong><span style=\"font-family: 仿宋; font-size: 21px;\">开学初组织第三轮选课（退选、改选）。</span></strong><span style=\"font-family: 仿宋_GB2312; font-size: 21px;\">教务处拟于2月16日至2月27日继续开展第三轮选课，请同学及时关注并按时完成退、改选。</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 37px; text-indent: 43px;\"><br/></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; text-align: right; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋; font-size: 21px;\">教务处</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; text-align: right; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋; font-size: 21px;\">实践教学中心</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; text-align: right; line-height: 37px; text-indent: 43px;\"><span style=\"font-family: 仿宋; font-size: 21px;\">网信中心</span></p><p style=\"margin-top: 0px; margin-bottom: 0px; border: medium none; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; text-align: right; line-height: 37px;\"><span style=\"font-family: 仿宋; font-size: 21px;\">2023</span><span style=\"font-family: 仿宋; font-size: 21px;\">年2月9日</span></p><p><br/></p><p><br/></p>',
        '2023-02-25 22:35:54');
INSERT INTO `news_manage`
VALUES ('2', '2023年全国大学生英语竞赛报名通知',
        '<p style=\"border: medium none; margin-top: 21px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px;\"><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">各位同学：</span></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">由国际英语外语教师协会、中国英语外语教师协会和高等学校大学外语教学研究会联合主办的</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">2023年全国大学生英语竞赛初赛将于</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">2</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">023</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">年</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">5月7日举行。现将竞赛有关事宜通知如下：</span></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px;\"><strong><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">一、命题范围及题型：</span></strong></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">初赛和决赛命题将依据《非英语专业研究生英语教学大纲》、《高等学校英语专业英语教学大纲》、《大学英语课程要求（试行）》等文件，并借鉴国内外最新的测试理论和命题技术、方法，既参考现行各种大学英语主要教材，又不依据任何一种教材。初、决赛笔试满分均为</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">150分(建构反应题型占90分，选择反应题型占60</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">分</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">)，其中听力均为30分。决赛口试满分为50分。</span></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px;\"><strong><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">二、竞赛对象：</span></strong></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">竞赛分为</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">A、B、C、D四个类别，A类考试适用于研究生参加；B类考试适用于英语专业本、专科生参加；C类考试适用于非英语专业本科生参加；D类考试适用于体育类和艺术类本科生和非英语专业高</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">职高专类学生参加。若D类考生报名人数少于2</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">0</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">人，则将</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">D</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">类考生统一转为C类参加考试。跨类别报名成绩无效。</span></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px;\"><strong><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">三、竞赛时间：</span></strong></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">本竞赛分初赛和决赛两个阶段进行。均为全国统一命题。</span></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">初赛：</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">2023年5月7日</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">（星期日）上午</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">9</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">:</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">00—11</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">:</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">00</span></strong></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">决赛：</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">2023年6月3日</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">（星期六）上午</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">9</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">:</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">00—11</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">:</span></strong><strong><span style=\"color: rgb(255, 0, 0); line-height: 24px; font-family: 宋体; font-size: 16px;\">00</span></strong></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px;\"><strong><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">四、奖励办法：</span></strong></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">本次竞赛设四种奖励等级：特等奖、一等奖、二等奖和三等奖。二等奖和三等奖通过初赛产生，特等奖和一等奖通过决赛产生。获奖学生由全国竞赛组委会颁发获奖证书。</span></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px;\"><strong><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">五、报名须知：</span></strong></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">报名费</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">50元/人,采用网上报名及缴费方式，</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">请用电脑浏览器登录以下网址</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">：</span></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">http://event.icrp.xjtu.edu.cn/116320520/index?pageId=116320570</span></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><strong><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">报名时间：即日起至</span></strong><strong><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">20</span></strong><strong><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">2</span></strong><strong><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">3年4月9日</span></strong><strong><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">24</span></strong><strong><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">:00</span></strong></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">后续比赛相关通知请注意查看教务处主页公告。</span></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">联系电话：俞老师</span>&nbsp;<span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">029-</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">82663962</span></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px;\"><strong><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">六、辅导用书：</span></strong></p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; font-family: 微软雅黑; font-size: 12px; white-space: normal; line-height: 18px; text-indent: 32px;\"><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">各位同学可扫描下方二维码自行订购订全国竞赛辅导用书：</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">2023年沈阳出版社新版大</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">学《英语奥林匹克》丛书和《全国大学生英语竞赛真题及解析》。</span><span style=\"line-height: 24px; font-family: 宋体; font-size: 16px;\">学校不统一组织。</span></p><p><br/></p>',
        '2023-02-25 22:37:43');
INSERT INTO `news_manage`
VALUES ('3', '关于2023届毕业班图像信息采集期间课程安排的通知',
        '<p class=\"vsbcontent_start\" style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; line-height: 33.6px; font-size: 14pt; font-weight: bold; font-family: 微软雅黑; white-space: normal;\">各学院（部、中心），有关单位：</p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; line-height: 33.6px; text-indent: 2em; font-size: 14pt; font-family: 微软雅黑; white-space: normal;\">根据陕西省教育厅及新华通讯社陕西分社的通知和时间安排，我校2023届普通本科毕业生图像信息采集工作于2023年3月6-7日进行。为保证毕业班学生信息采集的顺利进行，凡与拍摄时间段有冲突的课程暂时停课（由任课教师在教务系统调课管理模块中做停课申请），并请学院联系教务处备案停课信息，另行安排补课。</p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; line-height: 33.6px; text-indent: 2em; font-size: 14pt; font-family: 微软雅黑; white-space: normal;\">附件:西安交通大学2023届毕业生图像采集安排表</p><p style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; line-height: 33.6px; text-indent: 2em; font-size: 14pt; font-family: 微软雅黑; white-space: normal; text-align: right;\">教务处</p><p class=\"vsbcontent_end\" style=\"border: medium none; margin-top: 0px; margin-bottom: 0px; padding: 0px; line-height: 33.6px; font-size: 14pt; text-align: right; font-family: 微软雅黑; white-space: normal;\">2023年2月14日</p><p><br/></p>',
        '2023-02-25 22:37:59');

-- ----------------------------
-- Table structure for result_manage
-- ----------------------------
DROP TABLE IF EXISTS `result_manage`;
CREATE TABLE `result_manage`
(
    `id`                int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
    `session_manage_id` int(11) DEFAULT NULL COMMENT '学年',
    `course_manage_id`  int(11) DEFAULT NULL COMMENT '课程',
    `score`             int(3) DEFAULT NULL COMMENT '成绩',
    `grade_point`       float(4, 2
) DEFAULT NULL COMMENT '获得绩点',
  `a_type` int(2) DEFAULT NULL COMMENT '类型（核心/必修/选修）',
  `app_user_id` int(11) DEFAULT NULL COMMENT '学生',
  `class_manage_id` int(11) DEFAULT NULL COMMENT '班级',
  `special_subject_id` int(11) DEFAULT NULL COMMENT '专业',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `credit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='成绩管理';

-- ----------------------------
-- Records of result_manage
-- ----------------------------
INSERT INTO `result_manage`
VALUES ('1', '1', '5', '62', '1.00', '1', '8', '1', '1', '2023-02-25 23:16:42', '1');
INSERT INTO `result_manage`
VALUES ('2', '2', '1', '86', '3.50', '2', '8', '1', '1', '2023-02-25 23:19:11', '1');
INSERT INTO `result_manage`
VALUES ('3', '1', '2', '88', '3.50', '2', '8', '1', '1', '2023-02-25 23:19:49', '1');
INSERT INTO `result_manage`
VALUES ('4', '2', '6', '90', '4.00', '2', '8', '1', '1', '2023-02-25 23:22:17', '1');
INSERT INTO `result_manage`
VALUES ('5', '1', '7', '48', '0.00', '2', '8', '1', '1', '2023-02-25 23:22:34', '1');
INSERT INTO `result_manage`
VALUES ('6', '3', '8', '75', '2.50', '1', '8', '1', '1', '2023-02-25 23:23:01', '1');
INSERT INTO `result_manage`
VALUES ('7', '3', '4', '89', '3.50', '2', '8', '1', '1', '2023-02-26 01:44:08', '1');
INSERT INTO `result_manage`
VALUES ('8', '1', '1', '11', '11.00', '1', '8', '1', '1', '2023-04-26 16:10:48', '1');
INSERT INTO `result_manage`
VALUES ('9', '3', '1', '11', null, '1', '8', '1', '1', '2023-04-27 08:44:41', '1');
INSERT INTO `result_manage`
VALUES ('10', '3', '1', '22', null, '1', '8', '1', '1', '2023-04-27 08:44:57', '1');
INSERT INTO `result_manage`
VALUES ('11', '3', '1', '11', null, '1', '8', '1', '1', '2023-04-27 08:44:41', '1');
INSERT INTO `result_manage`
VALUES ('12', '3', '1', '11', null, '1', '8', '1', '1', '2023-04-27 08:44:41', '1');
INSERT INTO `result_manage`
VALUES ('13', '3', '1', '11', null, '1', '8', '1', '1', '2023-04-27 08:44:41', '1');
INSERT INTO `result_manage`
VALUES ('14', '3', '1', '11', null, '1', '8', '1', '1', '2023-04-27 08:44:41', '1');
INSERT INTO `result_manage`
VALUES ('15', '3', '1', '11', null, '1', '8', '1', '1', '2023-04-27 08:44:41', '1');
INSERT INTO `result_manage`
VALUES ('16', '3', '4', '10', null, '1', '8', '1', '1', '2023-04-27 11:46:34', '2');
INSERT INTO `result_manage`
VALUES ('17', '1', '3', '20', null, '1', '8', '1', '1', '2023-04-27 15:28:16', '2');
INSERT INTO `result_manage`
VALUES ('18', '3', '1', '60', null, '1', '12', '1', '1', '2023-04-27 18:11:31', '4');
INSERT INTO `result_manage`
VALUES ('19', '3', '1', '10', null, '1', '12', '1', '1', '2023-04-27 18:11:43', '4');
INSERT INTO `result_manage`
VALUES ('20', '3', '1', '20', null, '1', '12', '1', '1', '2023-04-27 18:12:05', '4');

-- ----------------------------
-- Table structure for role_manage
-- ----------------------------
DROP TABLE IF EXISTS `role_manage`;
CREATE TABLE `role_manage`
(
    `id`        int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID24',
    `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='角色管理';

-- ----------------------------
-- Records of role_manage
-- ----------------------------
INSERT INTO `role_manage`
VALUES ('1', '超级管理员');
INSERT INTO `role_manage`
VALUES ('4', '教师');

-- ----------------------------
-- Table structure for session_manage
-- ----------------------------
DROP TABLE IF EXISTS `session_manage`;
CREATE TABLE `session_manage`
(
    `id`           int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
    `name`         varchar(255) DEFAULT NULL COMMENT '学年',
    `order_number` int(2) DEFAULT NULL COMMENT '顺序',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='学年管理';

-- ----------------------------
-- Records of session_manage
-- ----------------------------
INSERT INTO `session_manage`
VALUES ('1', '2021学年', '1');
INSERT INTO `session_manage`
VALUES ('2', '2022学年', '2');
INSERT INTO `session_manage`
VALUES ('3', '2023学年', '3');
INSERT INTO `session_manage`
VALUES ('4', '2020学年', '5');

-- ----------------------------
-- Table structure for special_subject
-- ----------------------------
DROP TABLE IF EXISTS `special_subject`;
CREATE TABLE `special_subject`
(
    `id`   int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
    `name` varchar(255) DEFAULT NULL COMMENT '专业',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='专业管理';

-- ----------------------------
-- Records of special_subject
-- ----------------------------
INSERT INTO `special_subject`
VALUES ('1', '计算机科学与技术');
INSERT INTO `special_subject`
VALUES ('2', '软件工程');
INSERT INTO `special_subject`
VALUES ('3', '信息安全');

-- ----------------------------
-- Table structure for stu_violation
-- ----------------------------
DROP TABLE IF EXISTS `stu_violation`;
CREATE TABLE `stu_violation`
(
    `id`                int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
    `violation_type_id` int(11) DEFAULT NULL COMMENT '违纪类型',
    `content`           varchar(2000) DEFAULT NULL COMMENT '处分结果',
    `app_user_id`       int(11) DEFAULT NULL COMMENT '学生',
    `create_time`       varchar(20)   DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='违纪处分管理';

-- ----------------------------
-- Records of stu_violation
-- ----------------------------
INSERT INTO `stu_violation`
VALUES ('1', '1', '作弊', '8', '2023-02-25 23:23:18');
INSERT INTO `stu_violation`
VALUES ('2', '2', '翻墙', '1', '2023-02-25 23:23:26');
INSERT INTO `stu_violation`
VALUES ('3', '2', '奇装异服', '9', '2023-02-26 01:44:33');
INSERT INTO `stu_violation`
VALUES ('4', '1', '122', '8', '2023-04-27 17:48:12');
INSERT INTO `stu_violation`
VALUES ('5', '1', '1212', '11', '2023-04-27 17:51:17');
INSERT INTO `stu_violation`
VALUES ('6', '2', null, '8', '2023-04-27 17:52:34');
INSERT INTO `stu_violation`
VALUES ('7', '1', '1212', '8', '2023-04-27 17:53:09');
INSERT INTO `stu_violation`
VALUES ('8', '1', '1', '12', '2023-04-27 18:12:26');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`                     int(11) NOT NULL AUTO_INCREMENT COMMENT '主键24',
    `USERNAME`               varchar(50) NOT NULL COMMENT '用户名',
    `PASSWORD`               varchar(50) NOT NULL COMMENT '密码',
    `NAME`                   varchar(50)  DEFAULT NULL COMMENT '姓名',
    `jurisdiction_manage_id` int(11) NOT NULL COMMENT '角色',
    `create_time`            varchar(20) NOT NULL COMMENT '建立时间4',
    `update_time`            varchar(20)  DEFAULT NULL COMMENT '更新时间4',
    `signature`              varchar(255) DEFAULT NULL COMMENT '备注',
    `sex`                    int(2) DEFAULT NULL COMMENT '性别',
    `phone`                  varchar(20)  DEFAULT NULL COMMENT '联系电话',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COMMENT='用户管理';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES ('35', 'admin', '1', '管理员', '1', '2022-06-29 00:00:00', '', '', null, null);
INSERT INTO `sys_user`
VALUES ('36', 'js1', '1', '张老师', '5', '2023-02-25 22:46:17', null, null, '1', '18912345678');
INSERT INTO `sys_user`
VALUES ('37', 'js2', '1', '李方军', '5', '2023-02-25 22:46:35', null, null, '1', '18966554411');

-- ----------------------------
-- Table structure for violation_type
-- ----------------------------
DROP TABLE IF EXISTS `violation_type`;
CREATE TABLE `violation_type`
(
    `id`   int(11) NOT NULL AUTO_INCREMENT COMMENT '序号',
    `name` varchar(255) DEFAULT NULL COMMENT '类型',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='违纪处分类型';

-- ----------------------------
-- Records of violation_type
-- ----------------------------
INSERT INTO `violation_type`
VALUES ('1', '考试违纪');
INSERT INTO `violation_type`
VALUES ('2', '违反校规');
