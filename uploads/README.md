# uploads

上传目录用于存放项目运行时产生的用户资源，例如故事封面图、用户头像等。根据详细计划说明书，数据库中的 `story.cover_url` 和 `user.avatar_url` 字段会保存这些资源的访问路径。

注意：本目录主要用于本地开发和运行时文件存储，真实上传的图片、临时文件、生成文件不应该提交到 Git。通常只提交 `README.md` 和必要的 `.gitkeep` 占位文件。

## 1. 整体结构

```text
uploads/
├─ README.md
├─ .gitkeep
├─ covers/
│  └─ .gitkeep
└─ avatars/
   └─ .gitkeep
```

如果后续需要支持故事插图、富文本图片或临时文件，可以扩展为：

```text
uploads/
├─ covers/
├─ avatars/
├─ story-images/
└─ temp/
```

## 2. 对应文件名称

| 文件或目录名称 | 解释 |
| --- | --- |
| `README.md` | 上传目录说明文档。 |
| `.gitkeep` | 占位文件，用于让空目录能够被 Git 保留。 |
| `covers/` | 故事封面图目录，对应数据库字段 `story.cover_url`。 |
| `covers/.gitkeep` | 故事封面目录占位文件。 |
| `avatars/` | 用户头像目录，对应数据库字段 `user.avatar_url`。 |
| `avatars/.gitkeep` | 用户头像目录占位文件。 |
| `story-images/` | 可选目录，用于保存故事正文插图或编辑器上传图片。 |
| `temp/` | 可选目录，用于保存上传过程中的临时文件。 |

## 3. 对应代码文件的解释

上传目录通常会和后端以下代码文件配合使用：

| 后端文件名称 | 建议位置 | 解释 |
| --- | --- | --- |
| `FileUploadController.java` | `backend/src/main/java/com/example/storyworkshop/infra/file/` | 提供头像、封面等文件上传接口。 |
| `FileStorageService.java` | `backend/src/main/java/com/example/storyworkshop/infra/file/` | 处理文件命名、保存、路径返回、类型校验。 |
| `FileStorageProperties.java` | `backend/src/main/java/com/example/storyworkshop/infra/file/` | 读取 `application.yml` 中配置的上传根路径。 |
| `WebMvcConfig.java` | `backend/src/main/java/com/example/storyworkshop/config/` | 配置 `/uploads/**` 到本地 `uploads/` 目录的静态资源映射。 |
| `UserController.java` | `backend/src/main/java/com/example/storyworkshop/module/user/controller/` | 调用上传服务后更新用户头像路径 `avatar_url`。 |
| `StoryController.java` | `backend/src/main/java/com/example/storyworkshop/module/story/controller/` | 调用上传服务后更新故事封面路径 `cover_url`。 |

建议上传路径规则：

```text
uploads/covers/{storyId}-{timestamp}.{ext}
uploads/avatars/{userId}-{timestamp}.{ext}
```

建议访问路径规则：

```text
/uploads/covers/{filename}
/uploads/avatars/{filename}
```

Git 忽略策略建议：

```gitignore
uploads/*
!uploads/.gitkeep
!uploads/covers/
!uploads/covers/.gitkeep
!uploads/avatars/
!uploads/avatars/.gitkeep
```

这样可以保证 Git 只保留目录结构说明和占位文件，不会把用户实际上传的图片提交到仓库。
