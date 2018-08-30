package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.bot.IBotLogic;
import net.tindersamurai.russophobot.bot.commands.context.ICommandContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public abstract class ABotCommand implements IBotLogic {

	@Autowired private ICommandContext commandContext;

	protected abstract void onCommand(Update update, AbsSender sender);
	protected abstract String getCommandName();

	protected void onContextCommand(Update update, AbsSender sender) {
		log.debug("onContextCommand: {} {}", update, sender);
		// overridable
	}

	protected final ICommandContext getCommandContext() {
		return commandContext;
	}

	@Override
	public final boolean process(Update update, AbsSender sender) {
		if (update.hasMessage() && !update.getMessage().isCommand())
			processContext(update, sender);
		else if (update.hasMessage() && update.getMessage().isCommand())
			processCommand(update, sender);
		return true;
	}

	protected final void setContextCommand(int id, String command) {
		commandContext.setActualCommand(id, command);
	}

	private void processContext(Update update, AbsSender sender) {
		val id = update.getMessage().getFrom().getId();
		val command = commandContext.getActualCommand(id);
		if (command == null) return;
		log.debug("Context command: {}, Found: {}", getCommandName(), command);
		if (check(getCommandName(), command)) {
			onContextCommand(update, sender);
			commandContext.removeCommand(id);
		}
	}

	private void processCommand(Update update, AbsSender sender) {
		val command = update.getMessage().getText();
		log.debug("Command: {}, Found: {}", getCommandName(), command);
		if (check(getCommandName(), command))
			onCommand(update, sender);
	}

	private static boolean check(String command, String actual) {
		return actual.equalsIgnoreCase(command) || actual.equalsIgnoreCase("/" + command);
	}
}