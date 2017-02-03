# Tasks schema

# --- !Ups

CREATE TABLE URLS (
    id int NOT NULL AUTO_INCREMENT,
    hash_code int not null,
    url varchar(1000) NOT NULL,
    PRIMARY KEY (id)
);

CREATE INDEX URLS_HASH_INDEX ON URLS (hash_code);

# --- !Downs
DROP TABLE URLS;
DROP INDEX URLS_HASH_INDEX;