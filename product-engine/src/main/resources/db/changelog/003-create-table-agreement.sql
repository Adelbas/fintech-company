--liquibase formatted sql

--changeset adel:create-table-agreement failOnError=true
--comment: Create table agreement
CREATE TABLE IF NOT EXISTS agreement
(
    agreement_number   UUID DEFAULT gen_random_uuid(),
    client_id          UUID          NOT NULL,
    product_code       VARCHAR(30)   NOT NULL,
    loan_term          INT           NOT NULL,
    principal_amount   NUMERIC       NOT NULL,
    origination_amount NUMERIC       NOT NULL,
    interest           NUMERIC           NOT NULL,
    status             VARCHAR(30)   NOT NULL,
    disbursement_date  TIMESTAMP,
    next_payment_date  TIMESTAMP,

    CONSTRAINT pk_agreement PRIMARY KEY (agreement_number),
    CONSTRAINT fk_agreement_product FOREIGN KEY (product_code) REFERENCES product (code),
    CONSTRAINT check_status CHECK (status IN ('NEW', 'ACTIVE', 'CLOSED'))
);
