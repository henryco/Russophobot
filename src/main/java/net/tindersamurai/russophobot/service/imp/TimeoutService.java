package net.tindersamurai.russophobot.service.imp;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.service.ITimeoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.concurrent.TimeUnit;

@Service @Slf4j
@PropertySource(value = "classpath:/values.properties", encoding = "UTF-8")
public class TimeoutService implements ITimeoutService {

	private static final long MAX_TIMEOUT = 600000; // ms == 10 min
	private static final long MIN_TIMEOUT = 500; // ms


	//		https://dzone.com/articles/using-redis-spring
	//		https://memorynotfound.com/spring-redis-application-configuration-example/
	//		https://www.baeldung.com/spring-data-redis-tutorial
	//		https://stackoverflow.com/questions/34893279/spring-data-redis-expire-key
	//		https://www.baeldung.com/spring-data-redis-tutorial
	//		https://redis.io/commands/expire


	private final RedisTemplate<String, Object> template;

	@Autowired
	public TimeoutService(RedisTemplate<String, Object> template) {
		this.template = template;
		log.debug("Timeout service creation");
	}

	@Override
	public boolean isTimeouted(long timeout) {
		val timeouted = timeout > MIN_TIMEOUT;
		log.debug("TIMEOUTED: {}", timeouted);
		return timeouted;
	}

	@Override
	public long testTimeout(Message message) throws RuntimeException {

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