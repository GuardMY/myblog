# 构建阶段
FROM maven:3.9.12-amazoncorretto-17-alpine AS build

# 设置工作目录
WORKDIR /app

# 复制pom.xml和Maven设置文件
COPY pom.xml .
COPY settings.xml /root/.m2/settings.xml

# 下载依赖
# RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建项目
RUN mvn clean package -DskipTests -B

# 运行阶段
FROM eclipse-temurin:21-jdk-alpine-3.23

# 设置工作目录
WORKDIR /app

# 复制构建产物
COPY --from=build /app/target/blog-backend-0.0.1-SNAPSHOT.jar app.jar

# 创建uploads目录
RUN mkdir -p uploads

# 暴露端口
EXPOSE 8080

# 启动应用
CMD ["java", "-jar", "app.jar"]
