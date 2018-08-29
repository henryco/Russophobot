package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component @Slf4j
@PropertySource(value = "classpath:/values.properties", encoding = "UTF-8")
public class SubscribeCommand extends ABotCommand {

	@Value("${subscription.response.ok}")
	private String subscribeOk;

	@Value("${subscription.response.exists}")
	private String subscriptionExist;

	private final IDataService dataService;

	@Autowired
	public SubscribeCommand(IDataService dataService) {
		this.dataService = dataService;
	}

	@Override
	protected String getCommandName() {
		return "subscribe";
	}

	@Override
	protected void onCommand(Update update, AbsSender sender) {
		val from = update.getMessage().getFrom();
		val chatId = update.getMessage().getChatId();

		val userName = from.getUserName();
		val id = from.getId();
		val success = dataService.subscribeUser(id, userName, chatId);

		if (success) {
			val message = new SendMessage()
					.setText(subscribeOk)
					.setChatId(chatId);
			try {
				sender.execute(message);
			} catch (TelegramApiException e) {
				log.error("Cannot send reply message", e);
			}
		}
		else {
			if (dataService.subscriberExists(id)) {
				val message = new SendMessage()
						.setText(subscriptionExist)
						.setChatId(chatId);
				try {
					sender.execute(message);
				} catch (TelegramApiException e) {
					log.error("Cannot send reply message", e);
				}
			}
		}
	}
}