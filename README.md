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

### Zadanie 2. Routing
Należy stworzyć dziesięć kontrolerów oraz odpowiadającą metodom kontrolerów tablicę routingu. Należy przyjąć jako przykład sklep oraz odpowiadającą sklepu 10 kontrolerów, w każdym kontrolerze powinny być metody CRUD (Create Read Update Delete). Metody powinny być wydmuszką (mock), czyli bez istotnej implementacji.

### Zadanie 3. Modele
Należy stworzyć dziesięć modeli, gdzie każdy model powinnien mieć implementację CRUD (Create Read Update Delete).

### Zadanie 4. Kontrolery REST
Należy stworzyć wykorzystać stworzone wcześniej modele i zaimplementować metody we wcześniej stworzonych kontrolerach z zadania drugiego. Dodatkowo każda metoda CRUD powinna mieć odwołanie do widoku, np. formularza czy listy, poprawnioną mapę routingu jeżeli trzeba. Należy też dodać metody odpowiedzialne za część REST aplikacji, czyli implementację metod CRUD, które zwrócą dane z modelu w formie JSON.

### Zadanie 5. React
Kolejne zadanie dotyczy biblioteki React.js. Należy stworzyć komponenty odpowiadające funkcjonalności użytkownika, tzn. nie musi być 1:1 do kontrolerów w Play. Należy stworzyć minimum 8 komponentów, a ewentualne zapytania do serwera należy wydzielić do zewnętrznej klasy i/lub serwisu.

### Zdanie 6. React hooks
Należy wykorzystać React hooks, aby udostępnić dane np. produkty pomiędzy komponentami. Należy wykorzystać je tam, gdzie dane z serwera sa wykorzystywane. Alternatywa: mobx, redux.

### Zadanie 7. Chmura
Proszę wykorzystać Azure i zrobić deployment aplikacji za pomoca GitHub Actions. Należy zrobić deployment aplikacji serwerowej i klienckiej (frotend).

### Zadanie 8. Oauth2
Zaimplementować oauth2 po stronie serwera z przekazaniem tokena (nowego) do Reacta. Należy dodać jeden serwis, np. Google lub Facebook.
https://www.silhouette.rocks/docs/examples

### Zadanie 9. Sonar
Należy usunać wszystkie code smells, bugi, oraz security hotspots w kodzie.
