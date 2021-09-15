#!/bin/bash
set -e
export PGPASSWORD=postgres;
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE DATABASE app_db;
  COMMIT;
EOSQL