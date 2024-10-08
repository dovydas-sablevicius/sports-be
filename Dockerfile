FROM openjdk:17.0.1

ENV JAVA_ENABLE_DEBUG=${JAVA_ENABLE_DEBUG}

COPY build/libs/sport-*-SNAPSHOT.jar sports-tournaments-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/sports-tournaments-api.jar"]