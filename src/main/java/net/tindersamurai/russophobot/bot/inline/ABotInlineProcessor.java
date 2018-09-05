package net.tindersamurai.russophobot.bot.inline;

import lombok.extern.slf4j.Slf4j;
import net.tindersamurai.russophobot.bot.IBotLogic;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public abstract class ABotInlineProcessor implements IBotLogic {

	@Override
	public boolean process(Update update, AbsSender sender) {
		if (update.hasInlineQuery()) {
			log.debug("Inline query processor: {}", this.getClass());
			return onInlineCall(update, sender);
		}
		return true;
	}

	protected abstract boolean onInlineCall(Update update, AbsSender sender);
}