CREATE TABLE Product (
    id      NUMBER(11,0)  PRIMARY KEY,
    name    VARCHAR(50)  NOT NULL,
    price   NUMBER(7,2)  NOT NULL,
    description  VARCHAR(120) NULL
);