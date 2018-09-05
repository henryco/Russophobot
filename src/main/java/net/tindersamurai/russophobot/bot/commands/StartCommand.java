package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.bot.BotVariables;
import net.tindersamurai.russophobot.mvc.data.entity.Mailer;
import net.tindersamurai.russophobot.mvc.data.repository.MailersRepository;
import net.tindersamurai.russophobot.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component @Slf4j
@PropertySource(value = "classpath:/values.properties", encoding = "UTF-8")
public class StartCommand extends ABotCommand {

	private final MailersRepository mailersRepository;
	private final IConfigService configService;

	@Autowired
	public StartCommand(
			MailersRepository mailersRepository,
			IConfigService configService
	) {
		this.mailersRepository = mailersRepository;
		this.configService = configService;
		log.debug("Start command registered");
	}

	@Override
	protected void onCommand(Update update, AbsSender sender) {
		val message = update.getMessage();

		val mailer = new Mailer(); {
			mailer.setUsername(message.getFrom().getUserName());
			mailer.setId(message.getFrom().getId());
			mailer.setChatId(message.getChatId());
			mailer.setMuted(false);
		}

		log.debug("Registering mailer: {}", mailer);
		mailersRepository.saveAndFlush(mailer);
		log.debug("Mailer {} registered", mailer.getId());


		sendWelcomeSticker(update.getMessage().getChatId(), sender);
	}

	private void sendWelcomeSticker(long chatId, AbsSender sender) {
		val stickerId = configService.getProp(BotVariables.PINED_STICKER);
		if (stickerId == null) return;

		val sendSticker = new SendSticker().setSticker(stickerId).setChatId(chatId);

		try {
			log.debug("Send sticker: {}", sendSticker);
			sender.execute(sendSticker);
		} catch (TelegramApiException e) {
			log.error("Cannot send sticker", e);
		}
	}

	@Override
	protected String getCommandName() {
		return "start";
	}
}