DROP DATABASE IF EXISTS meetings;

CREATE DATABASE meetings DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

USE meetings;

-- 用户表
DROP TABLE IF EXISTS user;
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
VALUES (1, '管理员', 'admin', '123456', '13512345678', 'admin@tellyes.com', now());

-- 会议室表
DROP TABLE IF EXISTS room;
CREATE TABLE room (
  id   INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(32) NOT NULL
);
INSERT INTO room (id, name) VALUES (1, '会议室');

-- 会议表
DROP TABLE IF EXISTS room_schedule;
CREATE TABLE room_schedule (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  subject     VARCHAR(256) NOT NULL,
  roomid      INT          NOT NULL,
  userid      INT          NOT NULL,
  start       DATETIME     NOT NULL,
  end         DATETIME     NOT NULL,
  create_date DATETIME     NOT NULL
);