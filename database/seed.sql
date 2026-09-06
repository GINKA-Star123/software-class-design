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
  '$2b$10$H9LBio7tfhC84garSVMsdewNCod6umFtKw.Bx0biYoeGXLhBe.MJW',
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
  node_id, story_id, node_text, is_start, is_ending, ending_title, sort_order
)
VALUES
  (101, 1, '子时三刻，城南更楼的铜壶滴漏忽然停摆。你提起灯笼推开木门，雨幕里的长街空无一人——本该巡夜的更夫，今夜一个都没有回来。', 1, 0, NULL, 1),
  (102, 1, '你沿朱雀街向南巡行，雨水冲刷青石板。远处的更声刚响半下便断了，像被什么硬生生掐住。', 0, 0, NULL, 2),
  (103, 1, '城南老酒肆还亮着灯。老板娘说打更的老周今夜没来赊酒，倒有个撑伞的书生，在角落坐了一整夜。', 0, 0, NULL, 3),
  (104, 1, '你躲进城隍庙避雨，香火早已冷透。供桌下压着一枚铜符，符上刻着“更漏司”三个小字。', 0, 0, NULL, 4),
  (105, 1, '巷口一道黑影翻过坊墙，直奔城东而去。他脚步极快，像是怕被谁看见。', 0, 0, NULL, 5),
  (106, 1, '回到更楼翻查更簿，今夜记录被人撕去一页，纸边还沾着未干的墨。', 0, 0, NULL, 6),
  (107, 1, '书生自称姓沈，是修史的书办。他压低声音：更漏停摆那夜，有人抬着空棺进了府衙后门。', 0, 0, NULL, 7),
  (108, 1, '赌坊伙计认出你是更夫，用暗语提醒：铜壶滴漏的生意，今晚别碰。他眼神闪躲，显然知道些什么。', 0, 0, NULL, 8),
  (109, 1, '庙祝看到铜符脸色大变：这是更漏司的调令符，三年前已随旧案封存，怎会在供桌下？', 0, 0, NULL, 9),
  (110, 1, '庙里老僧是还俗的旧吏。他说更漏司当年负责宫城报时，一夜之间满司裁撤，卷宗全部焚毁。', 0, 0, NULL, 10),
  (111, 1, '义庄里你找到老周，他已经死了，掌心攥着一张字条：符在庙，账在仓，人进宫。', 0, 0, NULL, 11),
  (112, 1, '潜入城东官仓，你在粮垛下找到一本账册：每月初一、十五，有“空车”进、“满车”出，走的是宫城角门。', 0, 0, NULL, 12),
  (113, 1, '黑衣人果然在等你。搏斗中你扯下他的面巾——竟是府衙仵作。他冷笑：查到这里，你已经回不了头。', 0, 0, NULL, 13),
  (114, 1, '沈书生约你三更在曲江画舫相见，说有一封前朝密信，能证明更漏司当年蒙冤。', 0, 0, NULL, 14),
  (115, 1, '按老周字条回到城隍庙，你在香案暗格里找到半枚虎符和一份名单，名单上的名字早已“病故”。', 0, 0, NULL, 15),
  (116, 1, '义庄空棺夹层里藏着更漏司最后一任司正的遗书：宫中有“借尸还魂”之术，专用来除掉知情人。', 0, 0, NULL, 16),
  (117, 1, '顺着官仓后的密道，你摸到宫城角门内侧，看见有人把一口棺木抬进冷宫。', 0, 0, NULL, 17),
  (118, 1, '长街的雨越下越大。次日清晨，人们在城隍庙前发现你的灯笼——而你与这个雨夜一起，再无声息。', 0, 1, '雨落无声', 18),
  (119, 1, '你带名单夜闯府衙，却见府尹正与宫中内侍对坐。府尹叹气：你查到的，不止是更漏司。', 0, 0, NULL, 19),
  (120, 1, '画舫上沈书生把密信交给你：真相一旦见光，朝堂便再无宁日。你还要查下去吗？', 0, 0, NULL, 20),
  (121, 1, '虎符与密信相互印证：所谓借尸还魂，实为借更漏司轮值之便，将废太子余党偷运出宫。', 0, 0, NULL, 21),
  (122, 1, '你在金吾卫面前呈上虎符与密信。次日宫城连下三道旨意：更漏司沉冤昭雪，主谋下狱。雨停那夜，更声比以往任何一夜都响。', 0, 1, '真相大白', 22),
  (123, 1, '你决定孤身入宫，把证据递到御前。宫墙森森，这一去再无回头路。', 0, 0, NULL, 23),
  (124, 1, '你把证据留在府衙门口，牵一匹瘦马出春明门。多年后江南茶楼讲起长安旧案，说书人姓沈，台下总坐着个戴斗笠的更夫。', 0, 1, '远走江湖', 24),
  (125, 1, '城门口，你截住准备出逃的主谋。他缓缓拔刀：一个更夫，何必拿命赌真相。', 0, 0, NULL, 25),
  (126, 1, '你让开一步，看着他消失在雨里，却把证据交给潜伏多时的金吾卫。三日后诏书下达，你依旧做你的更夫——从此每夜更声，都比从前更亮。', 0, 1, '长夜将明', 26),
  (127, 1, '更楼门后躺着一块不属于本坊的铜牌：永宁坊更夫赵四。可他三天前就已经“病故”。', 0, 0, NULL, 27),
  (128, 1, '巡街时，府衙司库别院里有人连夜烧纸，火光里隐约是更簿的残页。', 0, 0, NULL, 28),
  (129, 1, '你记下黑衣人落脚处——义庄偏房。那口本应停放老周尸身的棺，此刻空空如也。', 0, 0, NULL, 29),
  (130, 1, '冷宫老妪自称是更漏司司正之妻，被幽禁十年。她说：虎符分两半，另一半在沈书生手里。', 0, 0, NULL, 30)
ON DUPLICATE KEY UPDATE
  node_text = VALUES(node_text),
  is_start = VALUES(is_start),
  is_ending = VALUES(is_ending),
  ending_title = VALUES(ending_title),
  sort_order = VALUES(sort_order);

INSERT INTO story_choice (
  choice_id, from_node_id, to_node_id, choice_text, condition_expr, sort_order
)
VALUES
  (2001, 101, 102, '沿朱雀街向南巡查', NULL, 1),
  (2002, 101, 103, '进老酒肆打听', NULL, 2),
  (2003, 101, 104, '进城隍庙避雨', NULL, 3),
  (2004, 101, 127, '去永宁坊查铜牌', NULL, 4),
  (2005, 101, 115, '循着模糊的记忆直奔城隍庙暗格', 'ach:FIRST_PLAY', 5),
  (2006, 102, 105, '尾随黑影', NULL, 6),
  (2007, 102, 106, '回更楼翻更簿', NULL, 7),
  (2008, 102, 128, '留意司库别院火光', NULL, 8),
  (2009, 103, 107, '与沈书生攀谈', NULL, 9),
  (2010, 103, 108, '随伙计去赌坊', NULL, 10),
  (2011, 104, 109, '拿铜符问庙祝', NULL, 11),
  (2012, 104, 110, '听老僧夜话', NULL, 12),
  (2013, 105, 113, '冲上去搏斗', NULL, 13),
  (2014, 105, 129, '暗中跟随记下落脚点', NULL, 14),
  (2015, 106, 111, '去义庄找老周', NULL, 15),
  (2016, 106, 127, '核对赵四铜牌', NULL, 16),
  (2017, 107, 114, '赴画舫之约', NULL, 17),
  (2018, 107, 113, '试探沈书生身份', NULL, 18),
  (2019, 108, 112, '夜探官仓', NULL, 19),
  (2020, 108, 120, '找沈书生买消息', NULL, 20),
  (2021, 109, 115, '循铜符找暗格', NULL, 21),
  (2022, 109, 110, '再问老僧', NULL, 22),
  (2023, 110, 115, '依老僧指点找暗格', NULL, 23),
  (2024, 110, 116, '查义庄空棺', NULL, 24),
  (2025, 111, 112, '按字条夜探官仓', NULL, 25),
  (2026, 111, 116, '查义庄空棺', NULL, 26),
  (2027, 112, 117, '沿密道入宫', NULL, 27),
  (2028, 112, 119, '夜闯府衙', NULL, 28),
  (2029, 113, 119, '直奔府衙报案', NULL, 29),
  (2030, 113, 123, '孤身入宫面圣', NULL, 30),
  (2031, 114, 120, '收下密信', NULL, 31),
  (2032, 114, 116, '先查义庄空棺', NULL, 32),
  (2033, 115, 121, '合并虎符与名单', NULL, 33),
  (2034, 115, 119, '直接夜闯府衙', NULL, 34),
  (2035, 116, 121, '整理遗书线索', NULL, 35),
  (2036, 116, 120, '取密信对证', NULL, 36),
  (2037, 117, 121, '撤离并整理证据', NULL, 37),
  (2038, 117, 123, '孤身入宫', NULL, 38),
  (2039, 117, 130, '与冷宫老妪攀谈', NULL, 39),
  (2040, 119, 121, '呈上全部证据', NULL, 40),
  (2041, 119, 125, '赶往城门拦截', NULL, 41),
  (2042, 119, 124, '留下证据远走', NULL, 42),
  (2043, 120, 121, '携密信对证', NULL, 43),
  (2044, 120, 125, '只身去城门堵人', NULL, 44),
  (2045, 120, 124, '焚信远走江湖', NULL, 45),
  (2046, 121, 122, '呈报金吾卫', NULL, 46),
  (2047, 121, 125, '只身截主谋', NULL, 47),
  (2048, 123, 118, '冒险求见，被构陷伏杀', NULL, 48),
  (2049, 125, 126, '让开一步，暗中交给金吾卫', NULL, 49),
  (2050, 127, 116, '循赵四尸身查义庄', NULL, 50),
  (2051, 127, 111, '找老周核实', NULL, 51),
  (2052, 128, 112, '潜入别院查证', NULL, 52),
  (2053, 128, 111, '回更楼找老周商量', NULL, 53),
  (2054, 129, 116, '潜入义庄偏房', NULL, 54),
  (2055, 129, 113, '折返与黑衣人当面对质', NULL, 55),
  (2056, 130, 121, '带老妪证词离开冷宫', NULL, 56)
ON DUPLICATE KEY UPDATE
  from_node_id = VALUES(from_node_id),
  to_node_id = VALUES(to_node_id),
  choice_text = VALUES(choice_text),
  condition_expr = VALUES(condition_expr),
  sort_order = VALUES(sort_order);

INSERT INTO achievement (
  ach_id, ach_code, ach_name, description, ach_type, story_id, status
)
VALUES
  (1, 'FIRST_PLAY', '初入工坊', '首次开始游玩任意故事', 0, NULL, 1),
  (2, 'FIRST_ENDING', '抵达结局', '首次抵达任意故事结局', 0, NULL, 1),
  (3, 'FIRST_COMMENT', '留下足迹', '首次发表评论', 0, NULL, 1),
  (4, 'FIRST_STORY', '故事作者', '首次创建自己的故事', 0, NULL, 1),
  (5, 'CHANGAN_T1', '真相大白', '在《长安夜雨》中达成结局：真相大白', 1, 1, 1),
  (6, 'CHANGAN_T2', '远走江湖', '在《长安夜雨》中达成结局：远走江湖', 1, 1, 1),
  (7, 'CHANGAN_T3', '长夜将明', '在《长安夜雨》中达成结局：长夜将明', 1, 1, 1),
  (8, 'CHANGAN_T4', '雨落无声', '在《长安夜雨》中达成结局：雨落无声', 1, 1, 1)
ON DUPLICATE KEY UPDATE
  ach_code = VALUES(ach_code),
  ach_name = VALUES(ach_name),
  description = VALUES(description),
  ach_type = VALUES(ach_type),
  story_id = VALUES(story_id),
  status = VALUES(status);
