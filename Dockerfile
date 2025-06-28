FROM azul/zulu-openjdk:17
COPY ./build/libs/*SNAPSHOT.jar production.jar
ENTRYPOINT ["java", "-jar", "production.jar"]