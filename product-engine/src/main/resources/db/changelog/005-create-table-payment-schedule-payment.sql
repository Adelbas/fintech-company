--liquibase formatted sql

--changeset adel:create-table-payment-schedule-payment failOnError=true
--comment: Create table payment_schedule_payment
CREATE TABLE IF NOT EXISTS payment_schedule_payment
(
    payment_id          BIGSERIAL,
    payment_schedule_id BIGSERIAL   NOT NULL,
    status              VARCHAR(30) NOT NULL,
    payment_date        TIMESTAMP   NOT NULL,
    period_payment      NUMERIC     NOT NULL,
    interest_payment    NUMERIC     NOT NULL,
    principal_payment   NUMERIC     NOT NULL,
    period_number       INT         NOT NULL,

    CONSTRAINT pk_payment_schedule_payment PRIMARY KEY (payment_id),
    CONSTRAINT fk_payment_schedule_payment_payment_schedule FOREIGN KEY (payment_schedule_id) REFERENCES payment_schedule (payment_schedule_id),
    CONSTRAINT check_status CHECK (status IN ('PAID', 'OVERDUE', 'FUTURE'))
);