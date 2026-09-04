SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS story_workshop
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE story_workshop;

DROP TABLE IF EXISTS user_achievement;
DROP TABLE IF EXISTS achievement;
DROP TABLE IF EXISTS report;
DROP TABLE IF EXISTS favorite;
DROP TABLE IF EXISTS like_record;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS game_progress;
DROP TABLE IF EXISTS story_choice;
DROP TABLE IF EXISTS story_node;
DROP TABLE IF EXISTS story;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS `user`;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `user` (
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

CREATE TABLE role (
  role_id SMALLINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色 ID',
  role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
  description VARCHAR(200) DEFAULT NULL COMMENT '角色说明',
  UNIQUE KEY uk_role_name (role_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

CREATE TABLE user_role (
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

CREATE TABLE story (
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

CREATE TABLE story_node (
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

CREATE TABLE story_choice (
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

CREATE TABLE game_progress (
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

CREATE TABLE comment (
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

CREATE TABLE like_record (
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

CREATE TABLE favorite (
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

CREATE TABLE report (
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

CREATE TABLE achievement (
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

CREATE TABLE user_achievement (
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