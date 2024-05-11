--liquibase formatted sql

--changeset adel:create-table-export-task-type failOnError=true
--comment: Create table export task type
CREATE TABLE IF NOT EXISTS export_task_type
(
    type        VARCHAR,
    retry_count INTEGER NOT NULL,

    CONSTRAINT pk_export_task_type PRIMARY KEY (type)
);