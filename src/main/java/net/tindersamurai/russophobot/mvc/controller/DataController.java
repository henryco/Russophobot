package net.tindersamurai.russophobot.mvc.controller;

import lombok.val;
import net.tindersamurai.russophobot.mvc.data.entity.HistoryMessage;
import net.tindersamurai.russophobot.service.IAccessService;
import net.tindersamurai.russophobot.service.IHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.Collection;

@RestController
@RequestMapping("data")
public class DataController {

	private final IHistoryService historyService;
	private final IAccessService accessService;

	@Autowired
	public DataController(
			IHistoryService historyService,
			IAccessService accessService
	) {
		this.historyService = historyService;
		this.accessService = accessService;
	}


	@GetMapping("/history/{token}/{page}:{size}")
	public Collection<HistoryMessage> getHistory(
			@PathVariable("token") String token,
			@PathVariable("page") int page,
			@PathVariable("size") int size
	) throws AccessDeniedException{
		val t = accessService.findAccessTokenById(token);
		if (t == null)
			throw new AccessDeniedException("Access denied for token: " + token);
		return historyService.getAllHistory(page, size);
	}

}