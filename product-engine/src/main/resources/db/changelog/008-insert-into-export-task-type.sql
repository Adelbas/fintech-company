--liquibase formatted sql

--changeset adel:insert-into-export-task-type failOnError=true
--comment: Add agreement_status_update type
INSERT INTO export_task_type (type, retry_count)
VALUES ('AGREEMENT_STATUS_UPDATE', 3);