package net.tindersamurai.russophobot.bot;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component @Slf4j @PropertySource("classpath:/bot.properties")
public final class ContactBot extends TelegramLongPollingBot {

	@Value("${token}") private String token;
	@Value("${name}") private String name;

	private final IBotLogic mainBotLogic;

	@Autowired
	public ContactBot(@Qualifier("mainLogic") IBotLogic mainBotLogic) {
		this.mainBotLogic = mainBotLogic;
		log.debug("ContactBot initialized");
	}


	@Override @SuppressWarnings("UnnecessaryReturnStatement")
	public void onUpdateReceived(Update update) {
		log.debug("Update statement");
		val status = mainBotLogic.process(update, this);
		log.debug("Update ended with status: {}", status ? "OK" : "ABORTED");
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