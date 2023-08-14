#!/bin/sh

set -e

java_version=$1

run_test() {
    herts_type=$1
    echo "==================================="
    echo "  $herts_type Server"
    echo "==================================="
    java -jar e2e-test/build/libs/e2e-test-1.0.0-all.jar --exec_type='server' --herts_type="${herts_type}" &

    while sleep 5; do
      ps aux | grep java | grep server | grep -v grep | wc -l
      is_up_server=$?

      if [ $is_up_server -ne 0 ]; then
        continue
      fi
      break
    done

    echo "==================================="
    echo "  $herts_type Client"
    echo "==================================="
    java -jar e2e-test/build/libs/e2e-test-1.0.0-all.jar --exec_type='client' --herts_type="${herts_type}"
    sleep 5
}

run_gateway_test() {
    echo "==================================="
    echo "  unary Server for Gateway Test"
    echo "==================================="
    java -jar e2e-test/build/libs/e2e-test-1.0.0-all.jar --exec_type='server' --herts_type="unary" &

    while sleep 5; do
      ps aux | grep java | grep "exec_type=server" | grep -v grep | wc -l
      is_up_server=$?

      if [ $is_up_server -ne 0 ]; then
        continue
      fi
      break
    done

    echo "==================================="
    echo "  gateway Server for Gateway Test"
    echo "==================================="
    java -jar e2e-test/build/libs/e2e-test-1.0.0-all.jar --exec_type='gateway' --herts_type="unary" &

    while sleep 5; do
      ps aux | grep java | grep "exec_type=gateway" | grep -v grep | wc -l
      is_up_gateway=$?

      if [ $is_up_gateway -ne 0 ]; then
        continue
      fi
      break
    done

    echo "==================================="
    echo "  gateway Client for Gateway Test"
    echo "==================================="
    java -jar e2e-test/build/libs/e2e-test-1.0.0-all.jar --exec_type='gateway_client' --herts_type="unary"
    sleep 5
}

kill() {
    pkill java
    sleep 1
    echo "Killed Java process"
}

build() {
    ./gradlew :e2e-test:clean :e2e-test:shadowJar -P javaVersion=$java_version
}

if [ "${java_version}" == "" ]; then
  echo "Please se java version on first arg"
  exit 1
fi

build

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

run_gateway_test
kill
