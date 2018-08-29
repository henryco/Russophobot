package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.mvc.data.entity.Mailer;
import net.tindersamurai.russophobot.mvc.data.repository.MailersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component @Slf4j
@PropertySource(value = "classpath:/values.properties", encoding = "UTF-8")
public class StartCommand extends ABotCommand {

	private final MailersRepository mailersRepository;

	@Autowired
	public StartCommand(MailersRepository mailersRepository) {
		this.mailersRepository = mailersRepository;
		log.debug("Start command registered");
	}

	@Override
	protected void onCommand(Update update, AbsSender sender) {
		val message = update.getMessage();

		val mailer = new Mailer(); {
			mailer.setId(message.getFrom().getId());
			mailer.setChatId(message.getChatId());
			mailer.setMuted(false);
		}

		log.debug("Registering mailer: {}", mailer);
		mailersRepository.saveAndFlush(mailer);
		log.debug("Mailer {} registered", mailer.getId());
	}

	@Override
	protected String getCommandName() {
		return "start";
	}
}