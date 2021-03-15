# Pull base image
FROM ubuntu:latest
MAINTAINER Artur Górowski <art.gorowski@gmail.com>

# Env variables
ENV SCALA_VERSION 2.12.13
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV JRE_HOME=/usr/lib/jvm/java-8-openjdk-amd64/jre

# Install Java.
RUN apt-get update && \
    apt-get install -y software-properties-common && \
    add-apt-repository -y ppa:openjdk-r/ppa && \
    apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y openjdk-8-jre && \
    update-alternatives --config java && \
    update-alternatives --config javac

#Install Scala
RUN apt-get install -y wget && \
    wget www.scala-lang.org/files/archive/scala-$SCALA_VERSION.deb && \
    dpkg -i scala-$SCALA_VERSION.deb

#Install SBT
RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list && \
    apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823 && \
    apt-get update && \
    apt-get install -y sbt

RUN apt-get update && \
    apt-get install -y curl && \
    curl -sL https://deb.nodesource.com/setup_15.x | bash - && \
    apt-get update && \
    apt-get install -y nodejs
