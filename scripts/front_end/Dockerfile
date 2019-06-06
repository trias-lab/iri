FROM iotacafe/maven:3.5.4.oracle8u181.1.webupd8.1.1-1
RUN mkdir /oauth_web
COPY ./ /oauth_web
WORKDIR /oauth_web/trias-oauth
RUN mvn clean package
RUN add-apt-repository ppa:longsleep/golang-backports -y
RUN apt-get update && apt-get install golang-go -y \
	&& apt-get clean \
	&& rm -rf /var/lib/apt/lists/* 
RUN curl -sL https://deb.nodesource.com/setup_10.x -o nodesource_setup.sh \
    && bash nodesource_setup.sh \
	&& apt install -y nodejs 
RUN mkdir -p /opt/trias/oauth/client  /opt/trias/oauth/server \
    && mv /oauth_web/trias-oauth/oauth-server/target/oauth-server-1.0-SNAPSHOT.jar /opt/trias/oauth/server/oauth-server-1.0-SNAPSHOT.jar \
    && mv /oauth_web/trias-oauth/oauth-resource/target/oauth-resource-1.0-SNAPSHOT.jar /opt/trias/oauth/client/oauth-resource-1.0-SNAPSHOT.jar \
    && mv /oauth_web/trias-oauth/oauth-server/src/main/resources/application.yml /opt/trias/oauth/server/application.yml \
    && mv /oauth_web/trias-oauth/oauth-server/src/main/resources/logback-spring.xml /opt/trias/oauth/server/logback-spring.xml \
    && mv /oauth_web/trias-oauth/oauth-resource/src/main/resources/application.yml /opt/trias/oauth/client/application.yml  \
    && mv /oauth_web/trias-oauth/oauth-resource/src/main/resources/logback-spring.xml /opt/trias/oauth/client/logback-spring.xml
WORKDIR /oauth_web/web
RUN npm install --unsafe-perm=true \
    && npm run build  \
    && mkdir -p /var/www/html/trias-dag/
WORKDIR /oauth_web/web/dist/
RUN cp  -r index.html static /var/www/html/trias-dag/
WORKDIR /oauth_web/server
RUN go get -u github.com/triasteam/noderank
CMD ["sh", "/oauth_web/entrypoint.sh"]
