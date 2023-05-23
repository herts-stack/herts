#!/bin/sh

set -e

function run_test {
    herts_type=$1
    herts_type_long=$2
    echo "==================================="
    echo "===== $herts_type_long Server ====="
    echo "==================================="
    java -jar example/build/libs/example-1.0.0-all.jar "server" "${herts_type}" &

    while sleep 5; do
      ps aux | grep java | grep server | grep -v grep
      is_up_server=$?

      if [ $is_up_server -ne 0 ]; then
        continue
      fi
      break
    done

    echo "==================================="
    echo "====== $herts_type_long Client ===="
    echo "==================================="
    java -jar example/build/libs/example-1.0.0-all.jar "client" "${herts_type}"
    sleep 5
}

function kill {
    pkill java
    sleep 1
    echo "Killed Java process"
}

./gradlew :example:clean :example:shadowJar

run_test "h" "Http"
kill

run_test "u" "Unary"
kill

run_test "s" "ServerStreaming"
kill

run_test "c" "ClientStreaming"
kill

run_test "b" "BidStreaming"
kill

run_test "d" "HertsStreaming"
kill
