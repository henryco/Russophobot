package net.tindersamurai.russophobot.bot.message;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.mvc.data.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component @Slf4j
public class MessageForwarder extends AMessageProcessor {

	private final RedisTemplate<String, Object> template;
	private final SubscriberRepository repository;

	@Autowired
	public MessageForwarder(
			RedisTemplate<String, Object> template,
			SubscriberRepository repository
	) {
		this.template = template;
		this.repository = repository;
	}

	@Override
	protected void onMessage(Update update, AbsSender sender) throws TelegramApiException {

		val userName = update.getMessage().getFrom().getUserName();
		val messageChatId = update.getMessage().getChatId();
		val messageId = update.getMessage().getMessageId();

//		https://dzone.com/articles/using-redis-spring
//		https://memorynotfound.com/spring-redis-application-configuration-example/
//		https://www.baeldung.com/spring-data-redis-tutorial
//		https://stackoverflow.com/questions/34893279/spring-data-redis-expire-key
//		https://www.baeldung.com/spring-data-redis-tutorial
//		https://redis.io/commands/expire

		for (val subscriber : repository.getAllByActiveTrue()) {
			if (subscriber.getId().equals(userName)) {
				log.debug("Message ignored: {}", update.getMessage());
				continue;
			}

			val chatId = subscriber.getChatId();
			val forwardMessage = new ForwardMessage(); {
				forwardMessage.setFromChatId(messageChatId);
				forwardMessage.setMessageId(messageId);
				forwardMessage.setChatId(chatId);
			}

			sender.execute(forwardMessage);

			log.debug("Message forwarded: {}", forwardMessage);
		}
	}
}