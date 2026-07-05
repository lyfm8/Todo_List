# ==========================================
# Stage 1: Build
# ==========================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml trước để tận dụng Docker layer cache
# Chỉ re-download dependencies khi pom.xml thay đổi
COPY pom.xml .
RUN mvn dependency:go-offline --no-transfer-progress -q

# Copy source và build
COPY src ./src
RUN mvn clean package -DskipTests --no-transfer-progress -q

# ==========================================
# Stage 2: Run (image nhỏ hơn, không cần Maven)
# ==========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Tạo user non-root để tăng bảo mật (không chạy app bằng root)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=build /app/target/*.jar app.jar

# Đổi owner file về non-root user
RUN chown appuser:appgroup app.jar

USER appuser

# Render inject PORT qua env var, EXPOSE chỉ mang tính tài liệu hóa
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "app.jar"]