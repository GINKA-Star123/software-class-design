SET NAMES utf8mb4;

USE story_workshop;

-- 游戏进度表
CREATE TABLE IF NOT EXISTS game_progress (
  progress_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '进度 ID',
  user_id BIGINT NOT NULL COMMENT '玩家 ID',
  story_id BIGINT NOT NULL COMMENT '故事 ID',
  current_node_id BIGINT DEFAULT NULL COMMENT '当前节点 ID',
  slot_no TINYINT NOT NULL DEFAULT 1 COMMENT '进度槽位，限制同一用户同一故事最多 3 条进度',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0 进行中，1 已完成',
  history TEXT DEFAULT NULL COMMENT '历史路径，JSON 数组字符串',
  ending_count INT NOT NULL DEFAULT 0 COMMENT '已达成结局数',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  UNIQUE KEY uk_progress_slot (user_id, story_id, slot_no),
  KEY idx_progress_user (user_id),
  KEY idx_progress_story (story_id),
  KEY idx_progress_node (current_node_id),
  CONSTRAINT fk_progress_user
    FOREIGN KEY (user_id) REFERENCES `user` (user_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_progress_story
    FOREIGN KEY (story_id) REFERENCES story (story_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_progress_node
    FOREIGN KEY (current_node_id) REFERENCES story_node (node_id)
    ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='游戏进度表';
