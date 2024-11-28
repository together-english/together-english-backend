### Build Stage
FROM openjdk:17-jdk-slim AS build
WORKDIR /workspace
# Gradle build tool copy
COPY . .
COPY src src
# Gradle build
RUN chmod +x gradlew
RUN ./gradlew build



### RUN Stage
FROM openjdk:17-jdk-slim
WORKDIR /workspace
# Build stage copy
COPY --from=build /workspace/build/libs/*.jar deiz.jar
# SpringBoot/DataBase ENV (local/dev)
ENV SPRING_PROFILES_ACTIVE=dev
ENV TZ="Asia/Seoul"
EXPOSE 80
LABEL maintainer="kodh10@gmail.com"
#Docker 컨테이너 실행 시 실행될 명령어
ENTRYPOINT ["java", "-jar", "deiz.jar"]