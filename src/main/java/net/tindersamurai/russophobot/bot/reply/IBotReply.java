package net.tindersamurai.russophobot.bot.reply;

import net.tindersamurai.russophobot.bot.IBotLogic;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface IBotReply extends IBotLogic {

	@Override
	default void process(Update update, AbsSender sender) {
		reply(update, sender);
	}

	void reply(Update update, AbsSender absSender);
}