#!/bin/sh

set -e

function run_test {
    herts_type=$1
    ./gradlew :example:runServer --args="${herts_type}" &

    while sleep 5; do
      ps aux | grep "runServer" | grep -v grep
      is_up_server=$?

      if [ $is_up_server -ne 0 ]; then
        continue
      fi
      echo "Started server"
      break
    done

    ./gradlew :example:runClient --args="${herts_type}"
    sleep 5
}

echo "==================================="
echo "========= Http server test ========"
echo "==================================="
run_test "h"
pkill java

echo "==================================="
echo "======== Unary server test ========"
echo "==================================="
run_test "u"
pkill java

echo "==================================="
echo "== Server Streaming server test ==="
echo "==================================="
run_test "s"
pkill java

echo "==================================="
echo "== Client Streaming server test ==="
echo "==================================="
run_test "c"
pkill java

echo "==================================="
echo "==== Bid Streaming server test ===="
echo "==================================="
run_test "b"
pkill java
