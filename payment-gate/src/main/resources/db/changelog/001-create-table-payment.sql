--liquibase formatted sql

--changeset adel:create-table-payment failOnError=true
--comment: Create table payment
CREATE TABLE IF NOT EXISTS payment
(
    id                       UUID,
    client_email             VARCHAR(30) NOT NULL,
    agreement_number         UUID        NOT NULL,
    amount                   NUMERIC     NOT NULL,
    type                     VARCHAR(30) NOT NULL,
    status                   VARCHAR(30) NOT NULL,
    payment_date             TIMESTAMP   NOT NULL,
    count_of_status_check    INTEGER     NOT NULL,
    next_status_check_date   TIMESTAMP   NOT NULL,
    status_check_external_id UUID UNIQUE NOT NULL,
    status_update_date       TIMESTAMP,

    CONSTRAINT pk_payment PRIMARY KEY (id),
    CONSTRAINT check_type CHECK (type IN ('DISBURSEMENT', 'LOAN_PAYMENT')),
    CONSTRAINT check_status CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED'))
);