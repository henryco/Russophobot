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

@Component @Slf4j
@PropertySource(value = "classpath:/values.properties", encoding = "UTF-8")
public class NotifyCommand extends ABotCommand {

	@Value("${unauthorized.message}")
	private String accessDeniedMsg;

	@Value("${done.message}")
	private String doneMsg;

	private final IDataService dataService;

	@Autowired
	public NotifyCommand(IDataService dataService) {
		this.dataService = dataService;
	}

	@Override
	protected void onCommand(Update update, AbsSender sender) {
		val id = update.getMessage().getFrom().getId();
		val chatId = update.getMessage().getChatId();

		if (!dataService.activeSubscriberExists(id)) {
			sendMessage(new SendMessage(chatId, accessDeniedMsg), sender);
			return;
		}

		setContextCommand(id);
		sendMessage(new SendMessage(chatId, "Now send me message"), sender);
	}

	@Override
	protected void onContextCommand(Update update, AbsSender sender) {
		log.debug("Context command: {}", getCommandName());
		val chatId = update.getMessage().getChatId();
		val text = update.getMessage().getText();
		if (text == null || text.trim().isEmpty()) {
			sendMessage(new SendMessage(chatId, "This is not text message"), sender);
			return;
		}

		for (val mailer : dataService.getAllMailers()) {
			if (mailer.getId() == update.getMessage().getFrom().getId()) continue;
			log.debug("BROADCAST NOTIFICATION TO: {}", mailer);
			sendMessage(new SendMessage(mailer.getChatId(), text), sender);
		}
		sendMessage(new SendMessage(chatId, doneMsg), sender);
	}

	@Override
	protected String getCommandName() {
		return "notify";
	}

}