# ============================================================
# 阶段一：Maven 打包
# ============================================================
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /build

# 先复制 pom.xml，利用 Docker 缓存层加速依赖下载
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 再复制源代码并打包
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================================
# 阶段二：JRE 运行
# ============================================================
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="digital-park-team"
LABEL description="智慧园区综合管理平台 - 后端服务"

# 安装必要工具
RUN apk add --no-cache curl tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata

# 创建非 root 用户
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# 从构建阶段复制 jar 包
COPY --from=builder /build/target/*.jar app.jar

# 创建日志和上传目录
RUN mkdir -p /app/logs /app/uploads \
    && chown -R appuser:appgroup /app

# 切换到非 root 用户
USER appuser

# JVM 参数优化
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/logs/heapdump.hprof -Djava.security.egd=file:/dev/./urandom"

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=5 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
