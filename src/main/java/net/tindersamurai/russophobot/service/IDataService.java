package net.tindersamurai.russophobot.service;

import net.tindersamurai.russophobot.mvc.data.entity.Mailer;
import net.tindersamurai.russophobot.mvc.data.entity.Subscriber;

import java.util.List;

public interface IDataService extends ISubscriptionService {

	boolean activeSubscriberExists(int id);

	boolean subscriberExists(int id);

	Subscriber getSubscriberById(int id);

	List<Subscriber> getAllSubscribers();

	List<Mailer> getAllMailers();

	String[] getSubscribersInfo();

	Mailer muteUnMuteMailer(int id);

	boolean isMailerMuted(int id);
}