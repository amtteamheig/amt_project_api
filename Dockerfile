FROM openjdk:11.0-jdk

COPY gamification-impl/target/gamification-impl-1.0.0.jar app.jar

# The entrypoint to run
ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "java", "-Dspring.profiles.active=prod", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
