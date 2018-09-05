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
public class UnSubscribeCommand extends ABotCommand {

	@Value("${subscription.un.ok}")
	private String unSubOk;

	private final IDataService dataService;

	@Autowired
	public UnSubscribeCommand(IDataService dataService) {
		this.dataService = dataService;
	}


	@Override
	protected String getCommandName() {
		return "unsubscribe";
	}

	@Override
	protected void onCommand(Update update, AbsSender sender) {
		val from = update.getMessage().getFrom();

		if (dataService.unSubscribeUser(from.getId())) {
			try {
				sender.execute(new SendMessage()
						.setChatId(update.getMessage().getChatId())
						.setText(unSubOk));
			} catch (TelegramApiException e) {
				log.error("Cannot send reply message", e);
			}
		}
	}

}