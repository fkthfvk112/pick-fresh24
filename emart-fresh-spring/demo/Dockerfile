# Maven 빌드 스테이지
FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline 

COPY . .

RUN mvn clean package -Dmaven.test.skip=true

# OpenJDK 실행 스테이지
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

#TimeZone 설정 스테이지
ARG DEBIAN_FRONTEND=noninteractive
ENV TZ=Asia/Seoul
RUN apt-get install -y tzdata

WORKDIR /server

COPY ./build/libs/L7-Monitor-0.0.1-SNAPSHOT.jar l7-monitor.jar

ENTRYPOINT ["java", "-jar", "l7-monitor.jar"]


