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
public class ListCommand extends ABotCommand {

	private final IDataService dataService;

	@Value("${unauthorized.message}")
	private String accessDeniedMsg;

	@Autowired
	public ListCommand(IDataService dataService) {
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

		val msg = new StringBuilder(); {

			msg.append("Subscribers:\n");
			for (val subscriber : dataService.getAllSubscribers()) {
				msg.append("\uD83D\uDC64 ID: ").append(subscriber.getId());
				if (subscriber.getUsername() != null)
					msg.append(" | UID: @").append(subscriber.getUsername());
				msg.append(" | Active: ").append(subscriber.isActive());
				msg.append("\n");
			}

			msg.append("\nMailers:\n");
			for (val mailer : dataService.getAllMailers()) {
				msg.append("\uD83D\uDDE3 ID: ").append(mailer.getId());
				if (mailer.getUsername() != null)
					msg.append(" | UID: @").append(mailer.getUsername());
				msg.append(" | Muted: ").append(mailer.isMuted());
				msg.append("\n");
			}
		}

		String message = msg.toString();
		while (message.length() > 2137) { // Jan Koder 3 kompilowal kody w sieci
			val sub = message.substring(0, 2137);
			message = message.substring(2137);
			sendMessage(new SendMessage(chatId, sub), sender);
		}

  		sendMessage(new SendMessage(chatId, msg.toString()), sender);
	}

	@Override
	protected String getCommandName() {
		return "list";
	}

}
