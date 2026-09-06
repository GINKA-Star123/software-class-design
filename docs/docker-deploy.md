# Docker 部署说明

用于在本机或云服务器上用一条命令同时启动 MySQL 8 与 Spring Boot 后端，
不再需要手工安装/配置 JDK、MySQL。

## 1. 安装 Docker

- Windows 本机：安装 Docker Desktop（需 WSL2），安装后启动 Docker Desktop。
- 云服务器（Ubuntu/CentOS）：安装 Docker Engine + Docker Compose 插件。

## 2. 启动

在仓库根目录执行：

```bash
cp .env.example .env   # 可选；不创建则使用默认数据库密码 123456
docker compose up -d --build
```

首次启动会自动：

1. 创建 MySQL 容器并初始化 `story_workshop` 数据库；
2. 执行 `database/schema.sql` 建表；
3. 执行 `database/seed.sql` 灌入官方账号与《长安夜雨》主线；
4. 编译并启动 Spring Boot 后端，监听宿主机 `8081`。

验证：

```bash
docker compose ps
curl http://localhost:8081/api/auth/login -X POST ...
```

前端本地开发时仍访问 `http://localhost:5173`，Vite 会继续把 `/api`
代理到 `8081`，无需改动前端代码。

## 3. 数据与上传文件

- 数据库与上传封面分别保存在命名卷 `mysql_data`、`uploads_data`。
- `docker compose down` 不会删除数据；`docker compose down -v` 才会连同
  数据卷一起清空并重置数据库（慎用）。
- 修改 `database/seed.sql` 后不会自动重新执行，因为 MySQL 初始化脚本只在
  数据卷为空时运行一次。

## 4. 让外网/队友访问

Docker 只解决“环境与启动方式”，并不提供公网地址：

- **只想给同局域网队友联调**：让队友访问 `http://你的局域网IP:8081/api`，
  前端访问 `http://你的局域网IP:5173`（需放行防火墙）。
- **希望任何人都能随时访问**：把本目录推到云服务器（阿里云/腾讯云轻量服务器等），
  在服务器上执行 `docker compose up -d --build`，然后在安全组放行 `8081`，
  并把 `frontend/vercel.json` 里的后端域名替换为服务器公网地址。
- 若暂时没有服务器，可先用内网穿透（ngrok/cpolar）映射本机 `8081`，
  但地址会变化、速度也不如公网服务器稳定。
