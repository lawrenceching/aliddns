FROM openjdk:12

WORKDIR /usr/src/app

ADD target/aliddns-1.0.14-SNAPSHOT.jar /usr/src/app/aliddns-1.0.14-SNAPSHOT.jar

CMD [ "java", "-jar", "/usr/src/app/aliddns-1.0.14-SNAPSHOT.jar", \
"-d", \
"--accessKeyId", "${ACCESS_KEU_ID}", \
"--accessKeySecret", "${ACCESS_KEY_SECRET}", \
"--rr", "${RR}", \
"--type", "${TYPE}", \
"--domain", "${DOMAIN}", \
"-t", "${INTERVAL}"]
