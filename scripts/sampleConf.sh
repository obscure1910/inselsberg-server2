#!/usr/bin/env bash

echo "Get sample conf from Postgresql"
docker run -i --name inselsberg-postgres --rm postgres:13.1-alpine cat ./usr/local/share/postgresql/postgresql.conf.sample > defaultSamplePostgres.conf