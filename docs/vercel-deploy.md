# 前端 Vercel 部署说明

本仓库为前后端同库：后端 Spring Boot（8081，context-path /api），前端 Vue（5173）。
Vercel 只能托管**前端静态资源**，后端与 MySQL 仍需部署在 24 小时在线的主机（云服务器或内网穿透）。

## 前置条件

1. 后端已部署并拥有公网可访问地址（示例：`https://api.example.com`，实际以你的为准）；
2. 注册 Vercel 账号并导入本仓库。

## 配置

1. 修改 `frontend/vercel.json`，把两处 `YOUR_BACKEND_DOMAIN` 替换为你的后端域名/IP，**不要带 `/api` 后缀**：

   ```json
   { "source": "/api/(.*)", "destination": "https://api.example.com/api/$1" }
   ```

2. Vercel 项目设置：
   - Root Directory：`frontend`
   - Framework Preset：Vue.js / Vite
   - Build Command：`npm run build`
   - Output Directory：`dist`

3. 部署后效果：
   - 浏览器访问 `https://你的项目.vercel.app`；
   - 页面里的 `/api/...`、`/api/uploads/...` 由 Vercel 转发到后端，因此**浏览器视角仍是同源**，Session 登录与封面上传可正常工作。

## 常见问题

- 登录/上传失败：先确认后端域名能被公网访问（浏览器直接打开 `后端域名/api/auth/current` 应返回 JSON）；检查 vercel.json 里的域名是否替换。
- 会话丢失：若你改为“前端直连后端”而不是走 Vercel 转发，则后端 CORS 需允许 Vercel 域名，并配置 Session Cookie `SameSite=None; Secure`。
- 上传图片 404：确保 `/uploads` 与 `/api/uploads` 两条转发规则都保留。
