--liquibase formatted sql

--changeset adel:create-table-agreement failOnError=true
--comment: Create table agreement
CREATE TABLE IF NOT EXISTS agreement
(
    agreement_number          UUID,
    client_id                 UUID NOT NULL,
    is_disbursement_completed BOOLEAN,

    CONSTRAINT pk_agreement PRIMARY KEY (agreement_number),
    CONSTRAINT fk_agreement_client FOREIGN KEY (client_id) REFERENCES client (client_id)
);