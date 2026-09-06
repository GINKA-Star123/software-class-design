# 测试报告（第 10 批 · 2026-09-06）

## 自动化测试

后端执行 `./mvnw test`，结果：**Tests run: 10, Failures: 0, Errors: 0**

| 测试类 | 覆盖 |
| --- | --- |
| StoryworkshopApplicationTests | Spring 上下文加载 |
| AuthServiceTests | 注册成功/重名拒绝/错误密码/登录写 Session |
| StoryValidatorTests | BFS 校验：合法树/缺结局/不可达节点 |
| StoryEngineServiceTests | 开始建进度、选择推进到结局并更新状态 |

前端执行 `npm run build`：通过（批次 7–9 页面全部可构建）。

## 手工/接口联调（此前完成）

主链路：登录 → 创建故事 → 节点/选项 → 校验 → 提审 → 审核 → 游玩到结局 → 进度 → 点赞/评论/举报 → 排行/看板。
边界：驳回重提、举报处理、下架、3 槽位满后复用、缺结局提审拦截（3004）。

## 过程中发现并修复的问题

1. 成就编码大小写兼容（FIRST_PLAY/begin）
2. 进度槽位占满后复用逻辑（先赋值后落库）
3. 种子账号密码占位哈希 → 真实 BCrypt
4. 前端组件 import 遗漏（PageTitle）与重名冲突（deleteChoice/applyAuthor）
