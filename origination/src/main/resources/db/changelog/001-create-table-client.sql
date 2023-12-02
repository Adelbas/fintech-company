--liquibase formatted sql

--changeset adel:create-table-client failOnError=true
--comment: Create table client
CREATE TABLE IF NOT EXISTS client
(
    client_id  UUID,
    first_name VARCHAR(30)        NOT NULL,
    last_name  VARCHAR(30)        NOT NULL,
    email      VARCHAR(30) UNIQUE NOT NULL,
    salary     NUMERIC            NOT NULL,

    CONSTRAINT pk_client PRIMARY KEY (client_id),
    CONSTRAINT valid_salary CHECK (salary > 0)
);