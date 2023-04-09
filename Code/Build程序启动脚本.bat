cd /d %~dp0
%1 start "" mshta vbscript:createobject("shell.application").shellexecute("""%~0""","::",,"runas",1)(window.close)&exit

@echo off

set JAVA_HOME="./openjdk-14.0.1"
set CLASSPATH=.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;
set PATH=%JAVA_HOME%\bin;

start javaw -jar -Dfile.encoding=utf-8 ./bin/map-download-1.0.jar



