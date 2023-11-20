--liquibase formatted sql

--changeset adel:insert-into-product failOnError=true
--comment: Add CashLoan v1.0
INSERT INTO product (code, loan_term_min, loan_term_max, principal_amount_min, principal_amount_max,
                     interest_min, interest_max, origination_amount_min, origination_amount_max)
VALUES ('CL_1.0', 3, 24, 50000, 500000, 8, 15, 2000, 10000);