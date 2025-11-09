FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/notification_user-1.0-SNAPSHOT.jar notification_user.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "notification_user.jar"]