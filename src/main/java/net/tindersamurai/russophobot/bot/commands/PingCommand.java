package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component @Slf4j
public class PingCommand extends ABotCommand {

	@Override
	protected void onCommand(Update update, AbsSender sender) {

		val chatId = update.getMessage().getChatId();
		sendMessage(new SendMessage(chatId, "\uD83D\uDEA6pong"), sender);
	}

	@Override
	protected String getCommandName() {
		return "ping";
	}
}
