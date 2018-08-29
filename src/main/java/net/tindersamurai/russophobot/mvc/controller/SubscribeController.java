package net.tindersamurai.russophobot.mvc.controller;

import lombok.val;
import net.tindersamurai.russophobot.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("subscription")
public class SubscribeController {

	private final IDataService dataService;

	@Autowired
	public SubscribeController(IDataService dataService) {
		this.dataService = dataService;
	}

	@GetMapping("/confirm/{token}")
	public String subscribe(@PathVariable String token) {
		if (dataService.confirmSubscriberViaToken(token))
			return "SUBSCRIPTION FINISHED SUCCESSFULLY";
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