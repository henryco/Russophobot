package net.tindersamurai.russophobot.bot;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface IBotLogic {
	boolean process(Update update, AbsSender sender);
}
