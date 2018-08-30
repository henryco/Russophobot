package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component @Slf4j
public class MuteCommand extends ABotCommand {

	private final IDataService dataService;

	@Autowired
	public MuteCommand(IDataService dataService) {
		this.dataService = dataService;
	}

	@Override
	protected void onCommand(Update update, AbsSender sender) {

		val id = update.getMessage().getFrom().getId();
		val chatId = update.getMessage().getChatId();
		if (!dataService.activeSubscriberExists(id)) {
			sendMessage(new SendMessage(chatId, "Access denied"), sender);
			return;
		}

		val mailerId = update.getMessage().getReplyToMessage().getForwardFrom().getId();
		val mailer = dataService.muteUnMuteMailer(mailerId);
		if (mailer == null)
			sendMessage(new SendMessage(chatId, "Unknown mailer"), sender);
		else
			sendMessage(new SendMessage(chatId, "Mailer muted: "+ mailer.isMuted()) , sender);
	}

	@Override
	protected String getCommandName() {
		return "mute";
	}

}
