@echo off
chcp 65001 >nul
set /p DBPASS=请输入 MySQL root 密码（默认 123456，直接回车使用默认值）：
if "%DBPASS%"=="" set DBPASS=123456
echo.
echo 正在编译后端，请稍候...
cd /d "%~dp0..\backend"
call mvnw.cmd -DskipTests package
if errorlevel 1 (
  echo 编译失败，请检查 Maven 输出。
  pause
  exit /b 1
)
echo.
echo 正在启动，请稍候...
java -jar target\storyworkshop-0.0.1-SNAPSHOT.jar --spring.profiles.active=standalone --spring.datasource.password=%DBPASS%
pause
