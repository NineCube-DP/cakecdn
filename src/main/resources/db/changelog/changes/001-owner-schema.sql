CREATE TABLE Owners (
    id SERIAL,
    username varchar(255) NOT NULL,
    password varchar(255),
    PRIMARY KEY (id)
);