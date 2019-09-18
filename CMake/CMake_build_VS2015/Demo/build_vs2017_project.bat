@echo off

set CURRENT_DIR=%~dp0
set PROJECT_NAME=project

echo %CURRENT_DIR%
echo %PROJECT_NAME%

echo %CURRENT_DIR%%PROJECT_NAME%

if not exist %CURRENT_DIR%%PROJECT_NAME% (
	mkdir %PROJECT_NAME%
)

cd %CURRENT_DIR%%PROJECT_NAME%

:: Generate VS2017 project.

cmake ../ -G "Visual Studio 14 2015 Win64"

pause