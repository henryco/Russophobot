package net.tindersamurai.russophobot.service;

import net.tindersamurai.russophobot.mvc.data.entity.Token;

public interface IAccessService {
	Token generateAccessToken(int subscriberId);

	Token findAccessTokenById(String token);
}
