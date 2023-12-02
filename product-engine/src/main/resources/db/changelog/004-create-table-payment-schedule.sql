--liquibase formatted sql

--changeset adel:create-table-payment-schedule failOnError=true
--comment: Create table payment_schedule
CREATE TABLE IF NOT EXISTS payment_schedule
(
    payment_schedule_id BIGSERIAL,
    agreement_number    UUID NOT NULL,
    version             INT  NOT NULL,

    CONSTRAINT pk_payment_schedule PRIMARY KEY (payment_schedule_id),
    CONSTRAINT fk_payment_schedule_agreement FOREIGN KEY (agreement_number) REFERENCES agreement (agreement_number)
);