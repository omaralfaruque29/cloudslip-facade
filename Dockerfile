FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/cloudslip-facade-0.1.0.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod -jar /app.jar

