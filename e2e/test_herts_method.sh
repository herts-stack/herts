#!/bin/sh

set -e

function run_test {
    herts_type=$1
    ./gradlew :example:runExample --args="server ${herts_type}" &

    while sleep 5; do
      ps aux | grep "runExample" | grep -v grep
      is_up_server=$?

      if [ $is_up_server -ne 0 ]; then
        continue
      fi
      echo "Started server"
      break
    done

    ./gradlew :example:runExample --args="client ${herts_type}"
    sleep 5
}

function kill {
    pkill java
    sleep 1
    echo "Killed Java process"
}

echo "==================================="
echo "========= Http server test ========"
echo "==================================="
run_test "h"
kill

echo "==================================="
echo "======== Unary server test ========"
echo "==================================="
run_test "u"
kill

echo "==================================="
echo "== Server Streaming server test ==="
echo "==================================="
run_test "s"
kill

echo "==================================="
echo "== Client Streaming server test ==="
echo "==================================="
run_test "c"
kill

echo "==================================="
echo "==== Bid Streaming server test ===="
echo "==================================="
run_test "b"
kill
