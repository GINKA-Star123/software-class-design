# backend

后端目录用于存放决策树游戏工坊平台的 JavaEE / Spring Boot 服务端代码。根据详细计划说明书，本目录采用 **MVC 分层 + 业务模块分包** 的方式组织，主要负责用户认证、故事管理、决策树游玩、进度保存、审核发布、互动统计和后台管理等功能。

## 1. 整体结构

```text
backend/
├─ pom.xml
├─ README.md
└─ src/
   ├─ main/
   │  ├─ java/
   │  │  └─ com/example/storyworkshop/
   │  │     ├─ StoryWorkshopApplication.java
   │  │     ├─ config/
   │  │     ├─ common/
   │  │     ├─ module/
   │  │     │  ├─ user/
   │  │     │  ├─ story/
   │  │     │  ├─ play/
   │  │     │  ├─ audit/
   │  │     │  ├─ interact/
   │  │     │  ├─ achievement/
   │  │     │  ├─ stat/
   │  │     │  └─ admin/
   │  │     └─ infra/
   │  └─ resources/
   │     ├─ application.yml
   │     ├─ application-dev.yml
   │     └─ mapper/
   └─ test/
      └─ java/com/example/storyworkshop/
```

各业务模块内部建议统一使用以下子结构：

```text
module-name/
├─ controller/       # 控制层，接收前端请求
├─ service/          # 业务层，处理业务规则
├─ mapper/           # 数据访问层，对应 MyBatis Mapper
├─ entity/           # 数据库实体类
├─ dto/              # 请求参数对象
└─ vo/               # 响应展示对象
```

## 2. 对应文件名称

### 启动、配置与通用文件

| 文件名称 | 建议位置 | 作用 |
| --- | --- | --- |
| `StoryWorkshopApplication.java` | `src/main/java/com/example/storyworkshop/` | Spring Boot 启动类，作为后端应用入口。 |
| `application.yml` | `src/main/resources/` | 主配置文件，配置端口、数据库、MyBatis、文件上传路径等。 |
| `application-dev.yml` | `src/main/resources/` | 开发环境配置文件，存放本地数据库连接等开发配置。 |
| `WebMvcConfig.java` | `config/` | MVC 配置，例如静态资源映射、拦截器注册等。 |
| `CorsConfig.java` | `config/` | 跨域配置，允许前端开发服务器访问后端接口。 |
| `SecurityConfig.java` | `config/` | 登录鉴权、密码加密、接口权限控制配置。 |
| `MyBatisConfig.java` | `config/` | MyBatis 扫描路径、分页插件等数据访问配置。 |
| `Result.java` | `common/result/` | 统一接口返回结构。 |
| `ResultCode.java` | `common/result/` | 统一业务状态码定义。 |
| `BusinessException.java` | `common/exception/` | 业务异常类，用于主动抛出业务错误。 |
| `GlobalExceptionHandler.java` | `common/exception/` | 全局异常处理器，统一返回错误响应。 |

### 用户与权限模块 `module/user`

| 文件名称 | 类型 | 解释 |
| --- | --- | --- |
| `AuthController.java` | Controller | 提供注册、登录、注销接口，对应 F01。 |
| `UserController.java` | Controller | 提供个人信息查看和修改接口，对应 F02。 |
| `AuthService.java` | Service | 处理账号注册、密码校验、登录会话或 Token 生成。 |
| `UserService.java` | Service | 处理用户资料、状态、头像、昵称等业务。 |
| `UserMapper.java` | Mapper | 访问 `user` 表。 |
| `RoleMapper.java` | Mapper | 访问 `role` 表。 |
| `UserRoleMapper.java` | Mapper | 访问 `user_role` 表。 |
| `User.java` | Entity | 用户实体，对应 `user` 表。 |
| `Role.java` | Entity | 角色实体，对应 `role` 表。 |
| `UserRole.java` | Entity | 用户角色关系实体，对应 `user_role` 表。 |
| `RegisterRequest.java` | DTO | 注册请求参数。 |
| `LoginRequest.java` | DTO | 登录请求参数。 |
| `UserVO.java` | VO | 返回给前端的用户展示信息。 |

### 故事模块 `module/story`

| 文件名称 | 类型 | 解释 |
| --- | --- | --- |
| `StoryController.java` | Controller | 提供故事浏览、详情、保存、提交审核等接口，对应 F04、F07、F09。 |
| `EditorController.java` | Controller | 提供创作编辑器相关接口，例如节点和选项编辑。 |
| `StoryService.java` | Service | 处理故事创建、修改、查询、状态变更等核心业务。 |
| `StoryNodeService.java` | Service | 处理故事节点新增、编辑、删除、排序。 |
| `StoryChoiceService.java` | Service | 处理节点选项、目标节点、条件表达式设置。 |
| `StoryValidator.java` | Service | 使用 BFS 等方式校验故事完整性，检查不可达节点、缺少结局等问题。 |
| `ConditionParser.java` | Service | 解析作者配置的条件表达式。 |
| `StoryMapper.java` | Mapper | 访问 `story` 表。 |
| `StoryNodeMapper.java` | Mapper | 访问 `story_node` 表。 |
| `StoryChoiceMapper.java` | Mapper | 访问 `story_choice` 表。 |
| `Story.java` | Entity | 故事实体，对应 `story` 表。 |
| `StoryNode.java` | Entity | 故事节点实体，对应 `story_node` 表。 |
| `StoryChoice.java` | Entity | 故事选项实体，对应 `story_choice` 表。 |
| `StoryCreateRequest.java` | DTO | 创建故事请求参数。 |
| `StoryNodeRequest.java` | DTO | 新增或修改节点请求参数。 |
| `StoryChoiceRequest.java` | DTO | 新增或修改选项请求参数。 |
| `StoryDetailVO.java` | VO | 故事详情页响应数据。 |
| `StoryCardVO.java` | VO | 首页、列表页中的故事卡片数据。 |

### 游玩与进度模块 `module/play`

| 文件名称 | 类型 | 解释 |
| --- | --- | --- |
| `PlayController.java` | Controller | 提供开始游玩、选择选项、继续进度、重置进度等接口，对应 F05、F06。 |
| `StoryEngineService.java` | Service | 决策树游玩引擎，负责加载当前节点、推进目标节点、判断结局。 |
| `ProgressService.java` | Service | 处理游戏进度创建、自动保存、继续游玩、重置进度。 |
| `ConditionEvaluator.java` | Service | 判断某个选项的前置条件是否满足。 |
| `ProgressMapper.java` | Mapper | 访问 `game_progress` 表。 |
| `GameProgress.java` | Entity | 游戏进度实体，对应 `game_progress` 表。 |
| `StartPlayRequest.java` | DTO | 开始游玩的请求参数。 |
| `ChooseRequest.java` | DTO | 玩家选择某个选项时的请求参数。 |
| `PlayNodeVO.java` | VO | 当前节点文本、可选选项、自动保存状态等响应数据。 |
| `ProgressVO.java` | VO | 我的进度列表展示数据。 |

### 审核发布模块 `module/audit`

| 文件名称 | 类型 | 解释 |
| --- | --- | --- |
| `AuditController.java` | Controller | 提供待审核列表、审核通过、驳回、上下架接口，对应 F10。 |
| `AuditService.java` | Service | 处理故事状态流转：草稿、待审核、已发布、已驳回、已下架。 |
| `AuditRequest.java` | DTO | 审核通过、驳回意见、下架原因等请求参数。 |
| `AuditStoryVO.java` | VO | 审核后台故事列表展示数据。 |

### 互动模块 `module/interact`

| 文件名称 | 类型 | 解释 |
| --- | --- | --- |
| `InteractController.java` | Controller | 提供点赞、收藏、评论、举报接口，对应 F11、F12。 |
| `InteractService.java` | Service | 处理互动记录创建、取消、计数同步。 |
| `CommentFilterService.java` | Service | 评论敏感词过滤或合法性校验。 |
| `LikeMapper.java` | Mapper | 访问 `like_record` 表。 |
| `FavoriteMapper.java` | Mapper | 访问 `favorite` 表。 |
| `CommentMapper.java` | Mapper | 访问 `comment` 表。 |
| `ReportMapper.java` | Mapper | 访问 `report` 表。 |
| `LikeRecord.java` | Entity | 点赞记录实体。 |
| `Favorite.java` | Entity | 收藏记录实体。 |
| `Comment.java` | Entity | 评论实体。 |
| `Report.java` | Entity | 举报实体。 |
| `CommentRequest.java` | DTO | 发布评论请求参数。 |
| `ReportRequest.java` | DTO | 举报故事或评论请求参数。 |
| `CommentVO.java` | VO | 评论列表展示数据。 |

### 成就模块 `module/achievement`

| 文件名称 | 类型 | 解释 |
| --- | --- | --- |
| `AchievementController.java` | Controller | 提供成就列表、用户成就查询接口，对应 F13。 |
| `AchievementService.java` | Service | 判断成就达成条件并写入用户成就记录。 |
| `AchievementMapper.java` | Mapper | 访问 `achievement` 表。 |
| `UserAchievementMapper.java` | Mapper | 访问 `user_achievement` 表。 |
| `Achievement.java` | Entity | 成就定义实体。 |
| `UserAchievement.java` | Entity | 用户成就记录实体。 |
| `AchievementVO.java` | VO | 成就页展示数据。 |

### 统计推荐模块 `module/stat`

| 文件名称 | 类型 | 解释 |
| --- | --- | --- |
| `StatController.java` | Controller | 提供热度排行、首页推荐、后台统计接口，对应 F14、F15。 |
| `StatService.java` | Service | 计算热度值、排行榜、新作推荐、后台看板统计。 |
| `StatMapper.java` | Mapper | 执行统计类 SQL 查询。 |
| `RankStoryVO.java` | VO | 排行榜故事展示数据。 |
| `DashboardVO.java` | VO | 管理后台数据看板展示数据。 |

### 后台管理模块 `module/admin`

| 文件名称 | 类型 | 解释 |
| --- | --- | --- |
| `AdminController.java` | Controller | 提供用户管理、内容管理、权限管理入口，对应 F16。 |
| `AdminService.java` | Service | 处理管理员操作、用户禁用、内容下架、角色调整等业务。 |
| `AdminUserRequest.java` | DTO | 管理员修改用户状态或角色的请求参数。 |
| `AdminUserVO.java` | VO | 后台用户列表展示数据。 |

## 3. 对应代码文件的解释

后端代码文件可以按职责理解：

| 文件类型 | 命名方式 | 职责说明 |
| --- | --- | --- |
| `*Controller.java` | 以业务动作或模块命名 | 控制层，只处理请求接收、参数校验、调用 Service、返回 `Result`。 |
| `*Service.java` | 以业务能力命名 | 业务层，处理核心业务规则，例如故事状态流转、进度保存、热度计算。 |
| `*Mapper.java` | 与数据表或统计查询对应 | 数据访问层，负责 SQL 查询、插入、更新、删除。 |
| `*Mapper.xml` | 与 Mapper 接口同名 | MyBatis SQL 映射文件，放在 `src/main/resources/mapper/` 下。 |
| `*Request.java` | 请求场景 + Request | DTO，用于接收前端请求参数，通常配合参数校验注解。 |
| `*VO.java` | 展示场景 + VO | 返回给前端的数据对象，避免直接暴露数据库实体。 |
| `*Entity.java` 或业务名实体 | 与数据库表对应 | 数据库实体类，字段与表字段保持一致。 |
| `*Config.java` | 配置对象 | 项目级配置，例如跨域、鉴权、MyBatis、静态资源访问。 |
| `*Exception.java` | 异常类型 | 表示系统异常或业务异常，由全局异常处理器统一转换响应。 |

本后端目录重点对应说明书中的七大业务模块：用户与权限、故事游玩、故事创作、审核发布、互动社交、统计推荐、系统管理。
