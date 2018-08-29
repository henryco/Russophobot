package net.tindersamurai.russophobot.bot.event.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class StartUpEvent extends ApplicationEvent {
	private @Getter boolean status;
	public StartUpEvent(boolean status) {
		super(status);
		this.status = status;
	}
}