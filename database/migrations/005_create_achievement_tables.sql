SET NAMES utf8mb4;

USE story_workshop;

-- 成就定义表
CREATE TABLE IF NOT EXISTS achievement (
  ach_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成就 ID',
  ach_code VARCHAR(50) NOT NULL COMMENT '成就编码',
  ach_name VARCHAR(100) NOT NULL COMMENT '成就名称',
  description VARCHAR(255) DEFAULT NULL COMMENT '成就描述',
  ach_type TINYINT NOT NULL DEFAULT 0 COMMENT '成就类型：0 平台级，1 故事级',
  story_id BIGINT DEFAULT NULL COMMENT '关联故事 ID',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1 启用，0 禁用',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY uk_achievement_code (ach_code),
  KEY idx_achievement_story (story_id),
  CONSTRAINT fk_achievement_story
    FOREIGN KEY (story_id) REFERENCES story (story_id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成就表';

-- 用户成就表
CREATE TABLE IF NOT EXISTS user_achievement (
  ua_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户成就记录 ID',
  user_id BIGINT NOT NULL COMMENT '用户 ID',
  ach_id BIGINT NOT NULL COMMENT '成就 ID',
  achieve_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '达成时间',
  UNIQUE KEY uk_user_achievement (user_id, ach_id),
  KEY idx_user_achievement_user (user_id),
  KEY idx_user_achievement_ach (ach_id),
  CONSTRAINT fk_user_achievement_user
    FOREIGN KEY (user_id) REFERENCES `user` (user_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_user_achievement_ach
    FOREIGN KEY (ach_id) REFERENCES achievement (ach_id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户成就表';
