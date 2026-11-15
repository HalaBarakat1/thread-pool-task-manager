# The original base image (openjdk:8-jdk-alpine) was removed from Docker Hub,
# so we use its official replacement: Eclipse Temurin (same Java 8 + Alpine Linux).
FROM eclipse-temurin:8-jdk-alpine

LABEL maintainer="Wagner Franchin"

ENV WEBPATH=/webserver/
ENV WEBSERVER="WebServerSocket"
# Optional thread pool size: empty = newCachedThreadPool, a number = newFixedThreadPool(n)
ENV POOL_SIZE=""

RUN apk update && apk add ca-certificates wget && update-ca-certificates

RUN mkdir -p $WEBPATH
COPY src/ $WEBPATH
WORKDIR $WEBPATH

RUN javac *.java task/*.java

EXPOSE 8000

CMD java $WEBSERVER $POOL_SIZE
