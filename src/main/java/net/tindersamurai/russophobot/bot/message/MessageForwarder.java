package net.tindersamurai.russophobot.bot.message;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.mvc.data.entity.Mailer;
import net.tindersamurai.russophobot.mvc.data.repository.MailersRepository;
import net.tindersamurai.russophobot.mvc.data.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import java.util.concurrent.TimeUnit;

@Component @Slf4j
@PropertySource(value = "classpath:/values.properties", encoding = "UTF-8")
public class MessageForwarder extends AMessageProcessor {

	private static final long MAX_TIMEOUT = 600000; // ms == 10 min
	private static final long MIN_TIMEOUT = 500; // ms

	private final RedisTemplate<String, Object> template;
	private final MailersRepository mailersRepository;
	private final SubscriberRepository repository;

	@Value("${timeout.message}")
	private String timeoutMsg;


	@Autowired
	public MessageForwarder(
			RedisTemplate<String, Object> template,
			MailersRepository mailersRepository,
			SubscriberRepository repository
	) {
		this.mailersRepository = mailersRepository;
		this.template = template;
		this.repository = repository;

		log.debug("MessageForwarder initialization");
	}

	@Override
	protected boolean onMessage(Update update, AbsSender sender) throws Exception {

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

		if (!repository.existsByIdAndActiveTrue(id)) {
			val timeout = testTimeout(message);
			if (timeout > MIN_TIMEOUT) {
				log.debug("TIMEOUT limit: {}ms, user: {}", timeout, userName + " | " + id);
				sender.execute(new SendMessage()
						.setChatId(update.getMessage().getChatId())
						.setText(timeoutMsg + " " + (((float) timeout) / 1000f) + " sec"));
				return false;
			}
		}

		for (val subscriber : repository.getAllByActiveTrue()) {
			if (subscriber.getId() == id) {
				log.debug("Message ignored because sender == receiver: {}", userName + " | " + id);
				continue;
			}

			val chatId = subscriber.getChatId();

			if (message.getSticker() != null) {
				val textMsg = new SendMessage(); {
					val from = message.getFrom();
					textMsg.setChatId(chatId);
					textMsg.setText("FROM: " + ((from.getUserName() == null || from.getUserName().isEmpty()) ?
							from.getFirstName() + " " + from.getLastName()
							: "@" + from.getUserName())
					);
				}
				sender.execute(textMsg);
			}


			val forwardMessage = new ForwardMessage(); {
				forwardMessage.setFromChatId(messageChatId);
				forwardMessage.setMessageId(messageId);
				forwardMessage.setChatId(chatId);
			}

			sender.execute(forwardMessage);

			log.debug("Message forwarded: {}", forwardMessage);
		}

		updateMailerInfo(id, messageChatId);

		return true;
	}


	private void updateMailerInfo(int id, long chatId) {

		val mailer = new Mailer(); {
			mailer.setChatId(chatId);
			mailer.setMuted(false);
			mailer.setId(id);
		}

		try {
			mailersRepository.saveAndFlush(mailer);
		} catch (Exception e) {
			log.error("Cannot update Mailer info", e);
		}
	}


	private long testTimeout(Message message) throws RuntimeException {

		val fromHash = message.getFrom().hashCode();
		val chatHash = message.getChatId().hashCode();

		val hash = fromHash | chatHash;
		val key = hash + ":" + message.getChatId();

		log.debug("TTKEY: {}", key);

		val keyExists = template.hasKey(key);
		long timeout = 0;

		boolean limit = false;

		if (keyExists != null && !keyExists)
			timeout = MIN_TIMEOUT;

		else {
			val v = template.opsForValue().get(key);
			if (v != null) {
				val lastVal = Long.parseLong(v.toString());
				if (lastVal >= MAX_TIMEOUT) limit = true;
				timeout = Math.min(lastVal * 5, MAX_TIMEOUT);
			}
		}

		template.opsForValue().set(key, timeout);
		template.expire(key, timeout, TimeUnit.MILLISECONDS);

		if (limit) throw new RuntimeException("TIMEOUT BAN EXCEEDED, USER IGNORED");

		return timeout;
	}

}