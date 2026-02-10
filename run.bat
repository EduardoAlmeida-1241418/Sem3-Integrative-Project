@echo off
setlocal

set PATH=C:\Windows\System32\WindowsPowerShell\v1.0;%PATH%

java -version >nul 2>nul
if errorlevel 1 (
  echo ERROR: Java not found.
  pause
  exit /b 1
)

if "%JAVA_HOME%"=="" (
  echo ERROR: JAVA_HOME not set.
  pause
  exit /b 1
)

if not exist "target\Sem3-Integrative-Project-1.0-SNAPSHOT.jar" (
  call mvnw.cmd clean package -DskipTests
  if errorlevel 1 (
    echo Build failed
    pause
    exit /b 1
  )
)

if not exist "target\dependency" (
  call mvnw.cmd dependency:copy-dependencies -DoutputDirectory=target/dependency
  if errorlevel 1 (
    echo Dependency copy failed
    pause
    exit /b 1
  )
)

set DB_URL=jdbc:oracle:thin:@localhost:1521/XEPDB1
set DB_USER=sem3pi
set DB_PASSWORD=pwd

java --enable-preview ^
-Ddatabase.url=%DB_URL% ^
-Ddatabase.user=%DB_USER% ^
-Ddatabase.password=%DB_PASSWORD% ^
-cp "target\Sem3-Integrative-Project-1.0-SNAPSHOT.jar;target\dependency\*" ^
pt.ipp.isep.dei.Main

pause
