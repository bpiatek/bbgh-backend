FROM openjdk:11.0.7-jdk

# Install selected extensions and other stuff
RUN apt-get update \
   && apt-get install curl -y \
   && curl -sL https://deb.nodesource.com/setup_13.x | bash - \
   && apt-get install -y nodejs

RUN apt-get install maven -y

ENTRYPOINT /bin/bash