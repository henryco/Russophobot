package net.tindersamurai.russophobot.service;

import javax.mail.MessagingException;


public interface IEmailService {
	void sendConfirmationEmail(String token, String username) throws MessagingException;
}
