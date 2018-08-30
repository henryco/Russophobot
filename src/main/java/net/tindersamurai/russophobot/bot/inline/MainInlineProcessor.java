package net.tindersamurai.russophobot.bot.inline;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component @Slf4j
public class MainInlineProcessor extends ABotInlineProcessor {


	@Override
	protected boolean onInlineCall(Update update, AbsSender sender) {
		return true;
	}
}
