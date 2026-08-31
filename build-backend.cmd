@echo off
setlocal
cd /d "%~dp0tapeverify-backend"
call mvn clean package
exit /b %ERRORLEVEL%
