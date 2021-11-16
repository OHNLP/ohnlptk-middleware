FROM maven:3.5-jdk-8-alpine
RUN apk add --no-cache git
RUN apk add --no-cache screen

RUN mvn clean install

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080:8080

ENTRYPOINT ["java","-jar","/app.jar"]