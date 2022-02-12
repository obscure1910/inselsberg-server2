#!/usr/bin/env bash

echo "Start Postgresql"
COMMAND="docker run -d --name inselsberg-postgres -p 5432:5432 -e POSTGRES_DB="inselsberg" -e POSTGRES_PASSWORD="postgres" -e POSTGRES_USER="postgres" postgres:13.1-alpine -c max_connections=50"
echo "$COMMAND"
$COMMAND