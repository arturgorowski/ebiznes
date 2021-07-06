#FROM ubuntu:18.04
#
#ENV TZ=Europe/Warsaw
#RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#
#RUN apt update && apt install -y build-essential unzip vim git curl wget zip
#
#RUN apt-get update &&\
#	apt-get upgrade -y &&\
#    apt-get install -y  software-properties-common
#
## JS
#RUN curl -sL https://deb.nodesource.com/setup_12.x | bash -
#RUN apt-get install -y nodejs
#RUN npm install -g npm@latest
#RUN npm install -g @angular/cli
#
#EXPOSE 4200
#EXPOSE 9000
#
#
#RUN useradd -ms /bin/bash arturgorowski
#RUN adduser arturgorowski sudo
#
#USER arturgorowski
#WORKDIR /home/arturgorowski/
#
#RUN mkdir projekt
#WORKDIR /home/arturgorowski/projekt/angular-shop
#
#RUN npm install

### STAGE 1: Build ###
FROM node:12.7-alpine AS build
WORKDIR /usr/src/app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

### STAGE 2: Run ###
FROM nginx:1.17.1-alpine
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /usr/src/app/dist/angular-shop /usr/share/nginx/html
