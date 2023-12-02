--liquibase formatted sql

--changeset adel:create-table-application failOnError=true
--comment: Create table application
CREATE TABLE IF NOT EXISTS application
(
    application_id                UUID,
    client_id                     UUID        NOT NULL,
    requested_disbursement_amount NUMERIC     NOT NULL,
    status                        VARCHAR(30) NOT NULL,

    CONSTRAINT pk_application PRIMARY KEY (application_id),
    CONSTRAINT fk_application_client FOREIGN KEY (client_id) REFERENCES client (client_id),
    CONSTRAINT valid_status CHECK (status IN ('NEW', 'SCORING', 'ACCEPTED', 'ACTIVE', 'CLOSED'))
);