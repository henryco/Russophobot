package net.tindersamurai.russophobot.service.imp;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.mvc.data.entity.Mailer;
import net.tindersamurai.russophobot.mvc.data.entity.Subscriber;
import net.tindersamurai.russophobot.mvc.data.repository.MailersRepository;
import net.tindersamurai.russophobot.mvc.data.repository.SubscriberRepository;
import net.tindersamurai.russophobot.mvc.data.repository.TokenRepository;
import net.tindersamurai.russophobot.service.IDataService;
import net.tindersamurai.russophobot.service.ISubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SimpleDataService implements IDataService {

	private final SubscriberRepository subscriberRepository;
	private final MailersRepository mailersRepository;

	private final ISubscriptionService subscriptionService;


	@Autowired
	public SimpleDataService(
			SubscriberRepository subscriberRepository,
			ISubscriptionService subscriptionService,
			MailersRepository mailersRepository
	) {
		this.subscriberRepository = subscriberRepository;
		this.subscriptionService = subscriptionService;
		this.mailersRepository = mailersRepository;
	}


	@Override
	public boolean subscriberExists(int id) {
		log.debug("subscriberExists: {}", id);
		return subscriberRepository.existsById(id);
	}

	@Override
	public boolean activeSubscriberExists(int id) {
		log.debug("activeSubscriberExists: {}", id);
		return subscriberRepository.existsByIdAndActiveTrue(id);
	}

	@Override
	public boolean subscribeUser(int id, String username, long chatId) {
		log.debug("subscribeUser: {}, {}, {}", id, username, chatId);
		return subscriptionService.subscribeUser(id, username, chatId);
	}

	@Override
	public boolean unSubscribeUser(int id) {
		log.debug("unSubscribeUser: {}", id);
		return subscriptionService.unSubscribeUser(id);
	}

	@Override
	public Subscriber confirmSubscriberViaToken(String token) {
		log.debug("confirmSubscriberViaToken: {}", token);
		return subscriptionService.confirmSubscriberViaToken(token);
	}

	@Override
	public Subscriber getSubscriberById(int id) {
		log.debug("getSubscriberById: {}", id);
		try {
			return subscriberRepository.getOne(id);
		} catch (Exception e) {
			log.error("Cannot find subscriber: {}", id, e);
			return null;
		}
	}

	@Override
	public List<Subscriber> getAllSubscribers() {
		log.debug("getAllSubscribers");
		return subscriberRepository.findAll();
	}

	@Override
	public List<Mailer> getAllMailers() {
		log.debug("getAllMailers");
		return mailersRepository.findAll();
	}

	@Override
	public String[] getSubscribersInfo() {
		log.debug("getSubscribersInfo");
		return getAllSubscribers().stream().map(Subscriber::toString).toArray(String[]::new);
	}

	@Override
	public Mailer muteUnMuteMailer(int id) {
		log.debug("muteUnMuteMailer: {}", id);
		try {
			val mailer = mailersRepository.getOne(id);
			mailer.setMuted(!mailer.isMuted());
			return mailersRepository.saveAndFlush(mailer);
		} catch (Exception e) {
			log.debug("Cannot get mailer", e);
			return null;
		}
	}

	@Override
	public boolean isMailerMuted(int id) {
		log.debug("isMailerMuted: {}", id);
		try {
			return mailersRepository.getOne(id).isMuted();
		} catch (Exception e) {
			log.debug("Cannot get mailer", e);
			return false;
		}
	}

}