#!/bin/bash

sudo apt-get update
sudo apt-get upgrade

sudo apt-get install redis-server
sudo systemctl enable redis-server.service
