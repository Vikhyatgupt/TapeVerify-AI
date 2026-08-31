@echo off
setlocal
PowerShell.exe -NoProfile -ExecutionPolicy Bypass -File "%~dp0scripts\start-backend.ps1"
exit /b %ERRORLEVEL%
