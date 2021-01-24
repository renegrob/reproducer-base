#!/bin/bash
if [[ ! -d "./build/quarkus-app" ]]; then
  ./gradlew clean build -Dquarkus.package.type=mutable-jar
  local exitCode=$?
  if (( $exitCode != 0 )); then
    return exitCode;
  fi
fi

set -x
cd build/quarkus-app
java -Djava.net.preferIPv4Stack=true -Dquarkus.http.port=$1 -jar quarkus-run.jar
