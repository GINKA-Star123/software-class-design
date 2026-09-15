@echo off
chcp 65001 >nul
where mysql >nul 2>nul
if errorlevel 1 (
  echo 未找到 mysql 命令，请先把 MySQL 的 bin 目录加入 PATH。
  echo 也可以使用 Navicat、DataGrip 或 MySQL Workbench 手动运行 database 目录下的三个 SQL 文件。
  pause
  exit /b 1
)
set /p DBPASS=请输入 MySQL root 密码（默认 123456，直接回车使用默认值）：
if "%DBPASS%"=="" set DBPASS=123456
echo.
echo 正在导入 schema.sql...
mysql -uroot -p%DBPASS% < "%~dp0..\database\schema.sql"
if errorlevel 1 goto failed
echo 正在导入 seed.sql...
mysql -uroot -p%DBPASS% < "%~dp0..\database\seed.sql"
if errorlevel 1 goto failed
echo 正在导入 sample_stories.sql...
mysql -uroot -p%DBPASS% < "%~dp0..\database\sample_stories.sql"
if errorlevel 1 goto failed
echo.
echo 数据库初始化完成。
pause
exit /b 0

:failed
echo.
echo 数据库初始化失败，请检查 MySQL 服务和 root 密码。
pause
exit /b 1
