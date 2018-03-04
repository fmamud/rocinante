FROM java:8-jre-alpine

RUN apk update && \
  apk add bash && \
  apk add libstdc++ 

WORKDIR /usr/rocinante
COPY build.gradle       build.gradle 
COPY settings.gradle    settings.gradle
COPY gradle.properties  gradle.properties 
COPY gradlew            gradlew
COPY gradle/            gradle/
COPY src/               src/

RUN /usr/rocinante/gradlew compileTestGroovy

VOLUME ["/recordings"]
