package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component @Slf4j
public class ListCommand extends ABotCommand {

	private final IDataService dataService;

	@Autowired
	public ListCommand(IDataService dataService) {
		this.dataService = dataService;
	}

	@Override
	protected void onCommand(Update update, AbsSender sender) {

		val id = update.getMessage().getFrom().getId();
		if (!dataService.activeSubscriberExists(id)) {
			try {
				sender.execute(new SendMessage()
						.setChatId(update.getMessage().getChatId())
						.setText("Access denied"));
			} catch (TelegramApiException e) {
				log.error("Cannot send reply message", e);
			}
			return;
		}

		val msg = new StringBuilder(); {

			msg.append("Subscribers:\n");
			for (val subscriber : dataService.getAllSubscribers()) {
				msg.append("\uD83D\uDC64 ID: ").append(subscriber.getId());
				if (subscriber.getUsername() != null)
					msg.append(" UID: @").append(subscriber.getUsername());
				msg.append(" CID: ").append(subscriber.getChatId());
				msg.append(" Active: ").append(subscriber.isActive());
				msg.append("\n");
			}

			msg.append("Mailers:\n");
			for (val mailer : dataService.getAllMailers()) {
				msg.append("\uD83D\uDDE3 ID: ").append(mailer.getId());
				if (mailer.getUsername() != null)
					msg.append(" UID: @").append(mailer.getUsername());
				msg.append(" CID: ").append(mailer.getChatId());
				msg.append(" Muted: ").append(mailer.isMuted());
				msg.append("\n");
			}
		}

		try {
			sender.execute(new SendMessage()
					.setChatId(update.getMessage().getChatId())
					.setText(msg.toString()));
		} catch (TelegramApiException e) {
			log.error("Cannot send reply message", e);
		}

	}

	@Override
	protected String getCommandName() {
		return "list";
	}

}
