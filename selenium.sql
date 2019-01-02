-- 长度说明:
-- tinyint(4):
  -- unsigned: -128 - 127
  -- unsigned:    0 - 255
-- smallint(6):
  -- unsigned: -32768 - 32767
  -- unsigned:      0 - 65535
-- mediumint(9):
  -- unsigned: -8388608 - 8388607
  -- unsigned:        0 - 16777215
-- int(11):
  -- unsigned: -2147483648 - 2147483647
  -- unsigned:           0 - 4294967295
-- bigint(20):
  -- unsigned: -9223372036854775808 - 9223372036854775807
  -- unsigned:                    0 - 18446744073709551615

-- 创建数据库
CREATE DATABASE IF NOT EXISTS selenium default character set utf8 COLLATE utf8_general_ci;

-- 创建表
DROP TABLE IF EXISTS x_java_api;

CREATE TABLE x_java_api
(
  `id`            int(11)       unsigned  NOT NULL  AUTO_INCREMENT  COMMENT '',
  `name`          varchar(255)            NOT NULL  DEFAULT ''      COMMENT '类名',
  `method`        tinyint(4)    unsigned  NOT NULL  DEFAULT 0       COMMENT '常用方法',
  `description`   varchar(255)            NOT NULL  DEFAULT ''      COMMENT '描述',
  `example_title` varchar(32)             NOT NULL  DEFAULT ''      COMMENT '例子标题',
  `project`       varchar(64)             NOT NULL  DEFAULT ''      COMMENT '例子项目名',
  `file`          varchar(64)             NOT NULL  DEFAULT ''      COMMENT '例子在项目哪个文件',
  `code`          text                    NOT NULL                  COMMENT '例子源码',
  `path`          varchar(255)            NOT NULL  DEFAULT ''      COMMENT '项目路径',
  `create_time` 	bigint(11)    unsigned 	NOT NULL 					        COMMENT '创建时间(毫秒)',
  `update_time` 	bigint(11) 		unsigned  		NULL  						    COMMENT '更新时间',
  `delete_time` 	bigint(11)    unsigned  		NULL  						    COMMENT '删除时间',
  PRIMARY KEY (id)
) engine=InnoDB DEFAULT charset=utf8 comment='example结果记录表';

