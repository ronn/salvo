FROM ubuntu:18.04
RUN apt update
RUN apt install -y openjdk-8-jre
RUN mkdir app
COPY build/libs/salvo-0.0.1-SNAPSHOT.jar /opt/salvo/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/salvo/salvo-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080