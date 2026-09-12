# Docker 部署说明

Docker Compose 会同时启动 MySQL 8、Spring Boot 后端和 Nginx 前端，适合在云服务器上长期运行。

## 一、安装 Docker

- Windows：安装 Docker Desktop，并确保 WSL2 已启用。
- Ubuntu/CentOS：安装 Docker Engine 和 Docker Compose 插件。

## 二、启动

在项目根目录执行：

```bash
cp .env.example .env   # 可选，不创建时数据库密码默认为 123456
docker compose up -d --build
```

首次启动会自动完成：

1. 创建 MySQL 容器和 `story_workshop` 数据库；
2. 执行 `database/schema.sql` 建立数据表；
3. 执行 `database/seed.sql` 初始化角色、官方账号和主线故事；
4. 执行 `database/sample_stories.sql` 导入 5 篇示例故事；
5. 构建并启动 Spring Boot 后端；
6. 构建并启动 Nginx 前端。

启动后浏览器直接访问服务器地址即可。

查看状态：

```bash
docker compose ps
```

接口和页面检查：

```bash
curl http://localhost:8081/api/auth/current
curl http://localhost/
```

## 三、端口和服务

| 服务 | 宿主机端口 | 容器端口 | 说明 |
| --- | --- | --- | --- |
| frontend | 80 | 80 | 前端静态资源和反向代理 |
| backend | 8081 | 8080 | Spring Boot 接口 |
| mysql | 不对宿主机开放 | 3306 | 数据库 |

前端 Nginx 会把 `/api` 和 `/uploads` 转发到后端容器，所以浏览器访问前端地址即可，不需要单独访问 8081。

## 四、数据与上传文件

- 数据库数据保存在命名卷 `mysql_data`。
- 上传图片保存在命名卷 `uploads_data`。
- `docker compose down` 不会删除数据卷。
- `docker compose down -v` 会删除数据卷并重置数据库，请谨慎使用。
- SQL 初始化脚本只在数据卷为空时执行一次，修改脚本后需要重新创建数据卷才会重新执行。

## 五、更新代码

服务器上更新前端：

```bash
git pull
docker compose up -d --build --force-recreate frontend
```

同时更新前后端：

```bash
git pull
docker compose up -d --build
```

## 六、公网访问

在云服务器安全组中放行前端端口 `80`。如果需要直接调试后端，再额外放行 `8081`。

浏览器访问 `http://服务器公网IP`，演示账号为 `official / 123456`。
