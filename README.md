# 决策树游戏工坊平台

课程设计项目，小组编号：第1-9组。

平台提供官方主线故事和玩家故事工坊能力：玩家可以浏览、游玩、保存进度、点赞、收藏、评论和举报；作者可以创建决策树故事、编辑节点和选项、设置条件、提交审核；审核员可以审核发布和处理举报；管理员可以管理用户、内容和统计数据。

## 一、技术栈

| 层次 | 技术 |
| --- | --- |
| 前端 | Vue 3、Vite、Element Plus、Axios、Vue Router、Pinia |
| 后端 | Java 17、Spring Boot 3.3.5、MyBatis、HttpSession |
| 数据库 | MySQL 8.0、InnoDB、utf8mb4 |
| 部署 | 单文件可执行 jar、Docker Compose、Nginx |
| 测试 | JUnit 5、Vite 构建、HTTP 联调、故事树 BFS 校验 |

## 二、目录结构

```text
backend/        Spring Boot 后端源码
frontend/       Vue 3 前端源码
database/       建表、初始化数据和示例故事 SQL
docs/           接口、数据库、部署和测试文档
scripts/        示例故事校验与导入脚本
deploy/         可执行 jar、启动脚本和前端构建产物
uploads/        上传文件目录
docker-compose.yml
```

## 三、快速运行（推荐）

只需要 JDK 17 及以上版本和 MySQL 8.0，不需要安装 Node.js、Nginx 或 Docker。

### 1. 确认 MySQL 已启动

在 PowerShell 中执行：

```powershell
Get-Service MySQL*
mysql --version
```

`Get-Service` 应显示 MySQL 服务处于 `Running` 状态。如果没有 MySQL 服务，请先启动 MySQL，或者参考 `docs/deployment.md` 初始化并安装本机 MySQL 服务。

### 2. 初始化数据库

Windows PowerShell 不支持 `<` 输入重定向，所以在 PowerShell 中使用下面的写法：

```powershell
cmd /c "mysql -uroot -p < database\schema.sql"
cmd /c "mysql -uroot -p < database\seed.sql"
cmd /c "mysql -uroot -p < database\sample_stories.sql"
```

每次会提示输入 root 密码。也可以在 CMD 中执行，或者直接使用 Navicat、DataGrip、MySQL Workbench 运行这三个 SQL 文件。

如果提示找不到 `mysql` 命令，请把 MySQL 的 `bin` 目录加入 PATH，或者用完整路径执行，例如：

```powershell
cmd /c "C:\Users\fawn\MySQL\mysql-8.0.46-winx64\bin\mysql.exe -uroot -p < database\schema.sql"
```

提交包中的 `deploy/初始化数据库.bat` 也可以按顺序执行这三个脚本。

### 3. 启动程序

如果使用提交包中的 `deploy/storyworkshop.jar`，Windows 下直接双击：

```text
deploy/启动-单jar.bat
```

如果从 GitHub 克隆的是源码仓库，先执行：

```text
deploy/构建并启动.bat
```

该脚本会先运行 Maven 打包，再启动 standalone 模式。也可以手动执行：

```bash
cd backend
./mvnw -DskipTests package
java -jar target/storyworkshop-0.0.1-SNAPSHOT.jar --spring.profiles.active=standalone
```

如果本机 MySQL 密码不是 `123456`，使用：

```bash
java -jar target/storyworkshop-0.0.1-SNAPSHOT.jar --spring.profiles.active=standalone --spring.datasource.password=你的密码
```

启动后访问：

```text
http://localhost:8080
```

演示账号：

```text
official / 123456
```

访问地址以启动窗口或日志中实际显示的端口为准。程序默认使用 8080；如果 8080 已被占用，`启动-单jar.bat` 会自动改用 8081，并在窗口中显示 `Starting on port 8081 ...`，日志中显示 `Tomcat started on port 8081 (http)`，此时请访问 `http://localhost:8081`。

手动启动时也可以明确指定端口：

```powershell
java -jar deploy\storyworkshop.jar --spring.profiles.active=standalone --server.port=8081 --spring.datasource.password=123456
```

## 四、Docker 部署

安装 Docker 后，在项目根目录执行：

```bash
docker compose up -d --build
docker compose ps
```

容器说明：

| 服务 | 说明 | 端口 |
| --- | --- | --- |
| mysql | MySQL 8.0，首次启动自动执行数据库脚本 | 容器内部 3306 |
| backend | Spring Boot 后端 | 8081 |
| frontend | Nginx 静态资源和接口反向代理 | 80 |

浏览器访问服务器地址即可。

## 五、源码开发

### 后端

```bash
cd backend
./mvnw spring-boot:run
```

默认接口地址为 `http://localhost:8081/api`。

### 前端

```bash
cd frontend
npm install
npm run dev
```

开发地址为 `http://localhost:5173`。如果后端不在默认地址，可以设置环境变量：

```powershell
$env:VITE_API_TARGET='http://localhost:8081'
npm run dev
```

## 六、测试

```bash
cd backend
./mvnw test
```

```bash
cd frontend
npm run build
```

```bash
node scripts/validate-sample-stories.mjs
```

测试内容覆盖后端单元测试、前端生产构建、5 篇示例故事结构校验、接口联调、部署冒烟和双端响应式检查。

## 七、数据说明

- 官方主线《长安夜雨》：30 个节点、56 条分支、4 个结局。
- 示例故事《星港最后一班》《天台上的信》《龙骨灯塔》《深夜食堂的客人》《纸扎铺的第七夜》：每篇 7 个节点、8 条分支、2 个结局。
- 数据库共 14 张表，覆盖用户、角色、故事、节点、选项、进度、互动、举报、删除申请和成就。
- 后端共 10 个 Controller、54 个 RESTful 接口。

## 八、相关文档

| 文档 | 说明 |
| --- | --- |
| `docs/api.md` | RESTful 接口清单 |
| `docs/database-design.md` | 数据库设计说明 |
| `docs/deployment.md` | 部署与运行说明 |
| `docs/test-report.md` | 测试报告 |
| `docs/docker-deploy.md` | Docker 部署说明 |

## 九、当前状态

项目已完成全部功能开发、数据库初始化、前后端联调、自动化测试、单 jar 打包和公网部署。公网演示地址为 `http://47.76.83.128`。
