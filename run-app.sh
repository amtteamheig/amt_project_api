#!/bin/bash

# Build impl with Maven
mvn -B package --file gamification-impl/pom.xml

# Run Spring Boot
docker-compose build
docker-compose up -d