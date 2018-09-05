package net.tindersamurai.russophobot.bot.event.events;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

@Slf4j
public final class MessageEvent extends ApplicationEvent {
	private @Getter final String message;
	public MessageEvent(String message) {
		super(message);
		this.message = message;
	}
}