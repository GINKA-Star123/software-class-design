SET NAMES utf8mb4;

USE story_workshop;

-- 故事表
CREATE TABLE IF NOT EXISTS story (
  story_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '故事 ID',
  author_id BIGINT NOT NULL COMMENT '作者 ID',
  title VARCHAR(100) NOT NULL COMMENT '故事标题',
  intro VARCHAR(500) DEFAULT NULL COMMENT '故事简介',
  category VARCHAR(50) DEFAULT NULL COMMENT '故事分类',
  cover_url VARCHAR(255) DEFAULT NULL COMMENT '封面图地址',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0 草稿，1 待审核，2 已发布，3 已驳回，4 已下架',
  reject_reason VARCHAR(500) DEFAULT NULL COMMENT '驳回原因',
  audit_user_id BIGINT DEFAULT NULL COMMENT '审核人 ID',
  audit_time DATETIME DEFAULT NULL COMMENT '审核时间',
  publish_time DATETIME DEFAULT NULL COMMENT '发布时间',
  play_count INT NOT NULL DEFAULT 0 COMMENT '游玩次数',
  like_count INT NOT NULL DEFAULT 0 COMMENT '点赞数',
  favorite_count INT NOT NULL DEFAULT 0 COMMENT '收藏数',
  comment_count INT NOT NULL DEFAULT 0 COMMENT '评论数',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY idx_story_author (author_id),
  KEY idx_story_status (status),
  KEY idx_story_category (category),
  KEY idx_story_publish_time (publish_time),
  CONSTRAINT fk_story_author
    FOREIGN KEY (author_id) REFERENCES `user` (user_id),
  CONSTRAINT fk_story_audit_user
    FOREIGN KEY (audit_user_id) REFERENCES `user` (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='故事表';

-- 故事节点表
CREATE TABLE IF NOT EXISTS story_node (
  node_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '节点 ID',
  story_id BIGINT NOT NULL COMMENT '所属故事 ID',
  node_text TEXT NOT NULL COMMENT '节点剧情文本',
  is_start TINYINT NOT NULL DEFAULT 0 COMMENT '是否起始节点：1 是，0 否',
  is_ending TINYINT NOT NULL DEFAULT 0 COMMENT '是否结局节点：1 是，0 否',
  ending_title VARCHAR(100) DEFAULT NULL COMMENT '结局标题',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY idx_story_node_story (story_id),
  KEY idx_story_node_start (story_id, is_start),
  KEY idx_story_node_ending (story_id, is_ending),
  CONSTRAINT fk_story_node_story
    FOREIGN KEY (story_id) REFERENCES story (story_id)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='故事节点表';

-- 故事选项表
CREATE TABLE IF NOT EXISTS story_choice (
  choice_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '选项 ID',
  from_node_id BIGINT NOT NULL COMMENT '源节点 ID',
  to_node_id BIGINT NOT NULL COMMENT '目标节点 ID',
  choice_text VARCHAR(200) NOT NULL COMMENT '选项文本',
  condition_expr VARCHAR(255) DEFAULT NULL COMMENT '前置条件表达式',
  sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  KEY idx_choice_from_node (from_node_id),
  KEY idx_choice_to_node (to_node_id),
  CONSTRAINT fk_choice_from_node
    FOREIGN KEY (from_node_id) REFERENCES story_node (node_id)
    ON DELETE CASCADE,
  CONSTRAINT fk_choice_to_node
    FOREIGN KEY (to_node_id) REFERENCES story_node (node_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='故事选项表';

-- 故事删除申请表（作者申请删除 -> 审核处理）
CREATE TABLE IF NOT EXISTS story_delete_request (
  req_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '删除申请ID',
  story_id BIGINT NOT NULL COMMENT '故事ID',
  requester_id BIGINT NOT NULL COMMENT '申请人(作者)ID',
  reason VARCHAR(500) DEFAULT NULL COMMENT '删除理由',
  status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0待处理 1已同意 2已拒绝',
  handle_user_id BIGINT DEFAULT NULL COMMENT '处理人ID',
  handle_result VARCHAR(500) DEFAULT NULL COMMENT '处理结果备注',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  handle_time DATETIME DEFAULT NULL COMMENT '处理时间',
  KEY idx_delreq_story (story_id),
  KEY idx_delreq_status (status),
  CONSTRAINT fk_delreq_story FOREIGN KEY (story_id) REFERENCES story (story_id) ON DELETE CASCADE,
  CONSTRAINT fk_delreq_user FOREIGN KEY (requester_id) REFERENCES `user` (user_id) ON DELETE CASCADE,
  CONSTRAINT fk_delreq_handle FOREIGN KEY (handle_user_id) REFERENCES `user` (user_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='故事删除申请表';
