package net.tindersamurai.russophobot.bot.event;

import lombok.extern.slf4j.Slf4j;
import net.tindersamurai.russophobot.bot.event.events.StartUpEvent;
import net.tindersamurai.russophobot.mvc.data.entity.Subscriber;
import net.tindersamurai.russophobot.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component @Slf4j
@PropertySource(value = "classpath:/bot.properties", encoding = "UTF-8")
public class StartUpProcessor extends AEventProcessor {

	private static final String STARTUP_MSG = "Bot started!";
	private final IDataService dataService;

	@Autowired
	public StartUpProcessor(IDataService dataService) {
		this.dataService = dataService;
	}

	@Async @EventListener(condition = "#event.status == true")
	public void onBotStartUp(StartUpEvent event) {
		log.debug("STARTUP EVENT");
		dataService.getAllSubscribers().stream().filter(Subscriber::isActive).forEach(subscriber -> {
			log.debug("SEND START UP NOTIFICATION TO: {}", subscriber);
			sendMessage(new SendMessage(subscriber.getChatId(), STARTUP_MSG));
		});
	}

}