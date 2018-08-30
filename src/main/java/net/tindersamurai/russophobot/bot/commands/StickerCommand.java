package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component @Slf4j
public class StickerCommand extends ABotCommand {


	@Override
	protected void onCommand(Update update, AbsSender sender) {

	}

	@Override
	protected String getCommandName() {
		return "sticker";
	}

}
