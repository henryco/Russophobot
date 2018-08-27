package net.tindersamurai.russophobot.bot.reply;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
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
@PropertySource(value = "classpath:bot.properties", encoding = "UTF-8")
public class ThankYouReply implements IBotReply {

	private final String[] messages;
	private final int numb;

	@Autowired
	public ThankYouReply(Environment env) {
		this.numb = Integer.parseInt(Objects.requireNonNull(env.getProperty("reply.numb")));
		this.messages = new String[numb];
		for (int i = 0; i < numb; i++) {
			val k = i + 1;
			messages[i] = env.getProperty("reply." + k);
		}
	}

	@Override
	public void reply(Update update, AbsSender absSender) {
		if (update.hasMessage() && !update.getMessage().isCommand()) {
			log.debug("Reply to: {}", update.getMessage().getFrom().getUserName());
			val message = new SendMessage()
					.setChatId(update.getMessage().getChatId())
					.setText(rollMessage());
			try {
				absSender.execute(message);
			} catch (TelegramApiException e) {
				log.error("Cannot send reply message", e);
			}
		}
	}

	private String rollMessage() {
		val msg = messages[new Random().nextInt(numb)];
		log.debug("reply: {}", msg);
		return msg;
	}
}