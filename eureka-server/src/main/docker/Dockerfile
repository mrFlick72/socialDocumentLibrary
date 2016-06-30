FROM frolvlad/alpine-oraclejdk8:slim
ADD socialDocumentLibraryEurekaServer.jar /opt/socialDocumentLibrary/eureka-server/
EXPOSE 8761
WORKDIR /opt/socialDocumentLibrary/eureka-server/
CMD ["java", "-Xms512m", "-Xmx1g", "-jar", "socialDocumentLibraryEurekaServer.jar"]