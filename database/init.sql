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
VALUES (1, '管理员', 'admin', 'admin', '13512345678', 'admin@tellyes.com', now());

-- 会议室表
CREATE TABLE room (
  id   INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(32) NOT NULL
);
INSERT INTO room (id, name) VALUES (1, '会议室');

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