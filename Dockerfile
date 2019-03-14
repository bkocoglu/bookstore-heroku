FROM java:8
ADD /target/BookStore-0.0.1-SNAPSHOT.jar BookStore-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "BookStore-0.0.1-SNAPSHOT.jar"]
EXPOSE 8081
