package net.tindersamurai.russophobot.bot.event;

import lombok.extern.slf4j.Slf4j;
import net.tindersamurai.russophobot.mvc.data.entity.Subscriber;
import net.tindersamurai.russophobot.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component @Slf4j
@PropertySource(value = "classpath:/values.properties", encoding = "UTF-8")
public class StartUpProcessor extends AEventProcessor {

	@Value("${startup.message}")
	private String startupMsg;

	private final IDataService dataService;

	@Autowired
	public StartUpProcessor(IDataService dataService) {
		this.dataService = dataService;
	}

	@EventListener
	public void onBotStartUp(ApplicationStartedEvent event) {
		log.debug("STARTUP EVENT: {}", event);
		dataService.getAllSubscribers().stream().filter(Subscriber::isActive).forEach(subscriber -> {
			log.debug("SEND START UP NOTIFICATION TO: {}", subscriber);
			sendMessage(new SendMessage(subscriber.getChatId(), startupMsg));
		});
	}

}