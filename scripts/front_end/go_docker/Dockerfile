FROM golang:1.12.7
RUN mkdir /gocode
COPY server/  /gocode
COPY go_docker/ /gocode/go_docker/
WORKDIR /gocode
ENV HOST_IP localhost
CMD ["sh","/gocode/go_docker/entrypoint.sh"]
