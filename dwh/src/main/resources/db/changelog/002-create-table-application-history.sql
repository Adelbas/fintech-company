--liquibase formatted sql

--changeset adel:create-table-application-history failOnError=true
--comment: Create table application history and add partitions
CREATE TABLE IF NOT EXISTS application_history
(
    id              BIGSERIAL,
    application_id  UUID    NOT NULL,
    status          VARCHAR NOT NULL,
    business_date   DATE    NOT NULL,
    idempotency_key UUID    NOT NULL,

    CONSTRAINT pk_application_history PRIMARY KEY (id, business_date),
    CONSTRAINT application_history_unique_key UNIQUE (business_date, idempotency_key),
    CONSTRAINT check_status_application CHECK (status IN ('NEW', 'SCORING', 'ACCEPTED', 'ACTIVE', 'CLOSED'))
) PARTITION BY RANGE (business_date);


SELECT ${DB_PARTMAN_SCHEMA}.create_parent(
               p_parent_table := '${DB_SCHEMA}.application_history',
               p_control := 'business_date',
               p_type := 'native',
               p_interval := 'P1D',
               p_start_partition := '2024-01-01 00:00:00'::text,
               p_premake := '30'
           );


UPDATE ${DB_PARTMAN_SCHEMA}.part_config
SET infinite_time_partitions = true
WHERE parent_table IN ('application_history')