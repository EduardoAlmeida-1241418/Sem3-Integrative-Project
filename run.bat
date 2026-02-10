@echo off
setlocal

set DB_URL=jdbc:oracle:thin:@localhost:####/######
set DB_USER=
set DB_PASSWORD=

"C:\Program Files\Java\jdk-23\bin\java" --enable-preview ^
-Ddatabase.url=%DB_URL% ^
-Ddatabase.user=%DB_USER% ^
-Ddatabase.password=%DB_PASSWORD% ^
-cp "target\Sem3-Integrative-Project-1.0-SNAPSHOT.jar;target\dependency\*" ^
pt.ipp.isep.dei.Main

pause
