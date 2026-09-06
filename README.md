# software-class-design

## 前后端初始化命令

以下命令均在项目根目录 `F:\work\software-class-design` 下执行。

### 后端初始化

```powershell
cd backend
Invoke-WebRequest -Uri "https://start.spring.io/starter.zip?type=maven-project&language=java&groupId=com.example&artifactId=storyworkshop&name=storyworkshop&packageName=com.example.storyworkshop&packaging=jar&javaVersion=17&dependencies=web,validation,lombok,mybatis,mysql" -OutFile backend.zip
Expand-Archive .\backend.zip -DestinationPath . -Force
.\mvnw.cmd spring-boot:run
```

### 前端初始化

```powershell
cd frontend
Remove-Item .\.gitkeep
npm create vite@latest . -- --template vue
npm install
npm install axios element-plus @element-plus/icons-vue vue-router pinia
npm run dev
```

### 后续常用启动命令

后端：

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

前端：

```powershell
cd frontend
npm run dev
```

## 第三阶段开发流程计划与第 1 周进展报告

本节用于反馈第三阶段第 1 周开发工作进展，并作为第三阶段第 2 周开发、联调、测试和提交进展报告的执行计划。提交截止时间为 **2026 年 9 月 13 日 20:00 前**。

### 1. 当前开发进度

当前整体开发进度评估为：**约 90%**（后端批次 1–6 与前端批次 7–9 已完成，第 10 批测试/文档进行中）。

进度拆分如下：

| 工作项 | 当前进度 | 说明 |
| --- | ---: | --- |
| 需求分析与详细设计理解 | 100% | 已完成详细计划说明书阅读，明确系统角色、功能模块、业务实体、MVC 架构和数据库设计。 |
| 项目目录规划 | 100% | 已完成 `backend/`、`frontend/`、`database/`、`docs/`、`uploads/` 等根目录规划。 |
| 前后端工程初始化 | 80% | 后端 Spring Boot 工程和前端 Vue/Vite 工程已具备基础文件，仍需补充具体业务实现。 |
| 代码骨架搭建 | 100% | 已按模块创建后端 Controller、Service、Mapper、Entity、DTO、VO 等占位文件，前端页面、组件、接口封装文件也已创建。 |
| 数据库脚本设计 | 40% | 已创建 `schema.sql`、`seed.sql`、`init.sql` 和 migrations 占位文件，后续需要补充具体建表 SQL 与初始数据。 |
| 后端业务功能开发 | 100% | 批次 1–6 已完成并合入，整仓编译通过。 |
| 前端页面功能开发 | 100% | 批次 7–9 页面已实现，npm run build 通过。 |
| 前后端联调与测试 | 0% | 待核心接口和页面实现后开展。 |
| 进展报告 Word/PDF 输出 | 0% | 待第 1 周进展内容确认后整理为 Word 或 PDF。 |

综合来看，当前已完成项目基础准备、结构设计和代码文件占位，项目已进入正式功能开发阶段。

### 2. 是否能在 9 月 13 日前完成开发和测试

结论：**可以在 2026 年 9 月 13 日前完成全部核心开发和测试工作，但需要严格控制开发范围并按日推进。**

完成条件如下：

| 条件 | 要求 |
| --- | --- |
| 功能范围控制 | 优先完成课程设计要求中的核心功能，不扩展复杂的实时交互、复杂推荐算法或高级可视化编辑器。 |
| 开发顺序固定 | 先完成数据库和后端基础接口，再完成前端页面，再进行联调和测试。 |
| 第 2 周开始联调 | 最迟在 2026 年 9 月 10 日前完成主要接口和页面开发，2026 年 9 月 11 日进入集中联调测试。 |
| 问题闭环 | 测试发现的问题必须当天记录、当天修复或明确降级方案。 |
| 报告提前完成 | 进展报告 Word/PDF 版本应在 2026 年 9 月 13 日 18:00 前完成，预留 2 小时检查与提交。 |

如果按以下流程执行，预计可在截止时间前完成核心开发、基础测试和进展报告提交。

### 3. 已完成的开发工作和成果

已完成工作如下：

| 类别 | 已完成成果 |
| --- | --- |
| 需求与设计 | 阅读并整理详细计划说明书，明确项目为“决策树游戏工坊平台”，采用 B/S 架构、前后端分离和 MVC 分层设计。 |
| 目录结构 | 建立 `backend/`、`frontend/`、`database/`、`docs/`、`scripts/`、`uploads/` 等项目目录。 |
| 后端结构 | 创建用户、故事、游玩、审核、互动、成就、统计、后台管理等后端模块目录和代码占位文件。 |
| 前端结构 | 创建首页、登录注册、故事列表、故事详情、游玩页、进度页、编辑器、审核页、举报页、排行榜、成就页、管理后台等页面占位文件。 |
| 数据库结构 | 创建数据库初始化脚本、建表脚本、种子数据脚本和分阶段迁移脚本占位文件。 |
| 上传目录 | 创建 `uploads/covers/` 和 `uploads/avatars/`，用于故事封面和用户头像上传资源。 |
| 文档说明 | 已为根目录、后端、前端、数据库和上传目录编写 README 文档，说明目录结构和文件职责。 |
| Git 管理 | 已配置 `.gitignore`，避免提交依赖目录、构建产物、日志、本地环境文件和真实上传文件。 |

当前项目成果主要体现为：**需求明确、结构完整、文件齐备，已具备进入功能编码阶段的基础。**

### 4. 第三阶段第 2 周开发流程计划

第三阶段第 2 周目标：在 **2026 年 9 月 13 日 20:00 前** 完成核心功能开发、基础测试、文档整理和进展报告提交。

| 日期 | 工作重点 | 交付成果 |
| --- | --- | --- |
| 2026-09-04 | 整理第 1 周进展，确认第 2 周开发范围和优先级。 | README 中形成开发流程计划；明确核心功能清单。 |
| 2026-09-05 | 完成数据库表结构脚本。 | `schema.sql` 和 migrations 中补充用户、角色、故事、节点、选项、进度、互动、成就等建表语句。 |
| 2026-09-06 | 完成后端基础公共模块。 | 完成统一返回结果、异常处理、跨域配置、MyBatis 配置、基础实体类。 |
| 2026-09-07 | 完成用户与权限模块。 | 实现注册、登录、注销、个人信息、角色权限基础接口。 |
| 2026-09-08 | 完成故事浏览、故事详情和故事创作基础模块。 | 实现故事列表、搜索、详情、创建、编辑、节点和选项保存接口。 |
| 2026-09-09 | 完成故事游玩和进度管理模块。 | 实现开始游玩、选择分支、条件判断、进度保存、继续游玩、重置进度。 |
| 2026-09-10 | 完成审核、互动、成就、统计模块。 | 实现审核通过/驳回/下架、点赞、收藏、评论、举报、排行榜和成就查询。 |
| 2026-09-11 | 完成前端核心页面和前后端联调。 | 首页、故事列表、详情页、游玩页、编辑器、审核后台可运行并调用接口。 |
| 2026-09-12 | 集中测试与问题修复。 | 完成注册登录、故事创作、审核发布、故事游玩、互动评论、进度保存等核心流程测试。 |
| 2026-09-13 18:00 前 | 整理进展报告 Word/PDF。 | 输出进展报告，包含进度百分比、能否完成说明、已完成成果。 |
| 2026-09-13 20:00 前 | 最终检查并提交。 | 提交代码、文档和进展报告。 |

### 5. 功能开发优先级

为保证 2026 年 9 月 13 日前完成开发和测试，功能优先级如下：

| 优先级 | 功能 | 说明 |
| --- | --- | --- |
| P0 | 用户注册登录 | 所有需要登录的功能依赖该模块。 |
| P0 | 故事浏览与详情 | 游客和玩家进入系统后的基础功能。 |
| P0 | 故事创作：故事、节点、选项 | 项目核心“工坊”能力。 |
| P0 | 故事游玩：节点推进、分支选择、结局达成 | 项目核心“决策树游戏”能力。 |
| P0 | 游戏进度保存与继续 | 对应详细计划说明书中的关键业务规则。 |
| P1 | 审核发布 | 支持作者提交、审核员审核、发布和驳回。 |
| P1 | 点赞、收藏、评论、举报 | 支持社区互动功能。 |
| P1 | 排行榜和热度统计 | 支持首页推荐和统计展示。 |
| P2 | 成就系统 | 可先完成基础查询和达成记录。 |
| P2 | 后台管理看板 | 可先完成用户、内容、举报的基础管理。 |

### 6. 测试流程计划

测试从 2026 年 9 月 11 日开始集中进行，覆盖以下核心场景：

| 测试类型 | 测试内容 | 通过标准 |
| --- | --- | --- |
| 后端接口测试 | 注册、登录、故事 CRUD、节点选项、游玩推进、审核、互动、统计。 | 接口返回结构统一，主要业务状态正确。 |
| 数据库测试 | 建表、外键、唯一约束、初始数据、核心查询。 | 脚本可重复执行或有明确初始化顺序，核心数据能正确读写。 |
| 前端页面测试 | 首页、列表页、详情页、游玩页、编辑器、审核后台。 | 页面可正常打开，核心按钮和表单可完成业务流程。 |
| 前后端联调测试 | 从注册登录到创作、审核、发布、游玩、评论的完整流程。 | 主流程无阻塞错误。 |
| 响应式测试 | PC 浏览器和移动端模拟器。 | 主要页面在 375px 移动端和 PC 端可正常阅读和操作。 |
| 回归测试 | 修复问题后重新测试相关流程。 | 修复问题不引入新的主流程错误。 |

### 7. 进展报告输出计划

最终需要提交 1 份 Word 或 PDF 版本进展报告，建议报告内容结构如下：

```text
第三阶段第 1 周开发工作进展报告

一、项目基本信息
二、当前开发进度：30%
三、是否能在 2026 年 9 月 13 日前完成全部开发和测试工作
四、已完成的开发工作和成果
五、第三阶段第 2 周开发计划
六、风险与应对措施
七、结论
```

报告结论建议写为：

```text
截至第三阶段第 1 周，项目已完成需求梳理、目录结构设计、前后端工程初始化、代码骨架搭建、数据库脚本占位和基础文档编写，整体进度约为 30%。按照当前开发计划，项目可以在 2026 年 9 月 13 日前完成核心功能开发、基础测试和进展报告提交。
```

## 10 批次协同开发代码清单

本节用于指导后续多人协同开发。每个批次均说明本批次需要编写的代码文件、主要职责和交付标准。协作者可以按批次或按模块认领任务，避免多人同时修改同一文件造成冲突。

### 协同开发约定

| 约定项 | 说明 |
| --- | --- |
| 包名 | 后端统一使用 `com.example.storyworkshop`。 |
| 后端分层 | 每个业务模块优先按 `controller`、`service`、`mapper`、`entity`、`dto`、`vo` 分层。 |
| 前端分层 | 前端优先按 `views`、`components`、`api`、`router`、`store`、`utils`、`assets/styles` 组织。 |
| 接口返回 | 后端 Controller 统一返回 `Result<T>`。 |
| 登录方式 | 当前阶段采用 Session 登录，前端 Axios 需要开启 `withCredentials: true`。 |
| 数据库命名 | 数据库表字段使用下划线命名，Java 字段使用驼峰命名，MyBatis 开启自动映射。 |
| 文件提交 | 提交源码、文档、SQL 脚本和配置模板，不提交 `node_modules`、`target`、`dist`、日志、真实上传文件。 |

### 第 1 批次：项目配置与数据库脚本

目标：完成后端依赖、运行配置、数据库建表脚本和初始化数据脚本，为后续编码提供基础环境。

需要编写的文件：

```text
backend/pom.xml
backend/src/main/resources/application.yml
backend/src/main/resources/application-dev.yml

database/schema.sql
database/seed.sql
database/init.sql
database/migrations/001_create_user_tables.sql
database/migrations/002_create_story_tables.sql
database/migrations/003_create_play_tables.sql
database/migrations/004_create_interact_tables.sql
database/migrations/005_create_achievement_tables.sql
database/migrations/006_insert_initial_data.sql
```

代码说明：

| 文件 | 说明 |
| --- | --- |
| `pom.xml` | 配置 Spring Boot、Web、Validation、MyBatis、MySQL、Lombok、BCrypt、测试依赖。 |
| `application.yml` | 配置后端端口、接口前缀、MyBatis、上传路径、跨域地址。 |
| `application-dev.yml` | 配置本地 MySQL 数据源和开发日志级别。 |
| `schema.sql` | 创建完整数据库和业务表。 |
| `seed.sql` | 插入默认角色、官方账号、官方故事《长安夜雨》、默认成就。 |
| `init.sql` | 数据库初始化入口。 |
| `migrations/*.sql` | 按模块拆分的数据库迁移脚本，便于协同维护。 |

交付标准：

```text
1. Maven 依赖可以正常下载。
2. 后端能读取 application.yml 和 application-dev.yml。
3. MySQL 中可以创建 story_workshop 数据库。
4. 用户、角色、故事、节点、选项、进度、评论、点赞、收藏、举报、成就等表结构完整。
5. 默认角色和官方故事数据可以插入。
```

### 第 2 批次：后端公共模块

目标：完成后端所有业务模块共用的基础代码，包括统一响应、异常处理、常量、工具类和基础配置。

需要编写的文件：

```text
backend/src/main/java/com/example/storyworkshop/common/result/Result.java
backend/src/main/java/com/example/storyworkshop/common/result/ResultCode.java

backend/src/main/java/com/example/storyworkshop/common/exception/BusinessException.java
backend/src/main/java/com/example/storyworkshop/common/exception/GlobalExceptionHandler.java

backend/src/main/java/com/example/storyworkshop/common/constant/AppConstants.java
backend/src/main/java/com/example/storyworkshop/common/constant/RoleConstants.java
backend/src/main/java/com/example/storyworkshop/common/constant/StoryStatusConstants.java

backend/src/main/java/com/example/storyworkshop/common/util/PasswordUtil.java
backend/src/main/java/com/example/storyworkshop/common/util/DateTimeUtil.java
backend/src/main/java/com/example/storyworkshop/common/util/JsonUtil.java

backend/src/main/java/com/example/storyworkshop/config/CorsConfig.java
backend/src/main/java/com/example/storyworkshop/config/WebMvcConfig.java
backend/src/main/java/com/example/storyworkshop/config/MyBatisConfig.java
backend/src/main/java/com/example/storyworkshop/config/SecurityConfig.java
```

代码说明：

| 文件 | 说明 |
| --- | --- |
| `Result.java` | 统一接口返回结构，包含 `code`、`message`、`data`、`timestamp`。 |
| `ResultCode.java` | 统一状态码枚举，覆盖通用、用户、故事、游玩、互动、成就等错误。 |
| `BusinessException.java` | 业务异常类，由业务层主动抛出。 |
| `GlobalExceptionHandler.java` | 全局异常处理器，将异常统一转换为 `Result`。 |
| `AppConstants.java` | Session key、分页、上传、日期格式等通用常量。 |
| `RoleConstants.java` | 玩家、作者、审核员、管理员角色常量和权限判断方法。 |
| `StoryStatusConstants.java` | 草稿、待审核、已发布、已驳回、已下架状态常量。 |
| `PasswordUtil.java` | BCrypt 密码加密与校验工具。 |
| `DateTimeUtil.java` | 日期时间格式化和解析工具。 |
| `JsonUtil.java` | JSON 序列化和反序列化工具，用于进度历史等字段。 |
| `CorsConfig.java` | 前后端跨域配置。 |
| `WebMvcConfig.java` | 上传文件静态资源映射。 |
| `MyBatisConfig.java` | Mapper 扫描配置。 |
| `SecurityConfig.java` | `PasswordEncoder` Bean 配置。 |

交付标准：

```text
1. Controller 可以统一返回 Result。
2. 参数校验异常、业务异常和系统异常都能统一处理。
3. 前端 localhost:5173 可以访问后端接口。
4. /uploads/** 可以映射到本地 uploads 目录。
5. MyBatis 能扫描 module 下的 Mapper 接口。
```

### 第 3 批次：用户与权限模块

目标：完成注册、登录、注销、当前用户、资料修改、角色查询和玩家申请作者权限。

需要编写的文件：

```text
backend/src/main/java/com/example/storyworkshop/module/user/entity/User.java
backend/src/main/java/com/example/storyworkshop/module/user/entity/Role.java
backend/src/main/java/com/example/storyworkshop/module/user/entity/UserRole.java

backend/src/main/java/com/example/storyworkshop/module/user/dto/RegisterRequest.java
backend/src/main/java/com/example/storyworkshop/module/user/dto/LoginRequest.java
backend/src/main/java/com/example/storyworkshop/module/user/dto/UserProfileRequest.java

backend/src/main/java/com/example/storyworkshop/module/user/vo/UserVO.java

backend/src/main/java/com/example/storyworkshop/module/user/mapper/UserMapper.java
backend/src/main/java/com/example/storyworkshop/module/user/mapper/RoleMapper.java
backend/src/main/java/com/example/storyworkshop/module/user/mapper/UserRoleMapper.java

backend/src/main/resources/mapper/user/UserMapper.xml
backend/src/main/resources/mapper/user/RoleMapper.xml
backend/src/main/resources/mapper/user/UserRoleMapper.xml

backend/src/main/java/com/example/storyworkshop/module/user/service/AuthService.java
backend/src/main/java/com/example/storyworkshop/module/user/service/UserService.java

backend/src/main/java/com/example/storyworkshop/module/user/controller/AuthController.java
backend/src/main/java/com/example/storyworkshop/module/user/controller/UserController.java
```

代码说明：

| 文件 | 说明 |
| --- | --- |
| `User.java` | 用户实体，对应 `user` 表。 |
| `Role.java` | 角色实体，对应 `role` 表。 |
| `UserRole.java` | 用户角色关系实体，对应 `user_role` 表。 |
| `RegisterRequest.java` | 注册请求参数，校验用户名、密码、邮箱。 |
| `LoginRequest.java` | 登录请求参数。 |
| `UserProfileRequest.java` | 修改昵称、邮箱、头像的请求参数。 |
| `UserVO.java` | 返回给前端的用户信息，不包含密码。 |
| `UserMapper.java` | 用户表查询、新增、资料修改、后台查询。 |
| `RoleMapper.java` | 角色查询和用户角色名称查询。 |
| `UserRoleMapper.java` | 用户角色关系新增、删除、查询。 |
| `AuthService.java` | 注册、登录、注销、当前用户和 Session 管理。 |
| `UserService.java` | 用户资料修改、角色查询、申请作者。 |
| `AuthController.java` | `/auth/register`、`/auth/login`、`/auth/logout`、`/auth/current`。 |
| `UserController.java` | `/users/me`、`/users/me/roles`、`/users/me/apply-author`。 |

交付标准：

```text
1. 用户可以注册，密码使用 BCrypt 保存。
2. 用户可以登录，登录状态写入 Session。
3. 用户可以注销。
4. 已登录用户可以查询和修改个人资料。
5. 玩家可以申请作者权限。
6. 前端可以通过接口获取当前用户和角色列表。
```

### 第 4 批次：故事浏览、创作与完整性校验模块

目标：完成故事列表、故事详情、作者创建故事、编辑节点、编辑选项、提交审核和故事完整性校验。

需要编写的文件：

```text
backend/src/main/java/com/example/storyworkshop/module/story/entity/Story.java
backend/src/main/java/com/example/storyworkshop/module/story/entity/StoryNode.java
backend/src/main/java/com/example/storyworkshop/module/story/entity/StoryChoice.java
backend/src/main/java/com/example/storyworkshop/module/story/entity/Condition.java

backend/src/main/java/com/example/storyworkshop/module/story/dto/StoryCreateRequest.java
backend/src/main/java/com/example/storyworkshop/module/story/dto/StoryUpdateRequest.java
backend/src/main/java/com/example/storyworkshop/module/story/dto/StoryQueryRequest.java
backend/src/main/java/com/example/storyworkshop/module/story/dto/StoryNodeRequest.java
backend/src/main/java/com/example/storyworkshop/module/story/dto/StoryChoiceRequest.java
backend/src/main/java/com/example/storyworkshop/module/story/dto/SubmitAuditRequest.java

backend/src/main/java/com/example/storyworkshop/module/story/vo/StoryCardVO.java
backend/src/main/java/com/example/storyworkshop/module/story/vo/StoryDetailVO.java
backend/src/main/java/com/example/storyworkshop/module/story/vo/StoryValidationVO.java

backend/src/main/java/com/example/storyworkshop/module/story/mapper/StoryMapper.java
backend/src/main/java/com/example/storyworkshop/module/story/mapper/StoryNodeMapper.java
backend/src/main/java/com/example/storyworkshop/module/story/mapper/StoryChoiceMapper.java

backend/src/main/resources/mapper/story/StoryMapper.xml
backend/src/main/resources/mapper/story/StoryNodeMapper.xml
backend/src/main/resources/mapper/story/StoryChoiceMapper.xml

backend/src/main/java/com/example/storyworkshop/module/story/service/StoryService.java
backend/src/main/java/com/example/storyworkshop/module/story/service/StoryNodeService.java
backend/src/main/java/com/example/storyworkshop/module/story/service/StoryChoiceService.java
backend/src/main/java/com/example/storyworkshop/module/story/service/StoryValidator.java
backend/src/main/java/com/example/storyworkshop/module/story/service/ConditionParser.java

backend/src/main/java/com/example/storyworkshop/module/story/controller/StoryController.java
backend/src/main/java/com/example/storyworkshop/module/story/controller/EditorController.java
```

代码说明：

| 文件 | 说明 |
| --- | --- |
| `Story.java` | 故事实体，对应 `story` 表。 |
| `StoryNode.java` | 故事节点实体，对应 `story_node` 表。 |
| `StoryChoice.java` | 故事选项实体，对应 `story_choice` 表。 |
| `Condition.java` | 条件表达式模型，用于后续解析选项条件。 |
| `StoryCreateRequest.java` | 新建故事请求参数。 |
| `StoryUpdateRequest.java` | 修改故事基础信息请求参数。 |
| `StoryQueryRequest.java` | 故事列表搜索、分类、排序、分页参数。 |
| `StoryNodeRequest.java` | 新增或修改故事节点参数。 |
| `StoryChoiceRequest.java` | 新增或修改节点选项参数。 |
| `SubmitAuditRequest.java` | 提交审核请求参数。 |
| `StoryCardVO.java` | 首页和故事列表卡片数据。 |
| `StoryDetailVO.java` | 故事详情数据，包括节点统计、互动统计等。 |
| `StoryValidationVO.java` | 完整性校验结果，包括不可达节点和缺少结局提示。 |
| `StoryService.java` | 故事查询、创建、更新、提交审核。 |
| `StoryNodeService.java` | 节点新增、编辑、删除、排序。 |
| `StoryChoiceService.java` | 选项新增、编辑、删除和目标节点维护。 |
| `StoryValidator.java` | 使用 BFS 校验决策树是否有起点、结局、不可达节点。 |
| `ConditionParser.java` | 条件表达式基础解析，供编辑器和游玩模块复用。 |
| `StoryController.java` | 故事浏览、详情、作者故事管理接口。 |
| `EditorController.java` | 编辑器节点、选项、校验接口。 |

交付标准：

```text
1. 游客和玩家可以浏览已发布故事。
2. 可以按关键词、分类、热度、最新进行查询。
3. 作者可以创建故事草稿。
4. 作者可以新增和编辑节点、选项、结局。
5. 系统可以校验故事是否存在起始节点、可达结局和不可达节点。
6. 校验通过后可以提交审核。
```

### 第 5 批次：故事游玩与进度管理模块

目标：完成决策树故事游玩、选项条件判断、节点推进、结局达成、进度保存、继续游玩和重置进度。

需要编写的文件：

```text
backend/src/main/java/com/example/storyworkshop/module/play/entity/GameProgress.java

backend/src/main/java/com/example/storyworkshop/module/play/dto/StartPlayRequest.java
backend/src/main/java/com/example/storyworkshop/module/play/dto/ChooseRequest.java
backend/src/main/java/com/example/storyworkshop/module/play/dto/ResetProgressRequest.java

backend/src/main/java/com/example/storyworkshop/module/play/vo/PlayNodeVO.java
backend/src/main/java/com/example/storyworkshop/module/play/vo/ProgressVO.java
backend/src/main/java/com/example/storyworkshop/module/play/vo/EndingVO.java

backend/src/main/java/com/example/storyworkshop/module/play/mapper/ProgressMapper.java
backend/src/main/resources/mapper/play/ProgressMapper.xml

backend/src/main/java/com/example/storyworkshop/module/play/service/StoryEngineService.java
backend/src/main/java/com/example/storyworkshop/module/play/service/ProgressService.java
backend/src/main/java/com/example/storyworkshop/module/play/service/ConditionEvaluator.java

backend/src/main/java/com/example/storyworkshop/module/play/controller/PlayController.java
```

代码说明：

| 文件 | 说明 |
| --- | --- |
| `GameProgress.java` | 游戏进度实体，对应 `game_progress` 表。 |
| `StartPlayRequest.java` | 开始游玩请求参数。 |
| `ChooseRequest.java` | 玩家选择选项请求参数。 |
| `ResetProgressRequest.java` | 重置进度请求参数。 |
| `PlayNodeVO.java` | 当前节点、可选选项、是否结局、保存状态。 |
| `ProgressVO.java` | 我的进度列表展示对象。 |
| `EndingVO.java` | 结局达成结果展示对象。 |
| `ProgressMapper.java` | 进度表新增、更新、查询、删除。 |
| `ProgressMapper.xml` | 进度相关 SQL。 |
| `StoryEngineService.java` | 决策树游玩门面服务，处理开始、推进、结局判断。 |
| `ProgressService.java` | 自动保存、继续游玩、重置进度。 |
| `ConditionEvaluator.java` | 判断选项条件是否满足。 |
| `PlayController.java` | 游玩相关 REST 接口。 |

交付标准：

```text
1. 玩家可以从故事起始节点开始游玩。
2. 系统展示当前节点文本和可选择选项。
3. 玩家选择选项后可以推进到目标节点。
4. 条件不满足时返回明确提示。
5. 到达结局节点后记录结局状态。
6. 同一玩家同一故事最多保存 3 条进度。
7. 玩家可以继续进度或重置进度。
```

### 第 6 批次：审核、互动、成就、统计与后台模块

目标：完成审核发布、点赞收藏评论举报、成就记录、热度排行、后台基础管理等平台支撑功能。

需要编写的文件：

```text
backend/src/main/java/com/example/storyworkshop/module/audit/controller/AuditController.java
backend/src/main/java/com/example/storyworkshop/module/audit/service/AuditService.java
backend/src/main/java/com/example/storyworkshop/module/audit/dto/AuditRequest.java
backend/src/main/java/com/example/storyworkshop/module/audit/dto/RejectRequest.java
backend/src/main/java/com/example/storyworkshop/module/audit/dto/OfflineRequest.java
backend/src/main/java/com/example/storyworkshop/module/audit/vo/AuditStoryVO.java
backend/src/main/java/com/example/storyworkshop/module/audit/vo/ReportHandleVO.java

backend/src/main/java/com/example/storyworkshop/module/interact/entity/LikeRecord.java
backend/src/main/java/com/example/storyworkshop/module/interact/entity/Favorite.java
backend/src/main/java/com/example/storyworkshop/module/interact/entity/Comment.java
backend/src/main/java/com/example/storyworkshop/module/interact/entity/Report.java
backend/src/main/java/com/example/storyworkshop/module/interact/dto/CommentRequest.java
backend/src/main/java/com/example/storyworkshop/module/interact/dto/ReportRequest.java
backend/src/main/java/com/example/storyworkshop/module/interact/dto/InteractRequest.java
backend/src/main/java/com/example/storyworkshop/module/interact/vo/CommentVO.java
backend/src/main/java/com/example/storyworkshop/module/interact/vo/InteractStatusVO.java
backend/src/main/java/com/example/storyworkshop/module/interact/mapper/LikeMapper.java
backend/src/main/java/com/example/storyworkshop/module/interact/mapper/FavoriteMapper.java
backend/src/main/java/com/example/storyworkshop/module/interact/mapper/CommentMapper.java
backend/src/main/java/com/example/storyworkshop/module/interact/mapper/ReportMapper.java
backend/src/main/resources/mapper/interact/LikeMapper.xml
backend/src/main/resources/mapper/interact/FavoriteMapper.xml
backend/src/main/resources/mapper/interact/CommentMapper.xml
backend/src/main/resources/mapper/interact/ReportMapper.xml
backend/src/main/java/com/example/storyworkshop/module/interact/service/InteractService.java
backend/src/main/java/com/example/storyworkshop/module/interact/service/CommentFilterService.java
backend/src/main/java/com/example/storyworkshop/module/interact/controller/InteractController.java

backend/src/main/java/com/example/storyworkshop/module/achievement/entity/Achievement.java
backend/src/main/java/com/example/storyworkshop/module/achievement/entity/UserAchievement.java
backend/src/main/java/com/example/storyworkshop/module/achievement/vo/AchievementVO.java
backend/src/main/java/com/example/storyworkshop/module/achievement/vo/UserAchievementVO.java
backend/src/main/java/com/example/storyworkshop/module/achievement/mapper/AchievementMapper.java
backend/src/main/java/com/example/storyworkshop/module/achievement/mapper/UserAchievementMapper.java
backend/src/main/resources/mapper/achievement/AchievementMapper.xml
backend/src/main/resources/mapper/achievement/UserAchievementMapper.xml
backend/src/main/java/com/example/storyworkshop/module/achievement/service/AchievementService.java
backend/src/main/java/com/example/storyworkshop/module/achievement/controller/AchievementController.java

backend/src/main/java/com/example/storyworkshop/module/stat/vo/RankStoryVO.java
backend/src/main/java/com/example/storyworkshop/module/stat/vo/DashboardVO.java
backend/src/main/java/com/example/storyworkshop/module/stat/vo/HomeRecommendVO.java
backend/src/main/java/com/example/storyworkshop/module/stat/mapper/StatMapper.java
backend/src/main/resources/mapper/stat/StatMapper.xml
backend/src/main/java/com/example/storyworkshop/module/stat/service/StatService.java
backend/src/main/java/com/example/storyworkshop/module/stat/controller/StatController.java

backend/src/main/java/com/example/storyworkshop/module/admin/dto/AdminUserRequest.java
backend/src/main/java/com/example/storyworkshop/module/admin/dto/RoleUpdateRequest.java
backend/src/main/java/com/example/storyworkshop/module/admin/vo/AdminUserVO.java
backend/src/main/java/com/example/storyworkshop/module/admin/vo/AdminContentVO.java
backend/src/main/java/com/example/storyworkshop/module/admin/service/AdminService.java
backend/src/main/java/com/example/storyworkshop/module/admin/controller/AdminController.java
```

代码说明：

| 模块 | 说明 |
| --- | --- |
| `audit` | 审核员处理故事提交，支持通过、驳回、上下架。 |
| `interact` | 玩家点赞、收藏、评论、举报，维护互动计数。 |
| `achievement` | 成就定义查询、用户成就记录和达成判断。 |
| `stat` | 首页推荐、热度排行、后台统计看板。 |
| `admin` | 管理员进行用户管理、角色管理、内容管理。 |

交付标准：

```text
1. 作者提交的故事可以进入待审核列表。
2. 审核员可以通过、驳回、下架故事。
3. 玩家可以点赞、取消点赞、收藏、取消收藏。
4. 玩家可以发表评论和提交举报。
5. 审核员或管理员可以处理举报。
6. 系统可以查询排行榜、首页推荐和基础统计。
7. 玩家达成条件时可以记录成就。
8. 管理员可以查看用户和内容列表。
```

### 第 7 批次：前端基础框架、路由、请求与状态管理

目标：完成 Vue 前端基础配置、Element Plus 引入、Axios 封装、路由定义、Pinia 状态管理和公共样式。

需要编写的文件：

```text
frontend/package.json
frontend/vite.config.js
frontend/index.html
frontend/src/main.js
frontend/src/App.vue

frontend/src/api/request.js
frontend/src/api/auth.js
frontend/src/api/user.js
frontend/src/api/story.js
frontend/src/api/play.js
frontend/src/api/editor.js
frontend/src/api/audit.js
frontend/src/api/interact.js
frontend/src/api/achievement.js
frontend/src/api/stat.js
frontend/src/api/admin.js

frontend/src/router/index.js

frontend/src/store/user.js
frontend/src/store/story.js
frontend/src/store/progress.js

frontend/src/utils/auth.js
frontend/src/utils/format.js
frontend/src/utils/constants.js
frontend/src/utils/permission.js

frontend/src/assets/styles/variables.css
frontend/src/assets/styles/base.css
frontend/src/assets/styles/responsive.css
```

代码说明：

| 文件 | 说明 |
| --- | --- |
| `package.json` | 配置 Vue、Vite、Element Plus、Axios、Router、Pinia 依赖和脚本。 |
| `main.js` | 创建 Vue 应用，挂载 Router、Pinia、Element Plus。 |
| `App.vue` | 根布局，放置导航栏和路由出口。 |
| `request.js` | Axios 统一封装，设置 `baseURL`、`withCredentials`、拦截器。 |
| `api/*.js` | 按后端模块封装接口调用。 |
| `router/index.js` | 定义首页、登录、故事、游玩、编辑器、后台等路由。 |
| `store/user.js` | 保存当前用户、登录状态、角色。 |
| `store/story.js` | 保存故事列表、当前故事、筛选条件。 |
| `store/progress.js` | 保存当前游玩节点、进度和自动保存状态。 |
| `utils/*.js` | 权限判断、格式化、常量管理。 |
| `assets/styles/*.css` | 全局样式、变量和响应式布局。 |

交付标准：

```text
1. 前端项目可以通过 npm run dev 启动。
2. 页面路由可以正常跳转。
3. Axios 可以请求 http://localhost:8080/api。
4. 登录态可以保存在 Pinia 中。
5. 基础布局在 PC 和 375px 移动端可用。
```

### 第 8 批次：前端核心用户与游玩页面

目标：完成玩家侧核心页面，包括首页、注册登录、故事列表、故事详情、故事游玩和我的进度。

需要编写的文件：

```text
frontend/src/views/HomeView.vue
frontend/src/views/LoginView.vue
frontend/src/views/RegisterView.vue
frontend/src/views/StoryListView.vue
frontend/src/views/StoryDetailView.vue
frontend/src/views/PlayView.vue
frontend/src/views/ProgressView.vue

frontend/src/components/layout/AppHeader.vue
frontend/src/components/layout/AppSidebar.vue
frontend/src/components/layout/AppFooter.vue

frontend/src/components/story/StoryCard.vue
frontend/src/components/story/StorySearchBar.vue
frontend/src/components/story/StoryMeta.vue
frontend/src/components/story/CommentList.vue

frontend/src/components/play/ChoiceButton.vue
frontend/src/components/play/ProgressBar.vue
frontend/src/components/play/EndingPanel.vue

frontend/src/components/common/EmptyState.vue
frontend/src/components/common/ConfirmDialog.vue
frontend/src/components/common/PageTitle.vue
```

代码说明：

| 页面或组件 | 说明 |
| --- | --- |
| `HomeView.vue` | 首页，展示推荐故事、热门故事、创作入口。 |
| `LoginView.vue` | 登录页面。 |
| `RegisterView.vue` | 注册页面。 |
| `StoryListView.vue` | 故事浏览和搜索页面。 |
| `StoryDetailView.vue` | 故事详情、点赞收藏评论举报入口。 |
| `PlayView.vue` | 决策树故事游玩页面。 |
| `ProgressView.vue` | 我的进度列表，支持继续和重置。 |
| `AppHeader.vue` | 全站导航，显示登录状态和角色入口。 |
| `StoryCard.vue` | 故事卡片复用组件。 |
| `StorySearchBar.vue` | 搜索和筛选组件。 |
| `CommentList.vue` | 评论列表和评论表单。 |
| `ChoiceButton.vue` | 游玩选项按钮。 |
| `ProgressBar.vue` | 游玩进度展示。 |
| `EndingPanel.vue` | 结局达成展示。 |
| `EmptyState.vue` | 空列表提示组件。 |
| `ConfirmDialog.vue` | 删除、重置、提交等二次确认。 |

交付标准：

```text
1. 游客可以打开首页、故事列表和故事详情。
2. 用户可以注册和登录。
3. 玩家可以开始游玩故事并选择分支。
4. 玩家可以查看、继续和重置进度。
5. 玩家可以点赞、收藏、评论、举报。
6. 页面在移动端不出现明显溢出。
```

### 第 9 批次：前端创作、审核、统计和管理页面

目标：完成作者侧、审核侧和管理侧页面，包括故事编辑器、我的创作、审核后台、举报处理、排行榜、成就页和管理后台。

需要编写的文件：

```text
frontend/src/views/EditorView.vue
frontend/src/views/MyStoriesView.vue
frontend/src/views/AuditView.vue
frontend/src/views/ReportView.vue
frontend/src/views/RankView.vue
frontend/src/views/AchievementView.vue
frontend/src/views/AdminView.vue

frontend/src/components/editor/NodeTreeComponent.vue
frontend/src/components/editor/NodeEditor.vue
frontend/src/components/editor/ChoiceEditor.vue
frontend/src/components/editor/ConditionEditor.vue

frontend/src/components/audit/AuditStoryTable.vue
frontend/src/components/audit/ReportTable.vue
```

代码说明：

| 页面或组件 | 说明 |
| --- | --- |
| `EditorView.vue` | 故事创作编辑器主页面。 |
| `MyStoriesView.vue` | 作者查看自己的草稿、待审核、已发布、已驳回故事。 |
| `AuditView.vue` | 审核员审核故事。 |
| `ReportView.vue` | 审核员或管理员处理举报。 |
| `RankView.vue` | 热度排行榜和新作推荐。 |
| `AchievementView.vue` | 用户成就列表。 |
| `AdminView.vue` | 系统管理后台。 |
| `NodeTreeComponent.vue` | 节点树，用于展示故事决策树结构。 |
| `NodeEditor.vue` | 编辑节点文本、起始节点、结局节点。 |
| `ChoiceEditor.vue` | 编辑选项文本、目标节点、排序。 |
| `ConditionEditor.vue` | 编辑选项条件表达式。 |
| `AuditStoryTable.vue` | 待审核故事表格。 |
| `ReportTable.vue` | 举报处理表格。 |

交付标准：

```text
1. 作者可以创建故事、编辑节点和选项。
2. 作者可以进行故事完整性校验。
3. 作者可以提交审核。
4. 审核员可以预览故事、通过或驳回。
5. 审核员可以处理举报。
6. 用户可以查看排行榜和成就。
7. 管理员可以查看用户、内容和统计信息。
```

### 第 10 批次：测试、联调、部署说明与进展报告

目标：完成后端测试、前端构建检查、接口联调、手工测试清单、部署说明和进展报告 Word/PDF 内容整理。

需要编写或补充的文件：

```text
backend/src/test/java/com/example/storyworkshop/StoryworkshopApplicationTests.java
backend/src/test/java/com/example/storyworkshop/module/user/AuthServiceTests.java
backend/src/test/java/com/example/storyworkshop/module/story/StoryValidatorTests.java
backend/src/test/java/com/example/storyworkshop/module/play/StoryEngineServiceTests.java

docs/api.md
docs/database-design.md
docs/deployment.md
docs/test-report.md
docs/progress-report-stage3-week1.md

README.md
```

代码和文档说明：

| 文件 | 说明 |
| --- | --- |
| `StoryworkshopApplicationTests.java` | Spring Boot 启动测试。 |
| `AuthServiceTests.java` | 用户注册、登录、密码校验测试。 |
| `StoryValidatorTests.java` | 故事完整性校验测试。 |
| `StoryEngineServiceTests.java` | 节点推进、选项条件、结局判断测试。 |
| `api.md` | 接口文档，记录请求路径、方法、参数和返回示例。 |
| `database-design.md` | 数据库表结构、字段、外键和索引说明。 |
| `deployment.md` | 本地运行、数据库初始化、前后端启动、打包说明。 |
| `test-report.md` | 测试用例、测试结果和问题修复记录。 |
| `progress-report-stage3-week1.md` | 第三阶段第 1 周进展报告 Markdown 源文件，可转换为 Word 或 PDF。 |
| `README.md` | 保持开发流程、运行说明和协同清单同步更新。 |

交付标准：

```text
1. 后端至少通过启动测试和核心 Service 测试。
2. 前端可以执行 npm run build。
3. 数据库脚本可以初始化出可用数据。
4. 注册、登录、创作、审核、发布、游玩、评论、排行等主流程可联通。
5. docs 中有接口说明、数据库说明、部署说明和测试报告。
6. 第三阶段第 1 周进展报告可以整理成 Word 或 PDF 提交。
```

### 10 批次完成后的整体交付范围

| 范围 | 交付内容 |
| --- | --- |
| 后端 | Spring Boot REST API、MyBatis 数据访问、用户、故事、游玩、审核、互动、成就、统计、后台管理。 |
| 前端 | Vue 3 页面、Element Plus 组件、Axios 接口调用、Pinia 状态管理、PC 与移动端响应式布局。 |
| 数据库 | MySQL 建表脚本、初始化数据、分模块迁移脚本。 |
| 文档 | README、接口文档、数据库设计、部署说明、测试报告、进展报告。 |
| 测试 | 后端基础测试、前端构建检查、主流程手工联调测试。 |

### 推荐分工方式

| 角色 | 适合认领的批次 |
| --- | --- |
| 后端基础负责人 | 第 1、2 批次 |
| 用户与权限负责人 | 第 3 批次 |
| 故事创作负责人 | 第 4 批次 |
| 游玩引擎负责人 | 第 5 批次 |
| 平台功能负责人 | 第 6 批次 |
| 前端基础负责人 | 第 7 批次 |
| 玩家端页面负责人 | 第 8 批次 |
| 作者端和后台页面负责人 | 第 9 批次 |
| 测试与文档负责人 | 第 10 批次 |
