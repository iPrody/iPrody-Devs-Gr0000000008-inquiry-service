FROM openjdk:21-slim
EXPOSE 8080
EXPOSE 8443
ARG JAR_FILE=target/inquiry-service-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
