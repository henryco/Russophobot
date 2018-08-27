package net.tindersamurai.russophobot.service;

public interface IDataService {

	boolean subscribeUser(int id, String username, long chatId);
	boolean unSubscribeUser(String username);
	boolean confirmSubscriberViaToken(String token);

	String[] getSubscribers();
}