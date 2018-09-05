package net.tindersamurai.russophobot;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.telegram.telegrambots.ApiContextInitializer;


@SpringBootApplication
public class RussophobotApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(RussophobotApplication.class, args);
	}
}