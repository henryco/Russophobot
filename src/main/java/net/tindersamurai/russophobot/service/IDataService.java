package net.tindersamurai.russophobot.service;

import net.tindersamurai.russophobot.mvc.data.entity.Subscriber;

import java.util.List;

public interface IDataService {

	boolean subscribeUser(int id, String username, long chatId);
	boolean unSubscribeUser(int id);
	boolean confirmSubscriberViaToken(String token);

	List<Subscriber> getAllSubscribers();

	String[] getSubscribersInfo();
}