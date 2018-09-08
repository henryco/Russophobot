# Russophobot [![Build Status](http://174.138.0.194:1997/buildStatus/icon?job=tinder-samurai/russophobot/master)](http://174.138.0.194:1997/job/tinder-samurai/job/russophobot/job/master/)
#### Pretty smart universal bot for Telegram based on Spring-Boot.

## List of available commads

| -- | -- |
| anon | Send anonymous message |
| subscribe | Start subscription process |
| unsubscribe | Yeah. unsubscribe |
| ping | Test bot status |
| token | Generate access token |
| mute | Mute/Unmute, use with replies! |
| sticker | Set start dialog sticker |
| notify | Send message to all mailers |
| list | List of all subscribers and mailers |

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
