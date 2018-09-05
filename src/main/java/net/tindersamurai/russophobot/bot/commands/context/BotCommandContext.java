package net.tindersamurai.russophobot.bot.commands.context;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component @Slf4j
public class BotCommandContext implements ICommandContext {

	private static final long CMD_EXPIRE = 60000L;
	private final RedisTemplate<String, Object> template;

	@Autowired
	public BotCommandContext(RedisTemplate<String, Object> template) {
		this.template = template;
	}

	@Override
	public void setActualCommand(String key, String command) {
		log.debug("SET ACTUAL COMMAND: {} : {}", key, command);
		if (key == null) {
			log.error("Key cannot be null, command: {}", command);
			return;
		}
		template.opsForValue().set(key, command);
		template.expire(key, CMD_EXPIRE, TimeUnit.MILLISECONDS);
	}

	@Override
	public String getActualCommand(String key) {
		log.debug("GET ACTUAL COMMAND: {}", key);
		if (key == null) {
			log.error("Key cannot be null");
			return null;
		}
		val command = template.opsForValue().get(key);
		if (command == null) {
			log.warn("Command for key: {} IS NULL", key);
			return null;
		}
		return command.toString();
	}

	@Override
	public void removeCommand(String key) {
		log.debug("REMOVE COMMAND: {}", key);
		if (key == null) {
			log.error("Key cannot be null");
			return;
		}
		template.delete(key);
	}
}