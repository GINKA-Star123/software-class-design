SET NAMES utf8mb4;

USE story_workshop;

-- 评论表
CREATE TABLE IF NOT EXISTS comment (
  comment_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论 ID',
  user_id BIGINT NOT NULL COMMENT '评论用户 ID',
  story_id BIGINT NOT NULL COMMENT '所属故事 ID',
  content VARCHAR(500) NOT NULL COMMENT '评论内容',
  parent_id BIGINT DEFAULT NULL COMMENT '父评论 ID',
  status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1 正常，0 删除',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  KEY idx_comment_user (user_id),
  KEY idx_comment_story (story_id),
  KEY idx_comment_parent (parent_id),
  CONSTRAINT fk_comment_user
    FOREIGN KEY (user_id) REFERENCES `user` (user_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_comment_story
    FOREIGN KEY (story_id) REFERENCES story (story_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_comment_parent
    FOREIGN KEY (parent_id) REFERENCES comment (comment_id)
    ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- 点赞表
CREATE TABLE IF NOT EXISTS like_record (
  like_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞 ID',
  user_id BIGINT NOT NULL COMMENT '用户 ID',
  story_id BIGINT NOT NULL COMMENT '故事 ID',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  UNIQUE KEY uk_like_user_story (user_id, story_id),
  KEY idx_like_story (story_id),
  CONSTRAINT fk_like_user
    FOREIGN KEY (user_id) REFERENCES `user` (user_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_like_story
    FOREIGN KEY (story_id) REFERENCES story (story_id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='点赞表';

-- 收藏表
CREATE TABLE IF NOT EXISTS favorite (
  fav_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收藏 ID',
  user_id BIGINT NOT NULL COMMENT '用户 ID',
  story_id BIGINT NOT NULL COMMENT '故事 ID',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  UNIQUE KEY uk_favorite_user_story (user_id, story_id),
  KEY idx_favorite_story (story_id),
  CONSTRAINT fk_favorite_user
    FOREIGN KEY (user_id) REFERENCES `user` (user_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_favorite_story
    FOREIGN KEY (story_id) REFERENCES story (story_id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- 举报表
CREATE TABLE IF NOT EXISTS report (
  report_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '举报 ID',
  user_id BIGINT NOT NULL COMMENT '举报人 ID',
  story_id BIGINT NOT NULL COMMENT '被举报故事 ID',
  comment_id BIGINT DEFAULT NULL COMMENT '被举报评论 ID',
  reason VARCHAR(200) NOT NULL COMMENT '举报原因',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0 待处理，1 已处理，2 已忽略',
  handle_user_id BIGINT DEFAULT NULL COMMENT '处理人 ID',
  handle_result VARCHAR(500) DEFAULT NULL COMMENT '处理结果',
  handle_time DATETIME DEFAULT NULL COMMENT '处理时间',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '举报时间',
  KEY idx_report_user (user_id),
  KEY idx_report_story (story_id),
  KEY idx_report_comment (comment_id),
  KEY idx_report_status (status),
  CONSTRAINT fk_report_user
    FOREIGN KEY (user_id) REFERENCES `user` (user_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_report_story
    FOREIGN KEY (story_id) REFERENCES story (story_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_report_comment
    FOREIGN KEY (comment_id) REFERENCES comment (comment_id)
    ON DELETE SET NULL,
  CONSTRAINT fk_report_handle_user
    FOREIGN KEY (handle_user_id) REFERENCES `user` (user_id)
    ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='举报表';
