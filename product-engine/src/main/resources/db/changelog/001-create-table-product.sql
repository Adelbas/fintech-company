--liquibase formatted sql

--changeset adel:create-table-product failOnError=true
--comment: Create table product
CREATE TABLE IF NOT EXISTS product
(
    code                   VARCHAR(30),
    loan_term_min          INT     NOT NULL,
    loan_term_max          INT     NOT NULL,
    principal_amount_min   NUMERIC NOT NULL,
    principal_amount_max   NUMERIC NOT NULL,
    interest_min           NUMERIC     NOT NULL,
    interest_max           NUMERIC     NOT NULL,
    origination_amount_min NUMERIC NOT NULL,
    origination_amount_max NUMERIC NOT NULL,

    CONSTRAINT pk_product PRIMARY KEY (code),
    CONSTRAINT valid_loan_term CHECK (loan_term_min > 0 AND loan_term_max >= loan_term_min),
    CONSTRAINT valid_principal_amount CHECK (principal_amount_min > 0 AND principal_amount_max >= principal_amount_min),
    CONSTRAINT valid_interest CHECK (interest_min > 0 AND interest_max >= interest_min),
    CONSTRAINT valid_origination_amount CHECK (origination_amount_min > 0 AND
                                               origination_amount_max >= origination_amount_min)
);