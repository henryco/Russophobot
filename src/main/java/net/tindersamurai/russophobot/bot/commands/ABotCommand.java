package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.bot.IBotLogic;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public abstract class ABotCommand implements IBotLogic {

	protected abstract void onCommand(Update update, AbsSender sender);
	protected abstract String getCommandName();

	public final boolean process(Update update, AbsSender sender) {
		if (update.hasMessage() && update.getMessage().isCommand()) {
			val command = update.getMessage().getText();
			log.debug("Command: {}, Found: {}", getCommandName(), command);
			if (check(getCommandName(), command))
				onCommand(update, sender);
		}
		return true;
	}

	private static boolean check(String command, String actual) {
		return actual.equalsIgnoreCase(command) || actual.equalsIgnoreCase("/" + command);
	}

}