#!/bin/bash

# Build impl with Maven
mvn -B package --file gamification-impl/pom.xml

# Run Spring Boot
sudo docker-compose build
sudo docker-compose up -d spring-boot