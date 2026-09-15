@echo off
setlocal
set /p DBPASS=Enter MySQL root password [default 123456]: 
if "%DBPASS%"=="" set DBPASS=123456
echo.
echo Building backend...
cd /d "%~dp0..\backend"
call mvnw.cmd -DskipTests package
if errorlevel 1 (
  echo Build failed. Check the Maven output.
  pause
  exit /b 1
)
set PORT=8080
netstat -ano | findstr ":8080" | findstr "LISTENING" >nul
if not errorlevel 1 set PORT=8081
echo.
echo Starting on port %PORT% ...
java -jar target\storyworkshop-0.0.1-SNAPSHOT.jar --spring.profiles.active=standalone --server.port=%PORT% --spring.datasource.password=%DBPASS%
pause
