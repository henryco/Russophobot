package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.bot.BotVariables;
import net.tindersamurai.russophobot.service.IConfigService;
import net.tindersamurai.russophobot.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component @Slf4j
public class StickerCommand extends ABotCommand {

	private final IConfigService configService;
	private final IDataService dataService;

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
		val command = update.getMessage().getText().substring(1);

		if (!dataService.activeSubscriberExists(id)) {
			sendMessage(new SendMessage(chatId, "Access denied"), sender);
			return;
		}

		setContextCommand(id, command);
		sendMessage(new SendMessage(chatId, "Now send sticker please"), sender);
	}

	@Override
	protected void onContextCommand(Update update, AbsSender sender) {

		val chatId = update.getMessage().getChatId();
		val sticker = update.getMessage().getSticker();
		if (sticker == null) {
			sendMessage(new SendMessage(chatId, "This is not sticker"), sender);
			return;
		}

		configService.saveProp(BotVariables.PINED_STICKER, sticker.getFileId());
	}

	@Override
	protected String getCommandName() {
		return "sticker";
	}

}
