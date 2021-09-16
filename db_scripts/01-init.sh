#!/bin/bash
set -e
export PGPASSWORD=postgres;
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE DATABASE app_db;
  \connect app_db postgres
  BEGIN;
    CREATE SCHEMA customers;
    CREATE SCHEMA sales;
    CREATE SCHEMA salesmen;
  COMMIT;
EOSQL