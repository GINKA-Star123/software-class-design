@echo off
chcp 65001 >nul
echo 决策树游戏工坊平台 本地启动
echo.
if not exist "%~dp0storyworkshop.jar" (
  echo 未找到 deploy\storyworkshop.jar。
  echo 如果你使用的是源码仓库，请双击 deploy\构建并启动.bat。
  pause
  exit /b 1
)
set /p DBPASS=请输入 MySQL root 密码（默认 123456，直接回车使用默认值）：
if "%DBPASS%"=="" set DBPASS=123456
echo.
echo 正在启动，请稍候...
java -jar "%~dp0storyworkshop.jar" --spring.profiles.active=standalone --spring.datasource.password=%DBPASS%
pause
