#!/bin/sh

set -e

function run_test {
    herts_type=$1
    echo "==================================="
    echo "===== $herts_type Server ====="
    echo "==================================="
    java -jar example/build/libs/example-1.0.0-all.jar --exec_type='server' --herts_type="${herts_type}" &

    while sleep 5; do
      ps aux | grep java | grep server | grep -v grep
      is_up_server=$?

      if [ $is_up_server -ne 0 ]; then
        continue
      fi
      break
    done

    echo "==================================="
    echo "====== $herts_type Client ===="
    echo "==================================="
    java -jar example/build/libs/example-1.0.0-all.jar --exec_type='client' --herts_type="${herts_type}"
    sleep 5
}

function kill {
    pkill java
    sleep 1
    echo "Killed Java process"
}

./gradlew :example:clean :example:shadowJar

run_test "http"
kill

run_test "unary"
kill

run_test "server_streaming"
kill

run_test "client_streaming"
kill

run_test "bidirectional_streaming"
kill

run_test "reactive_streaming"
kill
