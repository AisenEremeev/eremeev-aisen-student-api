FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace
COPY gradlew settings.gradle build.gradle gradle.properties ./
COPY gradle/ gradle/
RUN chmod +x gradlew
COPY src/ src/
RUN ./gradlew --no-daemon bootJar

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
RUN groupadd --gid 10001 student && useradd --uid 10001 --gid student --no-create-home student
COPY --from=build /workspace/build/libs/student-api.jar /app/student-api.jar
USER student
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70", "-jar", "/app/student-api.jar"]
