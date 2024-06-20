FROM eclipse-temurin:21-jre

#set current path
WORKDIR /app

#copy jar file to container
COPY *.jar .

#set container port
EXPOSE 8888

#run image
ENTRYPOINT ["java", "-jar", "*.jar"]

#ENTRYPOINT ["java"]
#CMD ["-jar", "/event-master-1.0.0.jar"]

