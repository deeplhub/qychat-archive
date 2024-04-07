-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        10.11.4-MariaDB - mariadb.org binary distribution
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.2.0.6213
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 qychat_archive 的数据库结构
CREATE DATABASE IF NOT EXISTS `qychat_archive` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `qychat_archive`;

-- 导出  表 qychat_archive.qychat_chat_room 结构
CREATE TABLE IF NOT EXISTS `qychat_chat_room` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `chat_id` varchar(80) NOT NULL COMMENT '群ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `name` varchar(80) NOT NULL COMMENT '群名称',
  `notice` text DEFAULT NULL COMMENT '群公告',
  `owner` varchar(100) DEFAULT NULL COMMENT '群主ID',
  `create_time` datetime DEFAULT NULL COMMENT '群创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `chat_id` (`chat_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='微信群详情';

-- 正在导出表  qychat_archive.qychat_chat_room 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `qychat_chat_room` DISABLE KEYS */;
/*!40000 ALTER TABLE `qychat_chat_room` ENABLE KEYS */;

-- 导出  表 qychat_archive.qychat_chat_room_member 结构
CREATE TABLE IF NOT EXISTS `qychat_chat_room_member` (
  `chat_id` varchar(80) NOT NULL COMMENT '企业微信客户群ID',
  `user_id` varchar(80) NOT NULL COMMENT '成员ID',
  UNIQUE KEY `chat_id_user_id` (`chat_id`,`user_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='微信群与用户关系';

-- 正在导出表  qychat_archive.qychat_chat_room_member 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `qychat_chat_room_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `qychat_chat_room_member` ENABLE KEYS */;

-- 导出  表 qychat_archive.qychat_member 结构
CREATE TABLE IF NOT EXISTS `qychat_member` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(80) NOT NULL COMMENT '成员ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `avatar` varchar(200) DEFAULT NULL COMMENT '头像',
  `type` tinyint(2) NOT NULL COMMENT '成员类型：1企业成员，2外部联系人',
  `gender` tinyint(2) DEFAULT NULL COMMENT '性别：0-未知 1-男性 2-女性',
  `position` varchar(50) DEFAULT NULL COMMENT '外部联系人所在企业的职位-联系人类型是企业微信用户时有此字段',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `biz_mail` varchar(50) DEFAULT NULL COMMENT '企业邮箱',
  `telephone` varchar(10) DEFAULT NULL COMMENT '座机',
  `note` varchar(50) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='企业微信成员信息表';

-- 正在导出表  qychat_archive.qychat_member 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `qychat_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `qychat_member` ENABLE KEYS */;

-- 导出  表 qychat_archive.qychat_message_content 结构
CREATE TABLE IF NOT EXISTS `qychat_message_content` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `seq` bigint(20) unsigned NOT NULL COMMENT '消息记录值',
  `msgid` varchar(100) NOT NULL COMMENT '消息id，消息的唯一标识',
  `publickey_ver` int(11) NOT NULL COMMENT '公钥版本号',
  `action` varchar(20) DEFAULT NULL COMMENT '消息动作，send-发送消息,recall-撤回消息,switch-切换企业日志',
  `fromid` varchar(50) DEFAULT NULL COMMENT '消息发送方id。同一企业内容为userid，非相同企业为external_userid。消息如果是机器人发出，也为external_userid',
  `tolist` text DEFAULT NULL COMMENT '消息接收方列表，可能是多个，同一个企业内容为userid，非相同企业为external_userid。数组',
  `roomid` varchar(50) DEFAULT '' COMMENT '群聊消息的群id。如果是单聊则为空',
  `msgtime` datetime NOT NULL COMMENT '消息发送时间戳，utc时间，ms单位',
  `msgtype` varchar(20) DEFAULT NULL COMMENT '消息类型',
  `content` text DEFAULT NULL COMMENT '消息内容',
  `original_content` text DEFAULT NULL COMMENT '原始消息内容',
  `media_status` tinyint(4) DEFAULT NULL COMMENT '媒体状态：1未加载，2加载成功，3.加载失败',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `is_support` tinyint(4) DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `msgid` (`msgid`) USING BTREE,
  KEY `msgtime_roomid` (`msgtime`,`roomid`) USING BTREE,
  KEY `roomid_seq` (`roomid`,`seq`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会话消息内容';

-- 正在导出表  qychat_archive.qychat_message_content 的数据：~0 rows (大约)
/*!40000 ALTER TABLE `qychat_message_content` DISABLE KEYS */;
/*!40000 ALTER TABLE `qychat_message_content` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
