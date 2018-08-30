package net.tindersamurai.russophobot.service;

import net.tindersamurai.russophobot.mvc.data.entity.Mailer;
import net.tindersamurai.russophobot.mvc.data.entity.Subscriber;

import java.util.List;

public interface IDataService {

	boolean activeSubscriberExists(int id);

	boolean subscribeUser(int id, String username, long chatId);
	boolean subscriberExists(int id);
	boolean unSubscribeUser(int id);

	Subscriber confirmSubscriberViaToken(String token);
	Subscriber getSubscriberById(int id);

	List<Subscriber> getAllSubscribers();

	List<Mailer> getAllMailers();

	String[] getSubscribersInfo();

	Mailer muteUnMuteMailer(int id);

	boolean isMailerMuted(int id);
}