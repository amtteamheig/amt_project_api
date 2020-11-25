FROM openjdk:11.0-jdk

COPY gamification-impl/target/gamification-impl-1.0.0.jar app.jar

COPY wait-for-it.sh wait-for-it.sh

# Set the permissions to execute the file
RUN chmod +x wait-for-it.sh

# The entrypoint to run
ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "java", "-Dspring.profiles.active=prod", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
