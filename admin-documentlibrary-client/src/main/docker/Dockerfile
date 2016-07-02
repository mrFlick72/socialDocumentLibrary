FROM frolvlad/alpine-oraclejdk8:slim
ADD adminDocumentLibraryClient.jar /opt/socialDocumentLibrary/adminUI/
EXPOSE 7070-7099
WORKDIR /opt/socialDocumentLibrary/adminUI/
CMD ["java", "-Xms512m", "-Xmx1g", "-jar", "adminDocumentLibraryClient.jar"]