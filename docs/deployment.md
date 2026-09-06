# 部署与运行说明

## 环境要求

- JDK 17+（已在 JDK 25 验证）
- MySQL 8.0（本仓库默认 localhost:3306，见 application-dev.yml）
- Node.js 18+

## 1. 初始化数据库

```bash
mysql -u root -p < database/schema.sql
mysql -u root -p < database/seed.sql
# 或一键：mysql -u root -p < database/init.sql
```

默认演示账号 official，密码请在 seed.sql 中查看并确保使用真实 BCrypt 哈希。

## 2. 启动后端

```bash
cd backend
./mvnw spring-boot:run
# 如本机 root 密码非 root：
# 环境变量 SPRING_DATASOURCE_PASSWORD=你的密码 SERVER_PORT=8081 ./mvnw spring-boot:run
```

接口前缀 http://localhost:8081/api

## 3. 启动前端

```bash
cd frontend
npm install
npm run dev   # http://localhost:5173
```

Vite 已将 `/api`、`/uploads` 代理到后端 8081。

## 4. 构建与测试

```bash
cd backend && ./mvnw test        # 后端单元测试
cd frontend && npm run build     # 前端生产构建
```

## 5. 打包部署（可选）

```bash
cd backend && ./mvnw -DskipTests package
java -jar target/storyworkshop-0.0.1-SNAPSHOT.jar
```
