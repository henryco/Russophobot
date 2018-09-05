package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.bot.util.BotVariables;
import net.tindersamurai.russophobot.service.IConfigService;
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
public class StickerCommand extends ABotCommand {

	private final IConfigService configService;
	private final IDataService dataService;


	@Value("${unauthorized.message}")
	private String accessDeniedMsg;

	@Value("${done.message}")
	private String doneMsg;

	@Autowired
	public StickerCommand(
			IConfigService configService,
			IDataService dataService
	) {
		this.configService = configService;
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
		sendMessage(new SendMessage(chatId, "Now send sticker please"), sender);
	}

	@Override
	protected void onContextCommand(Update update, AbsSender sender) {
		log.debug("Context command: {}", getCommandName());

		val chatId = update.getMessage().getChatId();
		val sticker = update.getMessage().getSticker();
		if (sticker == null) {
			sendMessage(new SendMessage(chatId, "This is not sticker"), sender);
			return;
		}

		configService.saveProp(BotVariables.PINED_STICKER, sticker.getFileId());
		sendMessage(new SendMessage(chatId, doneMsg), sender);
	}

	@Override
	protected String getCommandName() {
		return "sticker";
	}

}
