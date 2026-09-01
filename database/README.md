# database

数据库目录用于存放决策树游戏工坊平台的 MySQL 脚本。根据详细计划说明书，系统数据库采用 **MySQL 8.0 + InnoDB + utf8mb4**，主要围绕用户、角色、故事、节点、选项、进度、互动、举报和成就等实体建表。

## 1. 整体结构

```text
database/
├─ README.md
├─ init.sql
├─ schema.sql
├─ seed.sql
└─ migrations/
   ├─ 001_create_user_tables.sql
   ├─ 002_create_story_tables.sql
   ├─ 003_create_play_tables.sql
   ├─ 004_create_interact_tables.sql
   ├─ 005_create_achievement_tables.sql
   └─ 006_insert_initial_data.sql
```

如果课程设计阶段不想拆得太细，也可以只保留：

```text
database/
├─ schema.sql       # 全部建表语句
├─ seed.sql         # 全部初始数据
└─ init.sql         # 一键初始化入口
```

## 2. 对应文件名称

### SQL 脚本文件

| 文件名称 | 解释 |
| --- | --- |
| `schema.sql` | 建表脚本，包含所有业务表、主键、外键、索引、唯一约束。 |
| `seed.sql` | 初始化数据脚本，包含默认角色、默认管理员、官方故事、默认成就等。 |
| `init.sql` | 初始化入口脚本，可以按顺序执行 `schema.sql` 和 `seed.sql`。 |
| `migrations/001_create_user_tables.sql` | 创建用户与权限相关表：`user`、`role`、`user_role`。 |
| `migrations/002_create_story_tables.sql` | 创建故事创作相关表：`story`、`story_node`、`story_choice`。 |
| `migrations/003_create_play_tables.sql` | 创建游玩进度相关表：`game_progress`。 |
| `migrations/004_create_interact_tables.sql` | 创建互动相关表：`comment`、`like_record`、`favorite`、`report`。 |
| `migrations/005_create_achievement_tables.sql` | 创建成就相关表：`achievement`、`user_achievement`。 |
| `migrations/006_insert_initial_data.sql` | 插入基础初始化数据。 |

### 数据表文件对应关系

| 表名 | 建议放置脚本 | 解释 |
| --- | --- | --- |
| `user` | `001_create_user_tables.sql` | 用户表，保存账号、密码、昵称、邮箱、头像、状态、注册时间。 |
| `role` | `001_create_user_tables.sql` | 角色表，保存玩家、作者、审核员、管理员等角色。 |
| `user_role` | `001_create_user_tables.sql` | 用户角色关系表，支持一个用户拥有多个角色。 |
| `story` | `002_create_story_tables.sql` | 故事表，保存标题、简介、封面、作者、状态、播放数、点赞数等。 |
| `story_node` | `002_create_story_tables.sql` | 故事节点表，保存剧情文本、是否起始节点、是否结局节点。 |
| `story_choice` | `002_create_story_tables.sql` | 节点选项表，保存选项文本、源节点、目标节点、条件表达式。 |
| `game_progress` | `003_create_play_tables.sql` | 游戏进度表，保存当前节点、历史路径、完成状态、更新时间。 |
| `comment` | `004_create_interact_tables.sql` | 评论表，保存用户对故事的评论和回复。 |
| `like_record` | `004_create_interact_tables.sql` | 点赞记录表，限制同一用户对同一故事只能点赞一次。 |
| `favorite` | `004_create_interact_tables.sql` | 收藏记录表，限制同一用户对同一故事只能收藏一次。 |
| `report` | `004_create_interact_tables.sql` | 举报表，保存被举报故事或评论、原因、处理状态、处理人。 |
| `achievement` | `005_create_achievement_tables.sql` | 成就定义表，区分平台级成就和故事级成就。 |
| `user_achievement` | `005_create_achievement_tables.sql` | 用户成就表，记录用户已经获得的成就。 |

## 3. 对应代码文件的解释

数据库脚本与后端代码的对应关系如下：

| 数据库对象 | 后端 Entity | 后端 Mapper | 主要业务说明 |
| --- | --- | --- | --- |
| `user` | `User.java` | `UserMapper.java` | 支持注册、登录、个人信息管理、账号状态控制。 |
| `role` | `Role.java` | `RoleMapper.java` | 支持角色权限控制。 |
| `user_role` | `UserRole.java` | `UserRoleMapper.java` | 支持用户与角色的多对多关系。 |
| `story` | `Story.java` | `StoryMapper.java` | 支持故事浏览、搜索、创作、审核、发布、统计。 |
| `story_node` | `StoryNode.java` | `StoryNodeMapper.java` | 支持决策树节点编辑和游玩节点加载。 |
| `story_choice` | `StoryChoice.java` | `StoryChoiceMapper.java` | 支持玩家选择分支和作者编辑选项。 |
| `game_progress` | `GameProgress.java` | `ProgressMapper.java` | 支持自动保存、继续游玩、重置进度。 |
| `comment` | `Comment.java` | `CommentMapper.java` | 支持故事评论、回复、敏感词过滤后的展示。 |
| `like_record` | `LikeRecord.java` | `LikeMapper.java` | 支持点赞和取消点赞。 |
| `favorite` | `Favorite.java` | `FavoriteMapper.java` | 支持收藏和取消收藏。 |
| `report` | `Report.java` | `ReportMapper.java` | 支持故事或评论举报，以及审核员处理举报。 |
| `achievement` | `Achievement.java` | `AchievementMapper.java` | 支持成就定义和查询。 |
| `user_achievement` | `UserAchievement.java` | `UserAchievementMapper.java` | 支持用户达成成就后的记录。 |

核心约束建议：

| 表名 | 约束建议 | 说明 |
| --- | --- | --- |
| `user` | `UNIQUE(username)`、`UNIQUE(email)` | 用户名和邮箱不能重复。 |
| `user_role` | `PRIMARY KEY(user_id, role_id)` | 防止重复分配同一角色。 |
| `story_choice` | 外键关联 `from_node_id` 和 `to_node_id` | 保证选项必须从一个节点指向另一个节点。 |
| `like_record` | `UNIQUE(user_id, story_id)` | 同一用户对同一故事只能保留一条点赞记录。 |
| `favorite` | `UNIQUE(user_id, story_id)` | 同一用户对同一故事只能保留一条收藏记录。 |
| `user_achievement` | `UNIQUE(user_id, ach_id)` | 同一成就只发放一次。 |

本数据库目录重点对应说明书中的 E-R 设计和物理表结构设计。
