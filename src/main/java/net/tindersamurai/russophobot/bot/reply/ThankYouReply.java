package net.tindersamurai.russophobot.bot.reply;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;
import java.util.Random;

@Component @Slf4j
@PropertySource(value = "classpath:/values.properties", encoding = "UTF-8")
public class ThankYouReply implements IBotReply {

	private final IDataService dataService;
	private final String[] messages;
	private final int numb;

	@Value("${reply.subscriber}")
	private String subscriberResponse;

	@Autowired
	public ThankYouReply(IDataService dataService, Environment env) {
		this.numb = Integer.parseInt(Objects.requireNonNull(env.getProperty("reply.numb")));
		this.messages = new String[numb];
		this.dataService = dataService;
		for (int i = 0; i < numb; i++) {
			val k = i + 1;
			messages[i] = env.getProperty("reply." + k);
		}
	}

	@Override
	public boolean reply(Update update, AbsSender absSender) {
		if (update.hasMessage() && !update.getMessage().isCommand()) {

			val id = update.getMessage().getFrom().getId();
			val response = dataService.subscriberExists(id) ? subscriberResponse : rollMessage();

			log.debug("Reply to: {}", update.getMessage().getFrom().getUserName());
			val message = new SendMessage()
					.setReplyToMessageId(update.getMessage().getMessageId())
					.setChatId(update.getMessage().getChatId())
					.setText(response);
			try {
				absSender.execute(message);
				return true;
			} catch (TelegramApiException e) {
				log.error("Cannot send reply message", e);
				return true;
			}
		}
		return true;
	}

	private String rollMessage() {
		val msg = messages[new Random().nextInt(numb)];
		log.debug("reply: {}", msg);
		return msg;
	}
}