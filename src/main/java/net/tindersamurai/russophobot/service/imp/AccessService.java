package net.tindersamurai.russophobot.service.imp;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.mvc.data.entity.Token;
import net.tindersamurai.russophobot.mvc.data.repository.SubscriberRepository;
import net.tindersamurai.russophobot.mvc.data.repository.TokenRepository;
import net.tindersamurai.russophobot.service.IAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service @Slf4j
public class AccessService implements IAccessService {

	private static final long EXPIRATION_TIME = 14400000; // 3H

	private final SubscriberRepository subscriberRepository;
	private final TokenRepository tokenRepository;

	@Autowired
	public AccessService(
			SubscriberRepository subscriberRepository,
			TokenRepository tokenRepository
	) {
		this.subscriberRepository = subscriberRepository;
		this.tokenRepository = tokenRepository;
	}

	@Override
	public Token generateAccessToken(int subscriberId) {
		log.debug("Generate access token for: {}", subscriberId);

		if (!subscriberRepository.existsByIdAndActiveTrue(subscriberId)) {
			log.debug("Access denied, this is not active subscriber id: {}", subscriberId);
			return null;
		}

		val token = new Token(); {
			token.setId(genTokenId());
			token.setUser(subscriberId);
			token.setType(Token.Type.AUTHORIZATION);
			token.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
		}

		try {
			tokenRepository.deleteAllByUser(subscriberId);
		} catch (Exception e) {
			log.error("Cannot delete user access token", e);
		}

		try {
			tokenRepository.saveAndFlush(token);
			return token;
		} catch (Exception e) {
			log.error("Cannot create and save user access token", token);
			return null;
		}
	}

	@Override
	public Token findAccessTokenById(String token) {
		log.debug("Find access token: {}", token);
		try {
			return tokenRepository.getByIdAndExpirationAfterAndType(
					token, new Date(System.currentTimeMillis()), Token.Type.AUTHORIZATION
			);
		} catch (Exception e) {
			log.error("Cannot find access token: " + token, e);
			return null;
		}
	}


	private String genTokenId() {
		String uuid = UUID.randomUUID().toString();
		while (tokenRepository.existsById(uuid))
			uuid = UUID.randomUUID().toString();
		log.debug("New token id: {}", uuid);
		return uuid;
	}
}