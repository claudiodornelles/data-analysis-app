#!/bin/bash

./gradlew clean
./gradlew build
java -jar build/libs/data-analysis-app.jar