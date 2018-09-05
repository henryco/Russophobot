package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.service.IAccessService;
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
public class TokenCommand extends ABotCommand {

	private final IAccessService accessService;
	private final IDataService dataService;

	@Value("${unauthorized.message}")
	private String accessDeniedMsg;

	@Autowired
	public TokenCommand(
			IAccessService accessService,
			IDataService dataService
	) {
		this.accessService = accessService;
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

		val token = accessService.generateAccessToken(id);
		if (token == null) {
			sendMessage(new SendMessage(chatId, "\uD83D\uDEA6Unexpected error"), sender);
			return;
		}

		sendMessage(new SendMessage(chatId, "\uD83D\uDEA6Access Token:"), sender);
		sendMessage(new SendMessage(chatId, token.getId()), sender);
	}

	@Override
	protected String getCommandName() {
		return "token";
	}
}
