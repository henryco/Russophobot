package net.tindersamurai.russophobot.service.imp;

import lombok.extern.slf4j.Slf4j;
import net.tindersamurai.russophobot.service.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service @Slf4j @PropertySource(value="classpath:/bot.properties", encoding = "UTF-8")
public class EmailService implements IEmailService {

	private final JavaMailSender mailSender;

	@Value("${confirm.address}")
	private String botMailAddress;

	@Value("${confirm.email}")
	private String adminMail;

	@Autowired
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void sendConfirmationEmail(String token, String username) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		message.setSubject("Confirm subscription");

		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(adminMail);
		helper.setText("<a href=\""+ botMailAddress + token + "\">Confirm subscription @" + username + "</a>",true);

		log.debug("Sending message: {}", message);
		mailSender.send(message);
	}
}