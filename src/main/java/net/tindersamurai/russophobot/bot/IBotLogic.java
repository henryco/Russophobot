package net.tindersamurai.russophobot.bot;

import lombok.val;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface IBotLogic {
	boolean process(Update update, AbsSender sender);

	@SuppressWarnings("Duplicates")
	default boolean sendMessage(BotApiMethod<?> message, AbsSender sender) {
		val log = LoggerFactory.getLogger(this.getClass());
		try {
			log.debug("Send message: {}", message);
			sender.execute(message);
			return true;
		} catch (TelegramApiException e) {
			log.error("Cannot send reply message", e);
			return false;
		}
	}

}