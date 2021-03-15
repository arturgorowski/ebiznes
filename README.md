# ebiznes

### Zadanie 1. Docker

Należy stworzyć obraz, który zawiera Javę w wersji 8, Scalę w wersji
2.12, najnowszą wersję sbt oraz npm. Dockerfile z obrazem należy
umieścić w repozytorium git, a link do docker huba z obrazem należy
dodać do README.md w repozytorium git. Wersje zainstalowanych paczek
można sprawdzić uruchamiając kontener z odpowiednią komendą, np.:

$ docker run -it kprzystalski/ebiznes:latest java --version

Proszę również udostępnić porty (EXPOSE) dla aplikacji w React'cie
oraz aplikacji w Play. Dodatkowo proszę przeznaczyć jeden folder do
wymiany danych pomiędzy hostem a kontenerem (VOLUME).