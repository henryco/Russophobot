#!/bin/bash

# $1 - token
# $2 - name
# $3 - url
# $4 - email
# $5 - question
# $6 - answer

echo "" > src/main/resources/bot.properties

echo "token=$1" >> src/main/resources/bot.properties
echo "name=$2" >> src/main/resources/bot.properties

echo "confirm.address=$3/subscription/confirm/" >> src/main/resources/bot.properties
echo "confirm.email=$4" >> src/main/resources/bot.properties

echo "redis.host=localhost" >> src/main/resources/bot.properties
echo "redis.port=6379" >> src/main/resources/bot.properties

echo "secret.question=$5" >> src/main/resources/bot.properties
echo "secret.answer=$6" >> src/main/resources/bot.properties
