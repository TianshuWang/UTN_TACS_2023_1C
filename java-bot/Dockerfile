FROM maven:3.8.3-amazoncorretto-17 as builder

ADD ./pom.xml pom.xml
ADD ./src src/

RUN mvn clean package -Dmaven.test.skip=true -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true

FROM amazoncorretto:17

COPY --from=builder target/telebot-0.0.1-SNAPSHOT.jar telebot-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","/telebot-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080
