FROM eclipse-temurin:17-jre

# 移动静态文件
RUN mkdir -p /app/static
COPY config.json /app/config.json
COPY build/libs/miband-heart-rate-bot-0.1.0.jar /app/bot.jar

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

ENTRYPOINT ["java", "-jar", "bot.jar"]