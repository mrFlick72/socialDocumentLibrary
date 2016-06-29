FROM frolvlad/alpine-oraclejdk8:slim
ADD socialDocumentLibrary.jar /opt/socialDocumentLibrary/clientUI/
EXPOSE 9090-9099
WORKDIR /opt/socialDocumentLibrary/clientUI/
CMD ["java", "-Xms512m", "-Xmx1g", "-jar", "socialDocumentLibrary.jar"]