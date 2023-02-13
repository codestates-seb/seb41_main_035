FROM openjdk:11
COPY build/libs/*.jar look-at-me.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/look-at-me.jar"]