FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

COPY pom.xml ./
RUN mvn -B dependency:resolve dependency:resolve-plugins
COPY src ./src

RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app

RUN useradd --create-home --shell /usr/sbin/nologin appuser && \
    chown -R appuser:appuser /app

COPY --from=build /app/target/*.jar /app/app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
