--liquibase formatted sql

--changeset adel:create-table-dead-letter failOnError=true
--comment: Create table dead_letter
CREATE TABLE IF NOT EXISTS dead_letter
(
    id          BIGSERIAL,
    key         VARCHAR NOT NULL,
    value       VARCHAR NOT NULL,
    topic       VARCHAR NOT NULL,
    received_at TIMESTAMP,

    CONSTRAINT pk_dead_letter PRIMARY KEY (id)
);