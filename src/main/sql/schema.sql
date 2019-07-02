-- 数据库初始化脚本
-- 创建数据库
DROP DATABASE IF EXISTS seckill;
CREATE DATABASE seckill;

-- 创建秒杀库存表
DROP TABLE IF EXISTS seckill.table_seckill;
CREATE TABLE seckill.table_seckill
(
    `seckill_id`  BIGINT       NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
    `name`        VARCHAR(120) NOT NULL COMMENT '商品名称',
    `number`      INT          NOT NULL COMMENT '库存数量',
    `start_time`  TIMESTAMP    NOT NULL COMMENT '秒杀开始时间',
    `end_time`    TIMESTAMP    NOT NULL COMMENT '秒杀结束时间',
    `create_time` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (seckill_id),
    KEY idx_start_time (start_time),
    KEY idx_end_time (end_time),
    KEY idx_create_time (create_time)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8 COMMENT '秒杀库存表';

-- 初始化数据
INSERT INTO seckill.table_seckill(name, number, start_time, end_time)
    VALUE
    ('2000元秒杀iPhone X', 10, '2018-03-10 00:00:00', '2018-03-11 00:00:00'),
    ('1500元秒杀iPhone 8', 30, '2018-03-15 00:00:00', '2018-03-16 00:00:00'),
    ('1000元秒杀小米 Mix2s', 100, '2018-03-18 00:00:00', '2018-03-19 00:00:00'),
    ('500元秒杀魅族 15', 300, '2018-03-22 00:00:00', '2018-03-23 00:00:00'),
    ('200元秒杀坚果 3', 600, '2018-03-10 00:00:00', '2018-03-25 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关信息
DROP TABLE IF EXISTS seckill.success_killed;
CREATE TABLE seckill.success_killed
(
    `seckill_id`  BIGINT    NOT NULL COMMENT '秒杀商品id',
    `user_phone`  BIGINT    NOT NULL COMMENT '用户手机号',
    `state`       TINYINT   NOT NULL DEFAULT -1 COMMENT '状态标示 -1：无效， 0：成功， 1：已付款',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (seckill_id, user_phone),
    KEY idx_create_time (create_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8 COMMENT '秒杀成功明细表';

-- 更新秒杀库存表的id=1000的开启时间和结束时间
UPDATE seckill.table_seckill
SET start_time = '2019-6-20 00:00:00',
    end_time   = '2019-6-21 00:00:00'
WHERE seckill_id = 1000;
UPDATE seckill.table_seckill
SET start_time = '2019-6-20 17:35:00',
    end_time   = '2019-6-20 00:00:00'
WHERE seckill_id = 1001;