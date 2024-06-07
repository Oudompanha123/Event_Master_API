FROM eclipse-temurin:21-jre

#set current path
WORKDIR /app

#copy jar file to container
COPY */target/*.jar .
COPY . .

#set container port
EXPOSE 8080

#run image
ENTRYPOINT ["java", "-jar", "event-master-1.0.0.jar"]

#ENTRYPOINT ["java"]
#CMD ["-jar", "/event-master-1.0.0.jar"]

