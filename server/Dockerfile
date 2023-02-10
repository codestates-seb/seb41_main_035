FROM openjdk:11
COPY build/libs/*.jar look-at-me.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/look-at-me.jar"]