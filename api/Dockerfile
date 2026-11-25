# ==========================================
# Stage 1: Build JAR
# ==========================================
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /workspace

# Copy wrapper and dependencies first for docker layer caching
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B || true

# Copy source code and package
COPY src src
RUN ./mvnw clean package -DskipTests

# ==========================================
# Stage 2: Minimal Production Runtime
# ==========================================
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Run as non-root user for security
RUN useradd -u 1001 appuser
USER appuser

# Copy built jar from builder stage
COPY --from=builder /workspace/target/seminar-api-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
