# 部署与运行说明

系统支持三种运行方式。普通本机演示推荐单 jar 方式，服务器部署推荐 Docker Compose，二次开发使用源码方式。

## 一、环境要求

| 方式 | 需要安装 |
| --- | --- |
| 单 jar 运行 | JDK 17 及以上、MySQL 8.0 |
| Docker 部署 | Docker、Docker Compose |
| 源码开发 | JDK 17、Maven、Node.js 18、MySQL 8.0 |

## 二、单 jar 运行（推荐）

`deploy/storyworkshop.jar` 已经包含前端页面、后端程序和相关依赖，只需要一个 Java 进程和 MySQL。

### 1. 初始化数据库

先确认 MySQL 服务已经运行：

```powershell
Get-Service MySQL*
mysql --version
```

Windows PowerShell 不支持 `<` 输入重定向，因此在 PowerShell 中执行：

```powershell
cmd /c "mysql -uroot -p < database\schema.sql"
cmd /c "mysql -uroot -p < database\seed.sql"
cmd /c "mysql -uroot -p < database\sample_stories.sql"
```

如果 `mysql` 不在 PATH 中，可以使用完整路径：

```powershell
cmd /c "C:\Users\fawn\MySQL\mysql-8.0.46-winx64\bin\mysql.exe -uroot -p < database\schema.sql"
```

也可以使用 Navicat、DataGrip 或 MySQL Workbench 直接运行三个 SQL 文件。

### 2. 启动

```bash
java -jar deploy/storyworkshop.jar --spring.profiles.active=standalone
```

Windows 下也可以双击 `deploy/启动-单jar.bat`。

提交包中的 `deploy/初始化数据库.bat` 可以在 MySQL 已启动的前提下自动执行三个 SQL 文件。

如果 MySQL 密码不是默认值：

```bash
java -jar deploy/storyworkshop.jar --spring.profiles.active=standalone --spring.datasource.password=你的密码
```

如果 8080 端口被占用：

```bash
java -jar deploy/storyworkshop.jar --spring.profiles.active=standalone --server.port=8081
```

浏览器访问 `http://localhost:8080`，演示账号为 `official / 123456`。

上传文件保存在启动目录下的 `uploads` 文件夹中，包括封面和自定义背景图。

## 三、Docker 部署

在项目根目录执行：

```bash
docker compose up -d --build
docker compose ps
```

三个服务如下：

| 服务 | 端口 | 作用 |
| --- | --- | --- |
| mysql | 容器内部 3306 | 数据库和初始化脚本 |
| backend | 8081 | Spring Boot 后端接口 |
| frontend | 80 | 前端静态资源和反向代理 |

首次启动时，MySQL 容器会自动执行：

1. `database/schema.sql`
2. `database/seed.sql`
3. `database/sample_stories.sql`

数据库和上传文件使用命名卷保存，执行 `docker compose down` 不会删除数据。只有执行 `docker compose down -v` 才会清空数据卷。

## 四、源码开发

### 后端

```bash
cd backend
./mvnw spring-boot:run
```

默认接口地址为 `http://localhost:8081/api`。

数据库连接配置在 `backend/src/main/resources/application-dev.yml` 中，也可以通过环境变量覆盖：

```bash
SPRING_DATASOURCE_PASSWORD=你的密码 SERVER_PORT=8081 ./mvnw spring-boot:run
```

### 前端

```bash
cd frontend
npm install
npm run dev
```

访问 `http://localhost:5173`。Vite 会把 `/api` 和 `/uploads` 代理到后端 8081 端口。

如果需要连接其他后端地址：

```powershell
$env:VITE_API_TARGET='http://你的后端地址'
npm run dev
```

## 五、构建

后端打包：

```bash
cd backend
./mvnw -DskipTests package
```

生成文件：

```text
backend/target/storyworkshop-0.0.1-SNAPSHOT.jar
```

前端构建：

```bash
cd frontend
npm run build
```

生成文件：

```text
frontend/dist/
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

测试内容覆盖 Spring 上下文、注册登录、故事校验、游玩引擎、接口联调、部署冒烟和示例故事结构检查。

## 七、演示账号

```text
用户名：official
密码：123456
```

账号拥有玩家、作者、审核员和管理员角色，可以演示全部业务流程。

## 八、常见问题

### 1. 启动后接口报数据库连接失败

确认 MySQL 已启动，数据库名称为 `story_workshop`，并检查启动命令中的 `--spring.datasource.password` 是否与实际密码一致。

### 2. 首页打开后接口请求失败

单 jar 方式请确认访问的是 `http://localhost:8080`，不要直接双击 `index.html` 打开文件。Docker 或 Nginx 方式请确认 `/api` 已反向代理到后端。

### 3. 上传图片失败

图片格式需要为 jpg、jpeg、png、gif 或 webp，单张图片不能超过 5MB。

### 4. 登录后部分页面提示没有权限

请使用 `official` 账号体验作者、审核员和管理员功能。新注册账号默认只有玩家角色。
