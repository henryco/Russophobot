package net.tindersamurai.russophobot.bot.event;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.bot.event.events.SubscriptionConfirmEvent;
import net.tindersamurai.russophobot.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component @Slf4j
@PropertySource(value = "classpath:/values.properties", encoding = "UTF-8")
public class SubscriptionConfirmProcessor extends AEventProcessor {

	private final IDataService dataService;

	@Autowired
	public SubscriptionConfirmProcessor(IDataService dataService) {
		this.dataService = dataService;
	}

	@EventListener(condition = "#event.confirmed == true")
	public void onSubscriptionConfirm(SubscriptionConfirmEvent event) {
		val subscriber = dataService.getSubscriberById(event.getSubscriptionId());
		if (subscriber == null) {
			log.error("Subscriber == null");
			return;
		}
		sendMessage(new SendMessage(subscriber.getChatId(), "\uD83D\uDEA6Successful subscription\uD83D\uDEA6"));
	}

}