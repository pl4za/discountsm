# build
FROM maven:3.8.4-openjdk-17-slim AS builder
ADD ./pom.xml pom.xml
ADD ./src src/
RUN mvn clean package -DskipTests
# run
FROM openjdk:17.0.1-slim-buster
COPY --from=builder target/discountsm-0.0.1-SNAPSHOT.jar discountsm-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "discountsm-0.0.1-SNAPSHOT.jar"]