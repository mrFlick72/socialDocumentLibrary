FROM frolvlad/alpine-oraclejdk8:slim
ADD socialDocumentLibrarySearchBookService.jar /opt/socialDocumentLibrary/search-book-service/
EXPOSE 6060-6099
WORKDIR /opt/socialDocumentLibrary/search-book-service/
CMD ["java", "-Xms512m", "-Xmx1g", "-jar", "socialDocumentLibrarySearchBookService.jar"]