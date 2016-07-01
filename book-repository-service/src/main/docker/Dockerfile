FROM frolvlad/alpine-oraclejdk8:slim
ADD bookRepositoryService.jar /opt/socialDocumentLibrary/book-repository-service/
EXPOSE 8080-8099
WORKDIR /opt/socialDocumentLibrary/book-repository-service/
CMD ["java", "-Xms512m", "-Xmx1g", "-jar", "bookRepositoryService.jar"]