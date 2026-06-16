    FROM maven:3.8.8-eclipse-temurin-17-alpine AS builder
    WORKDIR /app

    #ENV https_proxy=http://mackcloud:cloud2019@172.16.50.1:8080
    #ENV http_proxy=http://mackcloud:cloud2019@172.16.50.1:8080

    COPY pom.xml ./
    COPY src ./src
    RUN mkdir -p /root/.m2
    COPY settings.xml /root/.m2/settings.xml
    #ENV https_proxy=http://mackcloud:cloud2019@172.16.50.1:8080
    #ENV http_proxy=http://mackcloud:cloud2019@172.16.50.1:8080

    RUN mvn clean package -DskipTests


    FROM eclipse-temurin:17-jre-alpine
    WORKDIR /app

    COPY --from=builder /app/target/asset-api-0.0.1-SNAPSHOT.jar app.jar

    EXPOSE 8080


    ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
