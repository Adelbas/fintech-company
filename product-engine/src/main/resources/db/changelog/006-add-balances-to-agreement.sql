--liquibase formatted sql

--changeset adel:add-balances-to-agreement failOnError=true
--comment: Create balance columns
ALTER TABLE agreement
ADD account_balance NUMERIC,
ALTER account_balance SET DEFAULT 0,
ALTER account_balance SET NOT NULL,
ADD CONSTRAINT check_account_balance CHECK (account_balance >= 0),
ADD overdue_balance NUMERIC,
ALTER overdue_balance SET DEFAULT 0,
ALTER overdue_balance SET NOT NULL,
ADD CONSTRAINT check_overdue_balance CHECK (overdue_balance >= 0);

