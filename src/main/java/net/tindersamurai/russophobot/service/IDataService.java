package net.tindersamurai.russophobot.service;

public interface IDataService {

	boolean subscribeUser(int id, String username, long chatId);
	boolean unSubscribeUser(int id);
	boolean confirmSubscriberViaToken(String token);

	String[] getSubscribers();
}