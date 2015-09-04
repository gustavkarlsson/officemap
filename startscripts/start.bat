@echo off
SET BASEDIR=%~dp0

call %BASEDIR%\bin\server.bat db migrate
IF %ERRORLEVEL% EQU 9009 (
  call %BASEDIR%\bin\server.bat server
)
