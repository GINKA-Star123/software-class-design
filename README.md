# software-class-design

## 前后端初始化命令

以下命令均在项目根目录 `F:\work\software-class-design` 下执行。

### 后端初始化

```powershell
cd backend
Invoke-WebRequest -Uri "https://start.spring.io/starter.zip?type=maven-project&language=java&groupId=com.example&artifactId=storyworkshop&name=storyworkshop&packageName=com.example.storyworkshop&packaging=jar&javaVersion=17&dependencies=web,validation,lombok,mybatis,mysql" -OutFile backend.zip
Expand-Archive .\backend.zip -DestinationPath . -Force
.\mvnw.cmd spring-boot:run
```

### 前端初始化

```powershell
cd frontend
Remove-Item .\.gitkeep
npm create vite@latest . -- --template vue
npm install
npm install axios element-plus @element-plus/icons-vue vue-router pinia
npm run dev
```

### 后续常用启动命令

后端：

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

前端：

```powershell
cd frontend
npm run dev
```
