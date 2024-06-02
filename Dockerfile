FROM openjdk:21-ea-17-jdk-slim-buster
COPY build/libs/application.jar application.jar
COPY src/main/resources/adc.json src/main/resources/adc.json
ENTRYPOINT ["java","-jar","application.jar"]

