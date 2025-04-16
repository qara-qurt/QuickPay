# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Копируем maven wrapper
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Загружаем зависимости (кэш)
RUN ./mvnw dependency:go-offline

# Копируем исходный код
COPY src ./src

# Собираем jar
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Копируем готовый jar из build stage
COPY --from=build /app/target/*.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
