--- !Ups

CREATE TABLE "address"
(
    "id"          INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "street"      VARCHAR NOT NULL,
    "number"      INTEGER NOT NULL,
    "city"        VARCHAR NOT NULL,
    "postalCode"  VARCHAR NOT NULL,
    "voivodeship" VARCHAR NOT NULL
);

CREATE TABLE "cart"
(
    "id"                 INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
    "customer"           INTEGER  NOT NULL,
    "productsQuantity"   INTEGER  NOT NULL,
    "totalProductsPrice" INTEGER  NOT NULL,
    "coupon"             INTEGER  NOT NULL,
    "createdAt"          DATETIME NOT NULL,
    FOREIGN KEY (coupon) references coupon (id),
    FOREIGN KEY (customer) references customer (id)
);

CREATE TABLE "cart_item"
(
    "id"              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "cart"            INTEGER NOT NULL,
    "product"         INTEGER NOT NULL,
    "productQuantity" INTEGER NOT NULL,
    FOREIGN KEY (cart) references cart (id),
    FOREIGN KEY (product) references product (id)
);

CREATE TABLE "category"
(
    "id"   INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name" VARCHAR NOT NULL
);

CREATE TABLE "coupon"
(
    "id"         INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
    "code"       INTEGER  NOT NULL,
    "couponType" VARCHAR  NOT NULL,
    "discount"   INTEGER  NOT NULL,
    "isActive"   INTEGER  NOT NULL,
    "createdAt"  DATETIME NOT NULL,
    "usedAt"     DATETIME NOT NULL
);

CREATE TABLE "customer"
(
    "id"        INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
    "username"  VARCHAR  NOT NULL,
    "firstName" VARCHAR  NOT NULL,
    "lastName"  VARCHAR  NOT NULL,
    "password"  VARCHAR  NOT NULL,
    "createdAt" DATETIME NOT NULL,
    "address"   INTEGER  NOT NULL,
    FOREIGN KEY (address) references address (id)
);

CREATE TABLE "order"
(
    "id"              INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,
    "createdAt"       DATETIME NOT NULL,
    "customer"        INTEGER  NOT NULL,
    "isPaid"          INTEGER  NOT NULL,
    "paidAt"          VARCHAR  NOT NULL,
    "totalOrderValue" DECIMAL  NOT NULL,
    "coupon"          INTEGER  NOT NULL,
    FOREIGN KEY (customer) references customer (id),
    FOREIGN KEY (coupon) references coupon (id)
);

CREATE TABLE "order_item"
(
    "id"      INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "order"   INTEGER NOT NULL,
    "product" INTEGER NOT NULL,
    FOREIGN KEY ('order') references 'order' (id),
    FOREIGN KEY (product) references product (id)
);

CREATE TABLE "product"
(
    "id"       INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "name"     VARCHAR NOT NULL,
    "description"     TEXT    NOT NULL,
    "price"    DECIMAL NOT NULL,
    "category" INTEGER NOT NULL,
    FOREIGN KEY (category) references category (id)
);

CREATE TABLE "review"
(
    "id"       INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    "product"  INTEGER NOT NULL,
    "customer" INTEGER NOT NULL,
    "content"  VARCHAR NOT NULL,
    "score"    FLOAT   NOT NULL,
    FOREIGN KEY (product) references product (id),
    FOREIGN KEY (customer) references customer (id)
);


--- !Downs

DROP TABLE "address";
DROP TABLE "cart";
DROP TABLE "cart_item";
DROP TABLE "category";
DROP TABLE "coupon";
DROP TABLE "customer";
DROP TABLE "order";
DROP TABLE "order_item";
DROP TABLE "product";
DROP TABLE "review";