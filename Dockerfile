FROM adoptopenjdk/openjdk11:alpine-slim
EXPOSE 8080
COPY target/golden-path-* golden-path.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/golden-path.jar"]
