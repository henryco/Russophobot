#!/bin/bash

# $1 - username
# $2 - password

echo "CREATE DATABASE russophobot_database;" > install-db.sql
echo "CREATE USER $1 WITH password '$2';" >> install-db.sql
echo "GRANT ALL ON DATABASE russophobot_database to $1;" >> install-db.sql
