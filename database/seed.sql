SET NAMES utf8mb4;

USE story_workshop;

INSERT INTO role (role_id, role_name, description)
VALUES
  (1, 'PLAYER', '玩家：可以浏览和游玩故事，进行点赞、收藏、评论、举报等互动'),
  (2, 'AUTHOR', '作者：可以创建、编辑并提交审核自己的决策树故事'),
  (3, 'AUDITOR', '审核员：可以审核故事、处理举报、执行上下架操作'),
  (4, 'ADMIN', '系统管理员：可以管理用户、角色、内容和平台统计')
ON DUPLICATE KEY UPDATE
  role_name = VALUES(role_name),
  description = VALUES(description);

INSERT INTO `user` (
  user_id,
  username,
  password,
  nickname,
  email,
  avatar_url,
  status
)
VALUES (
  1,
  'official',
  '$2a$10$replace_this_with_real_bcrypt_hash_for_disabled_official_user',
  '官方故事账号',
  'official@example.com',
  NULL,
  1
)
ON DUPLICATE KEY UPDATE
  nickname = VALUES(nickname),
  email = VALUES(email),
  status = VALUES(status);

INSERT INTO user_role (user_id, role_id)
VALUES
  (1, 1),
  (1, 2),
  (1, 3),
  (1, 4)
ON DUPLICATE KEY UPDATE
  user_id = VALUES(user_id),
  role_id = VALUES(role_id);

INSERT INTO story (
  story_id,
  author_id,
  title,
  intro,
  category,
  cover_url,
  status,
  play_count,
  like_count,
  favorite_count,
  comment_count,
  publish_time
)
VALUES (
  1,
  1,
  '长安夜雨',
  '一场夜雨笼罩长安，玩家将在疑云与线索之间做出选择，抵达不同结局。',
  '古风悬疑',
  NULL,
  2,
  0,
  0,
  0,
  0,
  NOW()
)
ON DUPLICATE KEY UPDATE
  title = VALUES(title),
  intro = VALUES(intro),
  category = VALUES(category),
  status = VALUES(status),
  publish_time = VALUES(publish_time);

INSERT INTO story_node (
  node_id,
  story_id,
  node_text,
  is_start,
  is_ending,
  ending_title,
  sort_order
)
VALUES
  (
    1,
    1,
    '长安城入夜后忽然落雨。你站在朱雀大街尽头，手中握着一封被雨水打湿的密信。远处更鼓响起，巷口似乎有人影闪过。',
    1,
    0,
    NULL,
    1
  ),
  (
    2,
    1,
    '你追入巷中，发现地上留着一枚青铜鱼符。墙边的脚印通向一座废弃宅院。',
    0,
    0,
    NULL,
    2
  ),
  (
    3,
    1,
    '你先打开密信，纸上只有一句话：不要相信今夜第一个向你求助的人。',
    0,
    0,
    NULL,
    3
  ),
  (
    4,
    1,
    '你进入废宅，门忽然在身后合上。黑暗中传来熟悉的声音，你意识到自己已落入陷阱。',
    0,
    1,
    '雨夜迷局',
    4
  ),
  (
    5,
    1,
    '你带着密信返回灯火处，避开了巷中的埋伏。天明时，你将线索交到大理寺，揭开了夜雨背后的阴谋。',
    0,
    1,
    '破晓之前',
    5
  )
ON DUPLICATE KEY UPDATE
  node_text = VALUES(node_text),
  is_start = VALUES(is_start),
  is_ending = VALUES(is_ending),
  ending_title = VALUES(ending_title),
  sort_order = VALUES(sort_order);

INSERT INTO story_choice (
  choice_id,
  from_node_id,
  to_node_id,
  choice_text,
  condition_expr,
  sort_order
)
VALUES
  (1, 1, 2, '追向巷口的人影', NULL, 1),
  (2, 1, 3, '先查看手中的密信', NULL, 2),
  (3, 2, 4, '立刻进入废弃宅院', NULL, 1),
  (4, 2, 5, '带着鱼符返回明亮处', NULL, 2),
  (5, 3, 5, '暂时不追人影，返回大街', NULL, 1),
  (6, 3, 2, '仍然追入巷中查看', NULL, 2)
ON DUPLICATE KEY UPDATE
  from_node_id = VALUES(from_node_id),
  to_node_id = VALUES(to_node_id),
  choice_text = VALUES(choice_text),
  condition_expr = VALUES(condition_expr),
  sort_order = VALUES(sort_order);

INSERT INTO achievement (
  ach_id,
  ach_code,
  ach_name,
  description,
  ach_type,
  story_id,
  status
)
VALUES
  (1, 'FIRST_PLAY', '初入工坊', '首次开始游玩任意故事', 0, NULL, 1),
  (2, 'FIRST_ENDING', '抵达结局', '首次抵达任意故事结局', 0, NULL, 1),
  (3, 'FIRST_COMMENT', '留下足迹', '首次发表评论', 0, NULL, 1),
  (4, 'FIRST_STORY', '故事作者', '首次创建自己的故事', 0, NULL, 1),
  (5, 'CHANGAN_ENDING_1', '雨夜迷局', '在《长安夜雨》中达成结局：雨夜迷局', 1, 1, 1),
  (6, 'CHANGAN_ENDING_2', '破晓之前', '在《长安夜雨》中达成结局：破晓之前', 1, 1, 1)
ON DUPLICATE KEY UPDATE
  ach_name = VALUES(ach_name),
  description = VALUES(description),
  ach_type = VALUES(ach_type),
  story_id = VALUES(story_id),
  status = VALUES(status);