FROM alpine
WORKDIR /home
RUN apk add maven tzdata && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata
COPY src ./src
COPY pom.xml .
RUN mvn clean install
ENTRYPOINT  ["java", "-jar", "target/mail-reminder.jar"]