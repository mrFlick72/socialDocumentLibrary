FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp/socialDocumentLibrary/clientUI/
ADD socialDocumentLibrary.jar app.jar
EXPOSE 9090-9099
ENTRYPOINT ["java","-jar","/app.jar"]