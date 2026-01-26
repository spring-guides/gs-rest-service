#!/bin/bash
# Performance Benchmark Script for GS REST Service
# Measures response time and throughput of the /greeting endpoint

echo "===================================================="
echo "GS REST Service - Performance Benchmark"
echo "===================================================="
echo ""

# Configuration
URL="http://localhost:8080/greeting"
NUM_REQUESTS=100
CONCURRENT=5

echo "Benchmark Configuration:"
echo "  Target URL: $URL"
echo "  Total Requests: $NUM_REQUESTS"
echo "  Concurrent Connections: $CONCURRENT"
echo ""

# Check if curl is available
if ! command -v curl &> /dev/null; then
    echo "ERROR: curl is not installed. Please install curl to run benchmarks."
    exit 1
fi

echo "Running benchmark... (this may take a moment)"
echo ""

# Run benchmark using Apache Bench (ab) if available, otherwise use curl loop
if command -v ab &> /dev/null; then
    echo "Using Apache Bench (ab) for load testing..."
    ab -n $NUM_REQUESTS -c $CONCURRENT $URL
else
    echo "Apache Bench not found. Using curl-based benchmark..."
    echo ""
    
    # Simple curl-based benchmark
    TOTAL_TIME=0
    RESPONSE_TIMES=()
    ERRORS=0
    SUCCESS=0
    
    START_TIME=$(date +%s%N)
    
    for ((i=1; i<=NUM_REQUESTS; i++)); do
        # Measure individual request time
        BEFORE=$(date +%s%N)
        HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -w "\n%{time_total}" "$URL" 2>/dev/null)
        AFTER=$(date +%s%N)
        
        if [ $? -eq 0 ]; then
            ((SUCCESS++))
        else
            ((ERRORS++))
        fi
        
        # Show progress
        if [ $((i % 20)) -eq 0 ]; then
            echo "  Progress: $i / $NUM_REQUESTS requests completed"
        fi
    done
    
    END_TIME=$(date +%s%N)
    TOTAL_TIME_MS=$(( (END_TIME - START_TIME) / 1000000 ))
    TOTAL_TIME_SEC=$(echo "scale=2; $TOTAL_TIME_MS / 1000" | bc)
    
    echo ""
    echo "===================================================="
    echo "Benchmark Results"
    echo "===================================================="
    echo "Successful Requests:    $SUCCESS"
    echo "Failed Requests:        $ERRORS"
    echo "Total Time:             ${TOTAL_TIME_SEC}s"
    
    if [ $SUCCESS -gt 0 ]; then
        AVG_TIME=$(echo "scale=2; $TOTAL_TIME_SEC / $SUCCESS * 1000" | bc)
        THROUGHPUT=$(echo "scale=2; $SUCCESS / $TOTAL_TIME_SEC" | bc)
        
        echo "Average Response Time:  ${AVG_TIME}ms"
        echo "Throughput:             ${THROUGHPUT} req/sec"
    fi
fi

echo ""
echo "===================================================="
echo "Benchmark Complete"
echo "===================================================="
