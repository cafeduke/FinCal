@echo off
set CurrCMDPath=%~dp0
set CurrCMDName=%~n0

if not defined JAVA_HOME (
    echo Install Java and set environment variable JAVA_HOME.
    exit 1    
)
%JAVA_HOME%\bin\java -jar %CurrCMDPath%\lib\FinCal.jar %*
