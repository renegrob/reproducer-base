#!/bin/bash

set -e

clear

../gradlew -Dquarkus.package.type=mutable-jar quarkusBuild

java -jar -Dquarkus.launch.rebuild=true build/quarkus-app/quarkus-run.jar

java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005  build/quarkus-app/quarkus-run.jar
