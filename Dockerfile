FROM openjdk:8
ADD target/emp-mgt.jar emp-mgt.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","emp-mgt.jar"] 