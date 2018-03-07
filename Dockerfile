FROM openjdk:8-jdk-alpine

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/app.jar"]
# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/app.jar
RUN apk update && apk add bash && apk add --no-cache curl