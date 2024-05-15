psql -U postgres << EOF
\c fintech_dwh;

CREATE SCHEMA partman;
CREATE EXTENSION pg_partman SCHEMA partman;
GRANT ALL ON SCHEMA partman TO postgres;
GRANT ALL ON ALL TABLES IN SCHEMA partman TO postgres;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA partman TO postgres;
GRANT EXECUTE ON ALL PROCEDURES IN SCHEMA partman TO postgres;

EOF