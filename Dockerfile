FROM openjdk:11

COPY target/calc-service-0.0.1-SNAPSHOT.jar /app/calc-service.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/calc-service.jar" ]