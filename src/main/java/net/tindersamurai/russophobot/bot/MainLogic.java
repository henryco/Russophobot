package net.tindersamurai.russophobot.bot;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.bot.commands.ABotCommand;
import net.tindersamurai.russophobot.bot.inline.ABotInlineProcessor;
import net.tindersamurai.russophobot.bot.message.AMessageProcessor;
import net.tindersamurai.russophobot.bot.reply.IBotReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component @Slf4j
public class MainLogic implements IBotLogic {

	private static final String ERROR_MSG = "\uD83D\uDEA6Message processor error";

	private final ABotInlineProcessor[] inlineProcessors;
	private final AMessageProcessor[] messageProcessors;
	private final ABotCommand[] commands;
	private final IBotReply reply;

	@Autowired
	public MainLogic(
			ABotInlineProcessor[] inlineProcessors,
			AMessageProcessor[] messageProcessors,
			ABotCommand[] commands,
			IBotReply reply
	) {
		this.inlineProcessors = inlineProcessors;
		this.messageProcessors = messageProcessors;
		this.commands = commands;
		this.reply = reply;
		log.debug("Main logic initialized");
	}

	@Override
	public boolean process(Update update, AbsSender sender) {
		log.debug("Process user: {}", update.getUpdateId());

		try {
			if (!processLogic(update, sender, inlineProcessors))
				return false;

			if (!processLogic(update, sender, commands))
				return false;

			if (!processLogic(update, sender, messageProcessors))
				return false;

			if (!processLogic(update, sender, reply))
				return false;

			// TODO MORE OPTIONS

		} catch (Exception e) {
			log.error("Update processing error", e);
			val trace = (ERROR_MSG + "\n\n" + e.toString()).substring(0, 4095);
			sendMessage(new SendMessage(update.getMessage().getChatId(), trace), sender);
			return false;
		}
		return true;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private boolean processLogic(Update update, AbsSender sender, IBotLogic ... array) {
		for (IBotLogic logic : array) {
			if (!logic.process(update, sender))
				return false;
		}
		return true;
	}
}