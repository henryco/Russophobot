package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.bot.commands.util.SecretBundle;
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
	private final SecretBundle secretBundle;

	@Autowired
	public SubscribeCommand(
			IDataService dataService,
			SecretBundle secretBundle
	) {
		this.dataService = dataService;
		this.secretBundle = secretBundle;
	}

	@Override
	protected String getCommandName() {
		return "subscribe";
	}

	@Override
	protected void onCommand(Update update, AbsSender sender) {
		val from = update.getMessage().getFrom();
		val chatId = update.getMessage().getChatId();

		setContextCommand(from.getId());
		sendMessage(new SendMessage(chatId, secretBundle.getQuestion()), sender);
	}

	@Override
	protected void onContextCommand(Update update, AbsSender sender) {

		val chatId = update.getMessage().getChatId();
		if (!update.getMessage().getText().trim().equalsIgnoreCase(secretBundle.getAnswer())) {
			sendMessage(new SendMessage(chatId, "⛔️"), sender);
			return;
		}

		val from = update.getMessage().getFrom();
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