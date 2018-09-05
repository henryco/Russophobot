package net.tindersamurai.russophobot.service.imp;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.mvc.data.entity.HistoryMessage;
import net.tindersamurai.russophobot.mvc.data.repository.HistoryMessageRepository;
import net.tindersamurai.russophobot.service.IHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

@Service @Slf4j
public class HistoryService implements IHistoryService {

	private final HistoryMessageRepository repository;

	@Autowired
	public HistoryService(HistoryMessageRepository repository) {
		this.repository = repository;
	}

	@Override @Transactional
	public Collection<HistoryMessage> getAllHistory(int page, int size) {
		return repository.getAllByIdNotNullOrderByTimestampDesc(PageRequest.of(page, size));
	}

	@Override @Transactional
	public Collection<HistoryMessage> getAllHistoryOfMailer(int mailerId, int page, int size) {
		return repository.getAllByMailerOrderByTimestampDesc(mailerId, PageRequest.of(page, size));
	}

	@Override @Transactional
	public boolean saveHistoryMessage(@Nullable Integer mailerId, String message, String... media) {
		try {
			val msg = new HistoryMessage(); {
				msg.setMailer(mailerId);
				msg.setMessage(message);
				msg.setMedia(new HashSet<>(Arrays.asList(media)));
				msg.setTimestamp(new Date(System.currentTimeMillis()));
			}
			repository.save(msg);
			return true;
		} catch (Exception e) {
			log.error("Cannot save history record", e);
			return false;
		}
	}

}