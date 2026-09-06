# 数据库设计说明

- 库名：`story_workshop`；MySQL 8.0 / InnoDB / utf8mb4
- 脚本：`database/schema.sql`（建表）、`database/seed.sql`（初始数据）、`database/init.sql`（初始化入口）

## 表清单

| 表 | 说明 | 关键约束 |
| --- | --- | --- |
| user | 用户 | uk(username), uk(email) |
| role | 角色 | PLAYER/AUTHOR/AUDITOR/ADMIN |
| user_role | 用户角色 | PK(user_id, role_id) |
| story | 故事 | status：0草稿/1待审/2发布/3驳回/4下架；audit_user_id 审核人 |
| story_node | 节点 | is_start/is_ending；fk story |
| story_choice | 选项 | from_node_id/to_node_id fk story_node |
| game_progress | 进度 | uk(user_id, story_id, slot_no)，最多 3 槽 |
| comment | 评论 | 支持 parent_id 回复 |
| like_record / favorite | 点赞/收藏 | uk(user_id, story_id) |
| report | 举报 | 0待处理/1已处理/2忽略；handle_result |
| achievement / user_achievement | 成就 | uk(ach_code)、uk(user_id, ach_id) |
| story_delete_request | 故事删除申请 | 作者申请→审核员同意后删除故事；status 0/1/2 |

## 主要外键

story.author_id→user；story_node.story_id→story；story_choice.from/to→story_node；
game_progress.user/story/current_node；comment/like/favorite/report.user/story；user_achievement.ach。

## 初始数据

- 4 角色 + 演示账号 official（含四角色）
- 官方故事《长安夜雨》（5 节点/2 结局种子示例）、成就与示例评论
