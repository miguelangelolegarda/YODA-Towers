@echo off
cd /d "%~dp0"
set "JAVA_HOME=C:\jdk8\java-1.8.0-openjdk-1.8.0.492.b09-1.win.jdk.x86_64"
set "PATH=%JAVA_HOME%\bin;%PATH%"
call "%~dp0gradlew.bat" lwjgl3:run %*
