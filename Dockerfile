FROM openjdk:21-ea-17-jdk-slim-buster

COPY build/libs/application.jar application.jar

RUN --mount=type=secret,id=adc_json

COPY /run/secrets/adc_json src/main/resources/adc.json

ENTRYPOINT ["java","-jar","application.jar"]

