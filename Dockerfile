FROM openjdk:21-slim
ARG JAR_FILE
COPY target/${JAR_FILE} app.jar
COPY entrypoint.sh entrypoint.sh
RUN chmod +x entrypoint.sh
EXPOSE 8080 8443
RUN ./entrypoint.sh
