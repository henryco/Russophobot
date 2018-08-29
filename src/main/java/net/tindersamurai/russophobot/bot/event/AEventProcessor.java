package net.tindersamurai.russophobot.bot.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public abstract class AEventProcessor {

	@Autowired private AbsSender absSender;

	protected AbsSender getAbsSender() {
		return absSender;
	}

	protected boolean sendMessage(BotApiMethod<?> message) {
		try {
			log.debug("Send message: {}", message);
			absSender.execute(message);
			return true;
		} catch (TelegramApiException e) {
			log.error("Cannot send reply message", e);
			return false;
		}
	}

}