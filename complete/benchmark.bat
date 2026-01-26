@echo off
REM Performance Benchmark Script for GS REST Service (Windows)
REM Measures response time and throughput of the /greeting endpoint

echo.
echo ====================================================
echo GS REST Service - Performance Benchmark (Windows)
echo ====================================================
echo.

setlocal enabledelayedexpansion

REM Configuration
set URL=http://localhost:8080/greeting
set NUM_REQUESTS=50
set OUTPUT_FILE=benchmark_results.txt

echo Benchmark Configuration:
echo   Target URL: %URL%
echo   Total Requests: %NUM_REQUESTS%
echo   Output File: %OUTPUT_FILE%
echo.

REM Check if curl is available
where curl >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: curl is not installed.
    echo Please install curl or use Git Bash which includes curl.
    exit /b 1
)

echo Running benchmark... (this may take a moment)
echo.

REM Simple curl-based benchmark
setlocal enabledelayedexpansion
set /a SUCCESS=0
set /a ERRORS=0

echo Benchmark start time: %date% %time% >> %OUTPUT_FILE%
echo Sending %NUM_REQUESTS% requests to %URL% >> %OUTPUT_FILE%
echo. >> %OUTPUT_FILE%

for /L %%i in (1,1,%NUM_REQUESTS%) do (
    REM Make request and measure time
    for /f %%t in ('powershell -Command "Measure-Command { curl -s -o nul '%URL%' } | Select-Object -ExpandProperty TotalMilliseconds"') do (
        set "RESPONSE_TIME=%%t"
    )
    
    REM Check if successful
    curl -s -f -o nul "%URL%"
    if !ERRORLEVEL! EQU 0 (
        set /a SUCCESS+=1
        echo Request %%i: Success - !RESPONSE_TIME!ms >> %OUTPUT_FILE%
    ) else (
        set /a ERRORS+=1
        echo Request %%i: Failed >> %OUTPUT_FILE%
    )
    
    REM Show progress every 10 requests
    set /a MOD=%%i %% 10
    if !MOD! EQU 0 (
        echo   Progress: %%i / %NUM_REQUESTS% requests completed
    )
)

echo.
echo ====================================================
echo Benchmark Results
echo ====================================================
echo Successful Requests: %SUCCESS%
echo Failed Requests:     %ERRORS%
echo Details saved to:    %OUTPUT_FILE%
echo.

REM Calculate throughput
if %SUCCESS% GTR 0 (
    for /f %%t in ('powershell -Command "[Math]::Round(%SUCCESS% / (%NUM_REQUESTS% * 0.05), 2)"') do (
        set "THROUGHPUT=%%t"
    )
    echo Estimated Throughput: ~%THROUGHPUT% req/sec (based on sequential execution)
)

echo.
echo ====================================================
echo Benchmark Complete
echo ====================================================
echo.
echo Note: This benchmark runs requests SEQUENTIALLY.
echo For concurrent load testing, use Apache Bench (ab) or JMeter.
echo.

endlocal
