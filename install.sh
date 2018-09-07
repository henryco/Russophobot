#!/bin/bash


# save current path and move
curr_dir=$(pwd)


# grant permissions
sudo chmod a+x *.sh


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


# install java 8
sudo ./install-java.sh
 
	
# install postgres
sudo ./install-postgres.sh


# install database
sudo ./gen-sql.sh $user $pass


# install properties
sudo ./gen-bot.sh $bot_token $bot_name $server_url $email
sudo ./gen-app.sh $app_email $app_email_pass $user $pass


# install redis
sudo ./install-redis.sh


# move to project root
cd $curr_dir


# build application
./gradlew build --stacktrace
(pkill -f gradle) || true


# deploy apllication
cp build/libs/russophobot-0.0.1-SNAPSHOT.jar "$destination/"russophobot.jar
cp install/start.sh "$destination/"start.sh
cd $destination


echo "Application installed"
