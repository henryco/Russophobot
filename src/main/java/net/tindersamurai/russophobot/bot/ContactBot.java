package net.tindersamurai.russophobot.bot;

import lombok.extern.slf4j.Slf4j;
import net.tindersamurai.russophobot.bot.commands.ABotCommand;
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

	private final AMessageProcessor messageProcessor;
	private final ABotCommand[] commands;
	private final IBotReply reply;

	@Autowired
	public ContactBot(
			AMessageProcessor messageProcessor,
			ABotCommand[] commands,
			IBotReply reply
	) {
		this.messageProcessor = messageProcessor;
		this.commands = commands;
		this.reply = reply;

		log.debug("ContactBot initialized");
	}

	@Override
	public void onUpdateReceived(Update update) {
		log.debug("Update user: {}", update.getUpdateId());
		if (!messageProcessor.process(update, this)) return;
		if (!reply.process(update, this)) return;
		for (ABotCommand command : commands) {
			command.process(update, this);
		}
	}

	@Override
	public String getBotUsername() {
		return name;
	}

	@Override
	public String getBotToken() {
		return token;
	}

}