FROM gradle:latest AS builder
COPY . .
RUN ./gradlew build 

FROM openjdk:17-jdk-alpine
COPY ./build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]