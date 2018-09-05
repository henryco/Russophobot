package net.tindersamurai.russophobot.service.imp;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.mvc.data.entity.Subscriber;
import net.tindersamurai.russophobot.mvc.data.entity.Token;
import net.tindersamurai.russophobot.mvc.data.repository.SubscriberRepository;
import net.tindersamurai.russophobot.mvc.data.repository.TokenRepository;
import net.tindersamurai.russophobot.service.IEmailService;
import net.tindersamurai.russophobot.service.ISubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service @Slf4j
public class SubscriptionService implements ISubscriptionService {

	private static final long EXPIRATION_TIME = 3600000; // 1H

	private final SubscriberRepository subscriberRepository;
	private final TokenRepository tokenRepository;
	private final IEmailService emailService;


	public SubscriptionService(
			SubscriberRepository subscriberRepository,
			TokenRepository tokenRepository,
			IEmailService emailService
	) {
		this.subscriberRepository = subscriberRepository;
		this.tokenRepository = tokenRepository;
		this.emailService = emailService;
	}


	@Override @Transactional
	public boolean subscribeUser(int id, String username, long chatId) {

		try {
			if (subscriberRepository.existsById(id))
				return false;
			val subscriber = new Subscriber(id, chatId, username,false);
			val token = new Token(); {
				token.setId(new Helper().genTokenId());
				token.setUser(id);
				token.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
				token.setType(Token.Type.SUBSCRIPTION);
			}

			subscriberRepository.save(subscriber);
			tokenRepository.save(token);

			emailService.sendConfirmationEmail(token.getId(), username);

			log.debug("Subscriber created: {}", subscriber);

			return true;
		} catch (Exception e) {
			log.error("subscribeUser: " + username, e);
			return false;
		}
	}


	@Override @Transactional
	public boolean unSubscribeUser(int id) {
		try {
			subscriberRepository.deleteById(id);
			log.debug("unsubscribed: {}", id);
			return true;
		} catch (Exception e) {
			log.error("unSubscribeUser: " + id, e);
			return false;
		}
	}

	@Override @Transactional
	public Subscriber confirmSubscriberViaToken(String token) {
		try {
			val t = tokenRepository.getOne(token);
			val user = subscriberRepository.getOne(t.getUser());

			if (t.getType() != Token.Type.SUBSCRIPTION) {
				log.debug("subscription unconfirmed WRONG TOKEN TYPE: {}", token);
				return null;
			}

			if (new Date(System.currentTimeMillis()).before(t.getExpiration())) {
				user.setActive(true);
				subscriberRepository.save(user);
				tokenRepository.delete(t);
				log.debug("subscription confirmed: {}", token);
				return user;
			}

			subscriberRepository.delete(user);
			tokenRepository.delete(t);
			log.debug("subscription unconfirmed: {}", token);
			return null;

		} catch (Exception e) {
			log.error("confirmSubscriberViaToken: " + token, e);
			return null;
		}
	}

	private final class Helper {

		private String genTokenId() {
			String uuid = UUID.randomUUID().toString();
			while (tokenRepository.existsById(uuid))
				uuid = UUID.randomUUID().toString();
			log.debug("New token id: {}", uuid);
			return uuid;
		}
	}

}