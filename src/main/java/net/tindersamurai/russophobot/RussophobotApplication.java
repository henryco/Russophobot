package net.tindersamurai.russophobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.telegram.telegrambots.ApiContextInitializer;


@SpringBootApplication
public class RussophobotApplication {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		SpringApplication.run(RussophobotApplication.class, args);
	}

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();

//		JedisConnectionFactory jedisConFactory
//				= new JedisConnectionFactory();
//		jedisConFactory.setHostName("localhost");
//		jedisConFactory.setPort(6379);
//		return jedisConFactory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}
}