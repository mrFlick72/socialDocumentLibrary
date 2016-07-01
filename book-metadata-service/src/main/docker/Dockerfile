FROM frolvlad/alpine-oraclejdk8:slim
ADD socialDocumentLibrarySocialMetadataService.jar /opt/socialDocumentLibrary/book-social-metadata-service/
EXPOSE 6060-6099
WORKDIR /opt/socialDocumentLibrary/book-social-metadata-service/
CMD ["java", "-Xms512m", "-Xmx1g", "-jar", "socialDocumentLibrarySocialMetadataService.jar"]