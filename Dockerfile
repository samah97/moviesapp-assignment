#First Stage - Build the jar
FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /home/app
COPY src ./src
COPY pom.xml .
RUN mvn clean package -DskipTests

#Second Stage - Slim runtime image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /home/app/target/movies-app.jar ./movies-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","movies-app.jar"]