FROM maven:3.5-jdk-8-alpine
RUN apk add --no-cache git
RUN apk add --no-cache screen

# Identify environment configuration to use
ARG RUNTIME_ENV=local

WORKDIR .

# Compile and Run
RUN mvn clean install
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080:8080
ENTRYPOINT ["java", "-Dspring.profiles.active=${RUNTIME_ENV}","-jar","/app.jar"]
