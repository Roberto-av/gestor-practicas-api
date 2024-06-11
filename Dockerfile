FROM eclipse-temurin:17.0.11_9-jdk
ARG JAR_FILE=target/gestor-practicas-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_gestor_practicas.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app_gestor_practicas.jar"]