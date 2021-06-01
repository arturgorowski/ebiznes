FROM ubuntu:18.04

ENV TZ=Europe/Warsaw
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone 

RUN apt update && apt install -y build-essential unzip vim git curl wget zip

RUN apt-get update &&\
	apt-get upgrade -y &&\
    apt-get install -y  software-properties-common

# JS
RUN curl -sL https://deb.nodesource.com/setup_12.x | bash -
RUN apt-get install -y nodejs
RUN npm install -g npm@latest
RUN npm install -g @angular/cli

EXPOSE 8080
EXPOSE 4200
EXPOSE 9000


RUN useradd -ms /bin/bash arturgorowski
RUN adduser arturgorowski sudo

USER arturgorowski
WORKDIR /home/arturgorowski/
RUN curl -s "https://get.sdkman.io" | bash
RUN chmod a+x "/home/arturgorowski/.sdkman/bin/sdkman-init.sh"
RUN bash -c "source /home/arturgorowski/.sdkman/bin/sdkman-init.sh && sdk install java 8.0.272.hs-adpt"
RUN bash -c "source /home/arturgorowski/.sdkman/bin/sdkman-init.sh && sdk install sbt 1.5.1"
RUN bash -c "source /home/arturgorowski/.sdkman/bin/sdkman-init.sh && sdk install scala 2.12.13"

RUN mkdir projekt
WORKDIR /home/arturgorowski/projekt/


#RUN bash -c "source /home/arturgorowski/.sdkman/bin/sdkman-init.sh && sbt new playframework/play-scala-seed.g8"

