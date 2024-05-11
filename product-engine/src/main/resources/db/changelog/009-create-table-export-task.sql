--liquibase formatted sql

--changeset adel:create-table-export-task failOnError=true
--comment: Create table export task
CREATE TABLE IF NOT EXISTS export_task
(
    id            BIGSERIAL,
    type          VARCHAR NOT NULL,
    status        VARCHAR NOT NULL,
    key           VARCHAR NOT NULL,
    payload       TEXT    NOT NULL,
    retries_count INTEGER   DEFAULT 0,
    updated_at    TIMESTAMP DEFAULT clock_timestamp(),
    created_at    TIMESTAMP DEFAULT clock_timestamp(),

    CONSTRAINT pk_export_task PRIMARY KEY (id),
    CONSTRAINT fk_export_task_type FOREIGN KEY (type) REFERENCES export_task_type (type),
    CONSTRAINT check_status CHECK (status IN ('NEW', 'PROCESSING', 'SUCCESS', 'ERROR'))
);