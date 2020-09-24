FROM  pge-ecm-java:latest 
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app.jar
VOLUME /opt/dctm/config
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
