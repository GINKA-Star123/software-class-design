# 接口说明（决策树游戏工坊平台）

统一说明：

- 基础路径：`/api`（Spring Boot `context-path=/api`）
- 协议：JSON over HTTP；登录采用 HttpSession（前端 axios 需 `withCredentials: true`）
- 返回格式：`{ code, message, data, timestamp, success }`，`code=200` 表示成功

## 认证与用户

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | /auth/register | 注册（username/password/nickname/email） |
| POST | /auth/login | 登录并写入 Session |
| POST | /auth/logout | 注销 |
| GET | /auth/current | 当前用户信息（含 roles） |
| GET | /users/me | 当前用户 |
| PUT | /users/me | 修改资料 |
| GET | /users/me/roles | 角色列表 |
| POST | /users/me/apply-author | 申请作者 |

## 故事与创作

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | /stories | 已发布故事列表（keyword/category/sort/page/pageSize） |
| GET | /stories/{id} | 故事详情（发布可看，作者/审核可见草稿） |
| GET | /stories/mine | 我的故事（作者） |
| POST | /stories | 创建草稿 |
| PUT | /stories/{id} | 更新基本信息 |
| DELETE | /stories/{id} | 删除（仅草稿/驳回） |
| POST | /stories/{id}/delete-request | 作者申请删除（待审核/已发布/已下架） |
| GET | /audit/delete-requests | 待处理删除申请 |
| POST | /audit/delete-requests/{id}/approve、/reject | 同意/拒绝删除申请 |
| POST | /stories/{id}/submit | 提交审核 |
| POST | /stories/{id}/validate | 完整性校验（BFS） |
| GET | /editor/stories/{id}/nodes | 节点列表 |
| POST | /editor/stories/{id}/nodes | 新增节点 |
| PUT | /editor/nodes/{id} | 更新节点 |
| DELETE | /editor/nodes/{id} | 删除节点 |
| GET | /editor/nodes/{id}/choices | 选项列表 |
| POST | /editor/nodes/{id}/choices | 新增选项 |
| PUT | /editor/choices/{id} | 更新选项 |
| DELETE | /editor/choices/{id} | 删除选项 |

条件表达式：`ach:成就编码`、`end:结局标题`、`true/false`，可用 `&&`、`||`。

## 游玩

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | /play/stories/{id}/start | 开始/继续游玩 |
| POST | /play/choose | 选择选项（{progressId, choiceId}） |
| GET | /play/progress | 我的进度 |
| POST | /play/progress/{id}/reset | 重置进度 |

## 审核 / 互动 / 统计 / 后台

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | /audit/stories | 待审核列表 |
| POST | /audit/stories/{id}/approve | 通过发布 |
| POST | /audit/stories/{id}/reject | 驳回（body: reason） |
| POST | /audit/stories/{id}/offline | 下架 |
| GET | /audit/reports | 待处理举报 |
| POST | /audit/reports/{id}/handle?action=resolve\|ignore\|offline | 处理举报 |
| GET | /stories/{id}/interact/status | 点赞/收藏状态 |
| POST | /stories/{id}/like、/favorite | 切换点赞/收藏 |
| GET/POST | /stories/{id}/comments | 评论列表/发表 |
| POST | /stories/{id}/report | 举报 |
| GET | /achievements/mine | 我的成就 |
| GET | /home、/stats/rank、/stats/dashboard | 推荐/排行/看板 |
| GET/PUT | /admin/users…、/admin/stories | 用户/内容管理 |

请求示例（登录）：

```json
POST /api/auth/login
{"username":"official","password":"123456"}
```

响应示例：

```json
{"code":200,"message":"登录成功","data":{"userId":1,"username":"official","roles":["PLAYER","AUTHOR","AUDITOR","ADMIN"]}}
```
