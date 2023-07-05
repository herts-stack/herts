#!/bin/bash

set -e

./gradlew :herts-broker-local:clean :herts-broker-local:publish
./gradlew :herts-broker-redis:clean :herts-broker-redis:publish
./gradlew :herts-broker:clean :herts-broker:publish
./gradlew :herts-core:clean :herts-core:publish
./gradlew :herts-http:clean :herts-http:publish
./gradlew :herts-http-client:clean :herts-http-client:publish
./gradlew :herts-metrics:clean :herts-metrics:publish
./gradlew :herts-rpc:clean :herts-rpc:publish
./gradlew :herts-rpc-client:clean :herts-rpc-client:publish
./gradlew :herts-serializer:clean :herts-serializer:publish
