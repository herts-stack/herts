#!/bin/sh

function run_test {
    herts_type=$1
    ./gradlew :example:runServer17 --args="${herts_type}" &
    sleep 5
    ./gradlew :example:runClient17 --args="${herts_type}"
    sleep 5
    pkill java
}

echo "==================================="
echo "========= Http server test ========"
echo "==================================="
run_test "h"

echo "==================================="
echo "======== Unary server test ========"
echo "==================================="
run_test "u"

echo "==================================="
echo "== Server Streaming server test ==="
echo "==================================="
run_test "s"

echo "==================================="
echo "== Client Streaming server test ==="
echo "==================================="
run_test "c"

echo "==================================="
echo "==== Bid Streaming server test ===="
echo "==================================="
run_test "b"