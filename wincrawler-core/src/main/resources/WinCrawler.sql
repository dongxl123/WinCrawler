/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50616
Source Host           : testwinbx.mysql.rds.aliyuncs.com:3306
Source Database       : win_crawler

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2019-07-10 15:22:19
*/
-- ----------------------------
-- Table structure for crawler_result
-- ----------------------------
CREATE TABLE `crawler_result`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time` timestamp  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `task_id`     bigint(20)      DEFAULT NULL COMMENT '任务ID',
    `key_name`    varchar(180)    DEFAULT NULL COMMENT '结果key_name, k1_v1-k2_v2...',
    `params`      varchar(1024)   DEFAULT NULL COMMENT '参数内容, json格式',
    `result`      longtext COMMENT '结果内容, json格式',
    `msg`         varchar(2048)   DEFAULT NULL COMMENT '记录关键信息或异常信息',
    `deleted`     bit(1)          DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_task_id_key_name` (`task_id`, `key_name`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_task_id` (`task_id`),
    KEY `idx_key_name` (`key_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='爬虫结果';

-- ----------------------------
-- Table structure for crawler_task
-- ----------------------------
CREATE TABLE `crawler_task`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time` timestamp  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `name`        varchar(100)    DEFAULT NULL COMMENT '任务名称',
    `description` varchar(4096)   DEFAULT NULL COMMENT '描述',
    `params`      varchar(4096)   DEFAULT NULL,
    `template_id` bigint(20) NOT NULL COMMENT '模板ID',
    `merged_task_id` bigint(20) DEFAULT NULL COMMENT '数据合并到指定的taskId中',
    `export_config`  text COMMENT '不配置时，默认使用crawler_template的export_config',
    `deleted`     bit(1)          DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
    PRIMARY KEY (`id`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_name` (`name`),
    KEY `idx_template_id` (`template_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='爬虫任务';

-- ----------------------------
-- Table structure for crawler_template
-- ----------------------------
CREATE TABLE `crawler_template`
(
    `id`             bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time`    timestamp  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    timestamp  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `creator_uid`    bigint(20)      DEFAULT NULL COMMENT '创建人ID',
    `name`           varchar(100)    DEFAULT NULL COMMENT '模板名称',
    `website_id`     bigint(20)      DEFAULT NULL COMMENT '网站ID',
    `description`    varchar(4096)   DEFAULT NULL COMMENT '描述',
    `use_proxy_method` tinyint(1)          DEFAULT '0' COMMENT '使用代理方式，0:不使用，1:当请求被拦截时使用代理，2:始终使用',
    `use_proxy_condition` varchar(512) DEFAULT NULL COMMENT '使用代理或切换代理的匹配条件',
    `params`         varchar(4096)   DEFAULT NULL COMMENT '参数',
    `params_range`   varchar(4096)   DEFAULT NULL COMMENT ' 参数可选范围，json格式',
    `main_steps`     text COMMENT '步骤配置，json格式',
    `export_config`  text COMMENT '文件导出配置，json格式',
    `deleted`        bit(1)          DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
    PRIMARY KEY (`id`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='爬虫模板，一个系统对应一个模板';

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
CREATE TABLE `system_config`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `create_time` timestamp  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `key`         varchar(100)    DEFAULT NULL COMMENT '名称',
    `value`       varchar(4096)   DEFAULT NULL COMMENT '值',
    `description` varchar(4096)   DEFAULT NULL COMMENT '描述',
    `deleted`     bit(1)          DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
    PRIMARY KEY (`id`),
    KEY `idx_create_time` (`create_time`),
    KEY `idx_key` (`key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Table structure for website
-- ----------------------------
CREATE TABLE `website`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `create_time` timestamp  NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp  NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `name`        varchar(100)    DEFAULT NULL COMMENT '名称',
    `url`         varchar(500)    DEFAULT NULL COMMENT '地址',
    `username`    varchar(100)    DEFAULT NULL COMMENT '用户名',
    `password`    varchar(100)    DEFAULT NULL COMMENT '密码',
    `description` varchar(4096)   DEFAULT NULL COMMENT '描述',
    `deleted`     bit(1)          DEFAULT b'0' COMMENT '是否删除， 1:删除， 0:有效',
    PRIMARY KEY (`id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config`
VALUES ('1', '2019-06-27 19:13:09', '2019-07-15 10:09:27', 'selenium_use_proxy', '0', null, '\0');
INSERT INTO `system_config`
VALUES ('2', '2019-06-27 19:13:12', '2019-07-02 16:51:47', 'selenium_vcode_fail_retry_times', '3', null, '\0');
INSERT INTO `system_config`
VALUES ('3', '2019-07-02 14:48:59', '2019-07-02 14:56:31', 'selenium_page_load_timeout', '30000', null, '\0');
INSERT INTO `system_config`
VALUES ('4', '2019-07-02 14:57:58', '2019-07-02 17:29:52', 'selenium_implicitly_wait', '5000', null, '\0');
INSERT INTO `system_config`
VALUES ('5', '2019-07-03 12:31:00', '2019-07-11 17:06:08', 'selenium_step_sleep_time', '1000', null, '\0');
INSERT INTO `system_config`
VALUES ('6', '2019-07-10 16:57:18', '2019-07-10 17:27:41', 'selenium_headless', '0', null, '\0');
INSERT INTO `system_config`
VALUES ('7', '2019-07-10 19:03:24', '2019-07-15 11:53:26', 'ajax_use_proxy', '0', null, '\0');
INSERT INTO `system_config`
VALUES ('8', '2019-07-11 17:06:19', '2019-07-11 17:06:58', 'ajax_step_sleep_time', '0', null, '\0');
INSERT INTO `system_config`
VALUES ('9', '2019-07-11 17:06:35', '2019-07-11 17:08:09', '1', '0', null, '\0');
INSERT INTO `system_config`
VALUES ('10', '2019-07-15 12:01:51', '2019-07-15 12:02:31', 'showapi_app_id', '您的appId', null, '\0');
INSERT INTO `system_config`
VALUES ('11', '2019-07-15 12:02:09', '2019-07-15 12:02:33', 'showapi_app_secret', '您的appSecret', null, '\0');

-- ----------------------------
-- Records of crawler_template
-- ----------------------------
INSERT INTO `crawler_template`
VALUES ('1', '2019-06-28 11:28:42', '2019-07-18 09:56:34', '开发测试', NULL,
        '{\"ageList\":\"${toJSONString(range(30,70))}\",\"sexList\":[\"Sex1\",\"Sex2\"],\"payEndYearList\":\"[2,3,4]\",\"a\":\"15967126512\",\"b\":\"0000\"}\r\n',
        '1', '\0');

-- ----------------------------
-- Records of crawler_task
-- ----------------------------
INSERT INTO `crawler_task`
VALUES ('1', '2019-06-28 11:28:42', '2019-07-15 10:15:50', '测试任务',
        '{\"ageList\":\"${range(30,70)}\",\"sexList\":[\"Sex1\",\"Sex2\"],\"payEndYearList\":[2,3,4],\"a\":\"15967126512\",\"b\":\"0000\"}\r\n',
        '{\"ageList\":\"${range(30,70)}\",\"sexList\":[\"Sex1\",\"Sex2\"],\"payEndYearList\":[2,3,4],\"a\":\"15967126512\",\"b\":\"0000\"}',
        '1', '\0');
