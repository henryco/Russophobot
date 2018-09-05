package net.tindersamurai.russophobot.service;

import net.tindersamurai.russophobot.mvc.data.entity.Subscriber;

public interface ISubscriptionService {

	boolean subscribeUser(int id, String username, long chatId);

	boolean unSubscribeUser(int id);

	Subscriber confirmSubscriberViaToken(String token);
}
