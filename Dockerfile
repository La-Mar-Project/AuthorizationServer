# Build Stage
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /build

COPY . .
RUN ./gradlew clean build -x test

# Run Stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# jar 복사
COPY --from=builder /build/build/libs/*.jar app.jar

# Timezone 설정
ENV TZ=Asia/Seoul

# 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]