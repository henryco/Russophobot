package net.tindersamurai.russophobot.bot.commands.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component @Slf4j
@PropertySource(value = "classpath:/bot.properties", encoding = "UTF-8")
public class SecretBundle {

	@Value("secret.question") @Getter private String question;
	@Value("secret.answer") @Getter private String answer;
}