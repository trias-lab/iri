FROM iotacafe/maven:3.5.4.oracle8u181.1.webupd8.1.1-1
RUN mkdir /oauth_web
COPY ./ /oauth_web
WORKDIR /oauth_web/trias-oauth
RUN mvn clean package
RUN mkdir -p /opt/trias/oauth/client  /opt/trias/oauth/server
RUN mv /oauth_web/trias-oauth/oauth-server/target/oauth-server-1.0-SNAPSHOT.jar /opt/trias/oauth/server/oauth-server-1.0-SNAPSHOT.jar
RUN mv /oauth_web/trias-oauth/oauth-resource/target/oauth-resource-1.0-SNAPSHOT.jar /opt/trias/oauth/client/oauth-resource-1.0-SNAPSHOT.jar
RUN mv /oauth_web/trias-oauth/oauth-server/src/main/resources/application.yml /opt/trias/oauth/server/application.yml
RUN mv /oauth_web/trias-oauth/oauth-server/src/main/resources/logback-spring.xml /opt/trias/oauth/server/logback-spring.xml
RUN mv /oauth_web/trias-oauth/oauth-resource/src/main/resources/application.yml /opt/trias/oauth/client/application.yml
RUN mv /oauth_web/trias-oauth/oauth-resource/src/main/resources/logback-spring.xml /opt/trias/oauth/client/logback-spring.xml
CMD ["sh", "/oauth_web/entrypoint.sh"]
