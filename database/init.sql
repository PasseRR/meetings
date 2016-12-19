CREATE DATABASE MyCalendar;

USE MyCalendar;
DROP TABLE user;
DROP TABLE menu;
DROP TABLE user_menu;
DROP TABLE room;
DROP TABLE room_schedule;

-- 用户表
CREATE TABLE user (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(8)  NOT NULL,
  loginName   VARCHAR(32) NOT NULL,
  password    VARCHAR(16) NOT NULL,
  tel         VARCHAR(14) NOT NULL,
  email       VARCHAR(32) NOT NULL,
  create_date DATETIME    NOT NULL
);
INSERT INTO user (id, name, loginName, password, tel, email, create_date)
VALUES (1, '吾同树', 'jin', '123', '15102751852', 'jins321@gmail.com', '2014-04-26 22:07:37');
INSERT INTO user (id, name, loginName, password, tel, email, create_date)
VALUES (2, '天仙姐姐', 'jie', '123', '15105201314', '5201314@qq.com', '2014-04-26 22:07:37');

-- 菜单表
CREATE TABLE menu (
  id    INT PRIMARY KEY AUTO_INCREMENT,
  name  VARCHAR(32) NOT NULL,
  price FLOAT       NOT NULL,
  state INT         NOT NULL
);
INSERT INTO `menu` (`id`, `name`, `price`, `state`) VALUES (1, '鱼香肉丝', 12, 1);
INSERT INTO `menu` (`id`, `name`, `price`, `state`) VALUES (2, '粉蒸排骨', 15, 2);
INSERT INTO `menu` (`id`, `name`, `price`, `state`) VALUES (3, '土豆回锅肉', 15, 3);
INSERT INTO `menu` (`id`, `name`, `price`, `state`) VALUES (4, '红烧肉', 20, 3);
INSERT INTO `menu` (`id`, `name`, `price`, `state`) VALUES (5, '韭菜炒鸡蛋', 12, 3);
INSERT INTO `menu` (`id`, `name`, `price`, `state`) VALUES (6, '青椒肉丝', 12, 1);
INSERT INTO `menu` (`id`, `name`, `price`, `state`) VALUES (7, '麻辣豆腐', 15, 1);
INSERT INTO `menu` (`id`, `name`, `price`, `state`) VALUES (8, '辣子鸡丁', 15, 1);
INSERT INTO `menu` (`id`, `name`, `price`, `state`) VALUES (9, '地三鲜', 10, 3);
INSERT INTO `menu` (`id`, `name`, `price`, `state`) VALUES (10, '剁椒鱼头', 30, 2);
INSERT INTO `menu` (`id`, `name`, `price`, `state`) VALUES (11, '毛豆牛肉', 18, 1);
INSERT INTO `menu` (`id`, `name`, `price`, `state`) VALUES (12, '千张肉丝', 15, 3);

-- 订单表
CREATE TABLE user_menu (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  userid      INT      NOT NULL,
  menuid      INT      NOT NULL,
  state       INT      NOT NULL,
  order_date  DATETIME NOT NULL,
  create_date DATETIME NOT NULL
);
INSERT INTO `user_menu` (`userid`, `menuid`, `state`, `order_date`, `create_date`)
VALUES (1, 1, 1, now(), '2014-04-26 22:09:49');
INSERT INTO `user_menu` (`userid`, `menuid`, `state`, `order_date`, `create_date`)
VALUES (1, 3, 1, SUBDATE(now(), INTERVAL 1 DAY), '2014-04-26 22:09:49');
INSERT INTO `user_menu` (`userid`, `menuid`, `state`, `order_date`, `create_date`)
VALUES (1, 2, 1, ADDDATE(now(), INTERVAL 1 DAY), '2014-04-29 00:09:20');
INSERT INTO `user_menu` (`userid`, `menuid`, `state`, `order_date`, `create_date`)
VALUES (1, 4, 2, now(), '2014-04-26 22:09:49');
INSERT INTO `user_menu` (`userid`, `menuid`, `state`, `order_date`, `create_date`)
VALUES (1, 5, 2, SUBDATE(now(), INTERVAL 1 DAY), '2014-04-26 22:09:49');
INSERT INTO `user_menu` (`userid`, `menuid`, `state`, `order_date`, `create_date`)
VALUES (1, 6, 2, ADDDATE(now(), INTERVAL 1 DAY), '2014-04-29 00:09:20');

-- 会议室表
CREATE TABLE room (
  id   INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(32) NOT NULL
);
INSERT INTO room (id, name) VALUES (1, '会议室一');
INSERT INTO room (id, name) VALUES (2, '会议室二');
INSERT INTO room (id, name) VALUES (3, '会议室三');
INSERT INTO room (id, name) VALUES (4, '会议室四');

CREATE TABLE room_schedule (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  subject     VARCHAR(256) NOT NULL,
  roomid      INT          NOT NULL,
  userid      INT          NOT NULL,
  start       DATETIME     NOT NULL,
  end         DATETIME     NOT NULL,
  create_date DATETIME     NOT NULL
);
INSERT INTO `room_schedule` (`subject`, `roomid`, `userid`, `start`, `end`, `create_date`) VALUES
  ('羽毛球赛程讨论会', 1, 2, ADDDATE(now(), INTERVAL "1 4" DAY_HOUR), ADDDATE(now(), INTERVAL "1 5" DAY_HOUR),
   '2014-04-29 00:09:20');
INSERT INTO `room_schedule` (`subject`, `roomid`, `userid`, `start`, `end`, `create_date`) VALUES
  ('全球环境保护会议', 1, 1, ADDDATE(now(), INTERVAL "1 2" DAY_HOUR), ADDDATE(now(), INTERVAL "1 3" DAY_HOUR),
   '2014-04-29 00:09:20');
INSERT INTO `room_schedule` (`subject`, `roomid`, `userid`, `start`, `end`, `create_date`) VALUES
  ('国庆假期出游动员大会', 1, 1, SUBDATE(now(), INTERVAL "1 2" DAY_HOUR), SUBDATE(now(), INTERVAL "1 1" DAY_HOUR),
   '2014-04-29 00:09:20');
INSERT INTO `room_schedule` (`subject`, `roomid`, `userid`, `start`, `end`, `create_date`)
VALUES ('XX项目评审', 1, 1, now(), ADDDATE(now(), INTERVAL "2" HOUR), '2014-04-29 00:09:20');