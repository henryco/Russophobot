#!/bin/bash


# save current path and move
curr_dir=$(pwd)


# ====================== input =======================
echo "Input db username..."
read user

echo "Input db password..."
read pass

echo "Input bot name..."
read bot_name

echo "Input bot token..."
read bot_token

echo "Input server url..."
read server_url

echo "Input email (should be valid and actual!)..."
read email

echo "Input application email (might be the same)..."
read app_email	
	
echo "Input application email password..."
read app_email_pass	

echo "Input output destination..."
read destination
# ====================== input =======================
	
	
# move to install folder	
cd install	


# grant permissions
sudo chmod a+x *.sh


# install java 8
sudo ./install-java.sh
 
	
# install postgres
sudo ./install-postgres.sh


# install database
sudo ./gen-sql.sh $user $pass
sudo psql -U $user -a -f install-db.sql


# install redis
sudo ./install-redis.sh


# move to project root
cd $curr_dir


# install properties
sudo install/./gen-bot.sh $bot_token $bot_name $server_url $email
sudo install/./gen-app.sh $app_email $app_email_pass $user $pass


# build application
./gradlew build -x test --stacktrace
(pkill -f gradle) || true


# deploy apllication
cp build/libs/russophobot-0.0.1-SNAPSHOT.jar "$destination/"russophobot.jar
cp install/start.sh "$destination/"start.sh
cd $destination


echo "Application installed"
