FROM openjdk:12

WORKDIR /usr/src/app

ADD target/aliddns-1.0.14-SNAPSHOT.jar /usr/src/app/aliddns-1.0.14-SNAPSHOT.jar

CMD [ "java", "-jar", "/usr/src/app/aliddns-1.0.14-SNAPSHOT.jar" ]
