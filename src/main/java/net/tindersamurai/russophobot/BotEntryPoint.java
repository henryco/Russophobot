package net.tindersamurai.russophobot;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component @Slf4j @ConfigurationProperties
public class BotEntryPoint implements CommandLineRunner {

	private final TelegramLongPollingBot contactBot;

	@Autowired
	public BotEntryPoint(
			TelegramLongPollingBot contactBot
	) {
		this.contactBot = contactBot;
	}

	@Override
	public void run(String... args) {

		try {
			TelegramBotsApi botsApi = new TelegramBotsApi();
			botsApi.registerBot(contactBot);
		} catch (TelegramApiException e) {
			log.error("Bot error", e);
		}
	}

}