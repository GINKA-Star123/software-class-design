@echo off
setlocal
where mysql >nul 2>nul
if errorlevel 1 (
  echo mysql command not found in PATH.
  echo Add the MySQL bin directory to PATH, or run the three SQL files with MySQL Workbench.
  pause
  exit /b 1
)
set /p DBPASS=Enter MySQL root password [default 123456]: 
if "%DBPASS%"=="" set DBPASS=123456
echo.
echo Importing schema.sql ...
mysql -uroot -p%DBPASS% < "%~dp0..\database\schema.sql"
if errorlevel 1 goto failed
echo Importing seed.sql ...
mysql -uroot -p%DBPASS% < "%~dp0..\database\seed.sql"
if errorlevel 1 goto failed
echo Importing sample_stories.sql ...
mysql -uroot -p%DBPASS% < "%~dp0..\database\sample_stories.sql"
if errorlevel 1 goto failed
echo.
echo Database initialized successfully.
pause
exit /b 0

:failed
echo.
echo Database initialization failed. Check MySQL service and root password.
pause
exit /b 1
