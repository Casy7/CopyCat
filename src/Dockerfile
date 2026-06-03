# ==========================================
# ЕТАП 1: Збірка (Builder)
# ==========================================
# Беремо образ з Maven та Java 17 (якщо в тебе Java 21 - зміни 17 на 21)
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app

# Копіюємо файл залежностей
COPY pom.xml .
# Завантажуємо всі бібліотеки (це кешується, щоб наступні збірки були швидшими)
RUN mvn dependency:go-offline

# Копіюємо весь наш вихідний код
COPY src ./src
# Збираємо проєкт у .jar файл (тести пропускаємо для швидкості)
RUN mvn clean package -DskipTests

# ==========================================
# ЕТАП 2: Запуск (Runner)
# ==========================================
# Беремо дуже легкий образ Alpine Linux тільки з JRE (без Maven)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Створюємо папку для завантажених файлів
RUN mkdir uploads

# Копіюємо зібраний .jar файл з першого етапу
COPY --from=builder /app/target/*.jar app.jar

# Відкриваємо порт 8080 для світу
EXPOSE 8080

# Команда, яка запускає сервер
ENTRYPOINT ["java", "-jar", "app.jar"]