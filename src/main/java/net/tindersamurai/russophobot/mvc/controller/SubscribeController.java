package net.tindersamurai.russophobot.mvc.controller;

import lombok.val;
import net.tindersamurai.russophobot.bot.event.events.SubscriptionConfirmEvent;
import net.tindersamurai.russophobot.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("subscription")
public class SubscribeController {

	private final ApplicationEventPublisher publisher;
	private final IDataService dataService;

	@Autowired
	public SubscribeController(
			ApplicationEventPublisher publisher,
			IDataService dataService
	) {
		this.dataService = dataService;
		this.publisher = publisher;
	}

	@GetMapping("/confirm/{token}")
	public String subscribe(@PathVariable String token) {
		val subscriber = dataService.confirmSubscriberViaToken(token);
		if (subscriber != null) {
			publisher.publishEvent(new SubscriptionConfirmEvent(subscriber.getId(), true));
			return "SUBSCRIPTION FINISHED SUCCESSFULLY";
		}
		return "INVALID OR EXPIRED TOKEN";
	}

	@GetMapping(value = "/list")
	public String list() {
		val builder = new StringBuilder();
		for (String s : dataService.getSubscribersInfo())
			builder.append(s).append("\n");
		return builder.toString();
	}

}