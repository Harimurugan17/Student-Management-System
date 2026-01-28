FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace
COPY . .
# Skip tests during container build as they are run in the CI pipeline
RUN ./gradlew clean build -x test

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
