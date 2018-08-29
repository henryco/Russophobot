package net.tindersamurai.russophobot.bot.event.events;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Value
public class MessageEvent {
	private String message;
}