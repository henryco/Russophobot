package net.tindersamurai.russophobot.service;

public interface IDataService {

	boolean subscribeUser(String username, long chatId);
	boolean unSubscribeUser(String username);
	boolean confirmSubscriberViaToken(String token);

	String[] getSubscribers();
}