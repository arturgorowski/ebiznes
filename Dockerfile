FROM ubuntu:18.04

ENV TZ=Europe/Warsaw
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt update && apt install -y build-essential unzip vim git curl wget zip

RUN apt-get update
RUN apt-get -y install software-properties-common

RUN apt install -y openjdk-8-jdk
RUN apt install -y openjdk-8-jre
RUN update-alternatives --config java
RUN update-alternatives --config javac

ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV JRE_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | tee /etc/apt/sources.list.d/sbt_old.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | apt-key add
RUN apt-get update
RUN apt-get install -y sbt=1.5.2

RUN wget www.scala-lang.org/files/archive/scala-2.12.13.deb
RUN dpkg -i scala-2.12.13.deb

EXPOSE 9000

RUN useradd -ms /bin/bash arturgorowski
RUN adduser arturgorowski sudo

USER arturgorowski
WORKDIR /home/arturgorowski/

RUN mkdir ebiznes
WORKDIR /home/arturgorowski/ebiznes
COPY . .
WORKDIR /home/arturgorowski/ebiznes/play-crud

CMD sbt run
