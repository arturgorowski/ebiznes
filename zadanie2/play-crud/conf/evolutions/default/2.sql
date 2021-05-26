# --- !Ups

INSERT INTO "address" ("street", "number", "city", "postalCode", "voivodeship")
    VALUES("Głęboka", "4", "Tuchów", "33-170", "Małopolska");

INSERT INTO "category" ("name")
VALUES("laptopy");

INSERT INTO "product" ("name", "description", "price", "category")
VALUES("MacBook Pro 2021",
       "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam ac metus at ipsum placerat tincidunt at a lorem. Nulla cursus in tellus ac bibendum. Praesent sed ipsum lobortis, venenatis ex ac, luctus turpis. Etiam aliquam egestas risus ut tempor. Vivamus varius augue sit amet lacus ultricies porttitor.",
       3000,
       1
    );

# --- !Downs

DELETE FROM "address" WHERE street="Głęboka";
