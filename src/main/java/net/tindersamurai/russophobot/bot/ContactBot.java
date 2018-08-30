package net.tindersamurai.russophobot.bot;

import lombok.extern.slf4j.Slf4j;
import net.tindersamurai.russophobot.bot.commands.ABotCommand;
import net.tindersamurai.russophobot.bot.inline.ABotInlineProcessor;
import net.tindersamurai.russophobot.bot.message.AMessageProcessor;
import net.tindersamurai.russophobot.bot.reply.IBotReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component @Slf4j @PropertySource("classpath:/bot.properties")
public class ContactBot extends TelegramLongPollingBot {

	@Value("${token}") private String token;
	@Value("${name}") private String name;

	private final ABotInlineProcessor[] inlineProcessors;
	private final AMessageProcessor[] messageProcessors;
	private final ABotCommand[] commands;
	private final IBotReply reply;

	@Autowired
	public ContactBot(
			ABotInlineProcessor[] inlineProcessors,
			AMessageProcessor[] messageProcessors,
			ABotCommand[] commands,
			IBotReply reply
	) {
		this.inlineProcessors = inlineProcessors;
		this.messageProcessors = messageProcessors;
		this.commands = commands;
		this.reply = reply;

		log.debug("ContactBot initialized");
	}

	@Override
	public void onUpdateReceived(Update update) {
		log.debug("Update user: {}", update.getUpdateId());

		if (!processLogic(update, inlineProcessors))
			return;

		if (!processLogic(update, commands))
			return;

		if (!processLogic(update, messageProcessors))
			return;

		if (!processLogic(update, reply))
			return;

		// TODO MORE OPTIONS
	}

	@Override
	public String getBotUsername() {
		return name;
	}

	@Override
	public String getBotToken() {
		return token;
	}

	private boolean processLogic(Update update, IBotLogic ... array) {
		for (IBotLogic logic : array) {
			if (!logic.process(update, this))
				return false;
		}
		return true;
	}
}