package net.tindersamurai.russophobot.bot.message;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.mvc.data.repository.MailersRepository;
import net.tindersamurai.russophobot.service.IDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import javax.persistence.EntityNotFoundException;

@Component @Slf4j
public class MessageReplier extends AMessageProcessor {

	private final MailersRepository mailersRepository;
	private final IDataService dataService;

	@Autowired
	public MessageReplier(
			MailersRepository mailersRepository,
			IDataService dataService
	) {
		this.mailersRepository = mailersRepository;
		this.dataService = dataService;

		log.debug("MessageReplier initialized");
	}

	@Override
	protected boolean onMessage(Update update, AbsSender sender) throws Exception {

		val message = update.getMessage();
		if (!message.isReply())
			return true; // if it is not reply => skip

		val id = message.getFrom().getId();
		if (!dataService.activeSubscriberExists(id))
			return false; // if author of reply is not subscriber => abort

		val reply = message.getReplyToMessage();

		val forwardFrom = reply.getForwardFrom();
		if (forwardFrom == null)
			return false; // we can reply only to forwards

		log.debug("***\n\n");
		log.debug("REPLY: {}", reply);
		log.debug("TXT: {}", reply.getText());
		log.debug("RTM: {}", reply.getReplyToMessage());
		log.debug("ID: {}", reply.getMessageId());
		log.debug("***");

		try {
			val mailer = mailersRepository.getOne(forwardFrom.getId());
			val msg = new SendMessage(); {
				msg.setText(message.getText());
				msg.setChatId(mailer.getChatId());
				msg.setReplyToMessageId(reply.getForwardFromMessageId());
			}
			log.debug("REPLY TO MAILER: {}", msg);
			sender.execute(msg);
		} catch (EntityNotFoundException e) {
			log.debug("Mailer not registered, abort");
			val msg = new SendMessage();{
				msg.setChatId(message.getChatId());
				msg.setReplyToMessageId(message.getMessageId());
				msg.setText("The recipient has not yet been registered");
			}
			sender.execute(msg);
		}
		return true;
	}

}
