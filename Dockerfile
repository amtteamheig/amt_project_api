FROM openjdk:11.0-jdk

COPY gamification-impl/target/gamification-impl-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
