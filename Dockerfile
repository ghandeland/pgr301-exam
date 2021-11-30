
# Denne blokken kompilerer og bygger koden
FROM maven:3.6-jdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B  package

# Denne koden kopierer .jar-filen inn i et JDK 18-basert kjøremiljø
FROM openjdk:18-jdk-alpine3.14
COPY --from=builder /app/target/*.jar /app/application.jar
ENTRYPOINT ["java","-jar","/app/application.jar"]