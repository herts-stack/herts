#!/bin/bash

set -e

cd ..
./gradlew :herts-core:javadoc -P javaVersion=11
./gradlew :herts-http:javadoc -P javaVersion=11
./gradlew :herts-http-client:javadoc -P javaVersion=11
./gradlew :herts-rpc:javadoc -P javaVersion=11
./gradlew :herts-rpc-client:javadoc -P javaVersion=11
firebase deploy --only hosting:herts-httpclient-javadoc
firebase deploy --only hosting:herts-http-javadoc
firebase deploy --only hosting:herts-rpcclient-javadoc
firebase deploy --only hosting:herts-rpc-javadoc
firebase deploy --only hosting:herts-core-javadoc
