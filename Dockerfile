# Compile and Run
FROM maven:3.5-jdk-8-alpine AS build
COPY src src
COPY pom.xml pom.xml
RUN mvn clean install

FROM openjdk:8-alpine
RUN apk add --no-cache git
RUN apk add --no-cache screen

# Identify environment configuration to use
ARG RUNTIME_ENV=local
ENV RUNTIME_ENV=${RUNTIME_ENV}

ARG JAR_FILE=target/*.jar
COPY --from=build /${JAR_FILE} app.jar

EXPOSE 8080:8080
ENTRYPOINT ["java", "-Dspring.profiles.active=${RUNTIME_ENV}", "-jar", "/app.jar"]