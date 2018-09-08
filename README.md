# Russophobot [![Build Status](http://174.138.0.194:1997/buildStatus/icon?job=tinder-samurai/russophobot/master)](http://174.138.0.194:1997/job/tinder-samurai/job/russophobot/job/master/) [![GitHub license](https://img.shields.io/badge/license-MIT-red.svg)](https://raw.githubusercontent.com/henryco/Russophobot/master/LICENSE)
#### Pretty smart universal bot for Telegram based on Spring-Boot.
&nbsp;

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

&nbsp;

## Rest API
Yeah, bot has a small rest api, you can analyze package
`net.tindersamurai.russophobot.mvc.controller` for more info.

&nbsp;

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
