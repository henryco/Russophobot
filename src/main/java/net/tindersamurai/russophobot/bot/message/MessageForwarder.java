package net.tindersamurai.russophobot.bot.message;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.mvc.data.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.TimeUnit;

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

		val message = update.getMessage();
		val userName = message.getFrom().getUserName();
		val id = message.getFrom().getId();
		val messageChatId = message.getChatId();
		val messageId = message.getMessageId();

//		https://dzone.com/articles/using-redis-spring
//		https://memorynotfound.com/spring-redis-application-configuration-example/
//		https://www.baeldung.com/spring-data-redis-tutorial
//		https://stackoverflow.com/questions/34893279/spring-data-redis-expire-key
//		https://www.baeldung.com/spring-data-redis-tutorial
//		https://redis.io/commands/expire

		val timeout = testTimeout(message);
		if (timeout > 0) {
			log.debug("TIMEOUT limit: {}ms, user: {}", timeout, userName + " | " + id);
			return;
		}

		for (val subscriber : repository.getAllByActiveTrue()) {
			if (subscriber.getId() == id) {
				log.debug("Message ignored: {}", userName + " | " + id);
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



	private static final long MIN_TIMEOUT = 50; // ms
	private static final long MAX_TIMEOUT = 600000; // ms == 10 min

	private long testTimeout(Message message) {

		val fromHash = message.getFrom().hashCode();
		val chatHash = message.getChatId().hashCode();

		val hash = fromHash | chatHash;
		val key = hash + ":" + message.getChatId();

		log.debug("TTKEY: {}", key);

		val keyExists = template.hasKey(key);
		long timeout = 0;

		if (keyExists != null && !keyExists) timeout = MIN_TIMEOUT;

		else {
			Long value = (Long) template.opsForValue().get(key);
			if (value != null) timeout = Math.max(value * 5, MAX_TIMEOUT);
		}

		template.opsForValue().set(key, timeout);
		template.expire(key, timeout, TimeUnit.MILLISECONDS);

		return timeout;
	}

}