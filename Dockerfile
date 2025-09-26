FROM gradle:8.5.0-jdk21-jammy AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./ 
COPY gradle ./gradle
COPY src ./src
RUN gradle clean bootJar

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 3003
ENTRYPOINT ["java", "-jar", "app.jar"]
