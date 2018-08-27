package net.tindersamurai.russophobot;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@PropertySource(value = "classpath:bot.properties")
public class DataSourceConfiguration {

	private @Value("${redis.host}") String redisHost;
	private @Value("${redis.port}") int redisPort;

	@Bean("JCF")
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(redisHost);
		factory.setPort(redisPort);
		factory.setUsePool(true);
		return factory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(
			@Qualifier("JCF") JedisConnectionFactory connectionFactory
	) {
		if (connectionFactory == null)
			throw new RuntimeException("JedisConnectionFactory == NULL");

		final RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericToStringSerializer<>(Object.class) );
		template.setValueSerializer(new GenericToStringSerializer<>(Object.class) );
		return template;
	}

}