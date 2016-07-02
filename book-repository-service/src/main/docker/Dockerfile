FROM frolvlad/alpine-oraclejdk8:slim

VOLUME ["/opt/socialDocumentLibrary/book-repository-service/","/opt/socialDocumentLibrary/book-repository-service/repository/"]
ADD bookRepositoryService.jar /opt/socialDocumentLibrary/book-repository-service/

WORKDIR /opt/socialDocumentLibrary/book-repository-service/

EXPOSE 8080-8099

CMD ["java", "-Xms512m", "-Xmx1g", "-jar", "bookRepositoryService.jar"]