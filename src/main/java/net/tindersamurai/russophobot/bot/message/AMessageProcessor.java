package net.tindersamurai.russophobot.bot.message;

import lombok.extern.slf4j.Slf4j;
import net.tindersamurai.russophobot.bot.IBotLogic;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public abstract class AMessageProcessor implements IBotLogic {

	protected abstract boolean onMessage(Update update, AbsSender sender) throws Exception;

	@Override
	public boolean process(Update update, AbsSender sender) {
		if (update.hasMessage() && !update.getMessage().isCommand()) {
			log.debug("Process message: {}", update.getMessage());
			try {
				return onMessage(update, sender);
			} catch (Exception e) {
				log.error("Cannot process message", e);
				return false;
			}
		}
		return true;
	}
}