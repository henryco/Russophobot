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

echo "Input server url (e.g 127.0.0.1:1970)..."
read server_url

echo "Input server port (e.g 1970)..."
read server_port

echo "Input email (should be valid and actual!)..."
read email

echo "Input application email (might be the same)..."
read app_email	
	
echo "Input application email password..."
read app_email_pass	

echo "Input mail host (type smtp.gmail.com for Gmail)..."
read app_email_host

echo "Input email port (type 587 for Gmail)..."
read app_email_port

echo "Input secret question..."
read question

echo "Input secret answer..."
read answer
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
sudo -u postgres psql -a -f install-db.sql


# install redis
sudo ./install-redis_u18.sh


# move to project root
cd $curr_dir


# install properties
sudo install/./gen-bot.sh $bot_token $bot_name $server_url $email $question $answer
sudo install/./gen-app.sh $app_email $app_email_pass $user $pass $server_port $app_email_host $app_email_port


# build application
./gradlew build -x test --stacktrace
(pkill -f gradle) || true


# deploy apllication
mkdir $HOME/Russophobot/
cp build/libs/russophobot-0.0.1-SNAPSHOT.jar "$HOME/Russophobot/"russophobot.jar
cp install/start.sh "$HOME/Russophobot/"start.sh
cd $HOME/Russophobot


echo "Application installed"
