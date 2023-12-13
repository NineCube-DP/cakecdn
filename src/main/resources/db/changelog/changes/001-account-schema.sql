CREATE SEQUENCE accounts_seq
INCREMENT 1
START 1;

CREATE TABLE accounts (
    id SERIAL,
    username varchar(255) NOT NULL,
    password varchar(255),
    PRIMARY KEY (id)
);