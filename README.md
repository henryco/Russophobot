# **Russophobot** 
[![Sonar Cloud](https://sonarcloud.io/api/project_badges/measure?project=russophobot&metric=alert_status)](https://sonarcloud.io/dashboard?id=russophobot) [![Build Status](http://174.138.0.194:1997/buildStatus/icon?job=tinder-samurai/russophobot/master)](http://174.138.0.194:1997/job/tinder-samurai/job/russophobot/job/master/)  [![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://bitbucket.org/tinder-samurai/russophobot/raw/23b0da328df046d612f69beda3fb27036696d172/LICENSE)

#### Pretty smart universal Telegram bot based on Spring-Boot.

## List of available commads

| Command | Description |
| --- | --- |
| **anon** | Send anonymous message |
| **subscribe** | Start subscription process |
| **unsubscribe** | Yeah. unsubscribe |
| **ping** | Test bot status |
| **token** | Generate access token |
| **mute** | Mute/Unmute, use it with replies! |
| **sticker** | Set start dialog sticker |
| **notify** | Send message to all mailers |
| **list** | List of all subscribers and mailers |

Subscibers also can reply to non-anonymous messages.

## Rest API
Yeah, bot has a small rest api, you can analyze package
`net.tindersamurai.russophobot.mvc.controller` for more info.

## Installation
* Clone or download repo
* Move to project root
* Run `install_u16.sh` on Ubuntu16
* Run `install_u18.sh` on Ubuntu18
* Move to `$HOME/Russophobot`
* Run `start.sh`

### Note
I've tested installation script only on Ubuntu 16,
so there are no guarantee that script for version 18 will work properly.
