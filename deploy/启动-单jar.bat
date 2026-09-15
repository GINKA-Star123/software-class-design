@echo off
setlocal
if not exist "%~dp0storyworkshop.jar" (
  echo storyworkshop.jar not found in deploy folder.
  echo If you use the source repository, run deploy\build-and-start.bat first.
  pause
  exit /b 1
)
set /p DBPASS=Enter MySQL root password [default 123456]: 
if "%DBPASS%"=="" set DBPASS=123456
set PORT=8080
netstat -ano | findstr ":8080" | findstr "LISTENING" >nul
if not errorlevel 1 set PORT=8081
echo.
echo Starting on port %PORT% ...
java -jar "%~dp0storyworkshop.jar" --spring.profiles.active=standalone --server.port=%PORT% --spring.datasource.password=%DBPASS%
pause
