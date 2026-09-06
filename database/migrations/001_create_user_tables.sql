SET NAMES utf8mb4;

USE story_workshop;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  user_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户 ID',
  username VARCHAR(50) NOT NULL COMMENT '用户名',
  password VARCHAR(100) NOT NULL COMMENT 'BCrypt 加密密码',
  nickname VARCHAR(50) NOT NULL COMMENT '昵称',
  email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  avatar_url VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1 正常，0 禁用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY uk_user_username (username),
  UNIQUE KEY uk_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS role (
  role_id SMALLINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色 ID',
  role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
  description VARCHAR(200) DEFAULT NULL COMMENT '角色说明',
  UNIQUE KEY uk_role_name (role_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 用户角色关系表
CREATE TABLE IF NOT EXISTS user_role (
  user_id BIGINT NOT NULL COMMENT '用户 ID',
  role_id SMALLINT NOT NULL COMMENT '角色 ID',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_user_role_user
    FOREIGN KEY (user_id) REFERENCES `user` (user_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_user_role_role
    FOREIGN KEY (role_id) REFERENCES role (role_id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关系表';
