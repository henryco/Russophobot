package net.tindersamurai.russophobot.bot.message;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.mvc.data.entity.Mailer;
import net.tindersamurai.russophobot.mvc.data.repository.MailersRepository;
import net.tindersamurai.russophobot.mvc.data.repository.SubscriberRepository;
import net.tindersamurai.russophobot.service.IHistoryService;
import net.tindersamurai.russophobot.service.ITimeoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component @Slf4j
@PropertySource(value = "classpath:/values.properties", encoding = "UTF-8")
public class MessageForwarder extends AMessageProcessor {


	private final MailersRepository mailersRepository;
	private final SubscriberRepository repository;
	private final ITimeoutService timeoutService;
	private final IHistoryService historyService;

	@Value("${timeout.message}")
	private String timeoutMsg;


	@Autowired
	public MessageForwarder(
			MailersRepository mailersRepository,
			SubscriberRepository repository,
			ITimeoutService timeoutService,
			IHistoryService historyService
	) {
		this.mailersRepository = mailersRepository;
		this.timeoutService = timeoutService;
		this.historyService = historyService;
		this.repository = repository;

		log.debug("MessageForwarder initialization");
	}

	@Override
	protected boolean onMessage(Update update, AbsSender sender) throws Exception {

		val message = update.getMessage();
		val userName = message.getFrom().getUserName();
		val id = message.getFrom().getId();
		val messageChatId = message.getChatId();
		val messageId = message.getMessageId();

		if (mailersRepository.existsByIdAndMuted(id, true)) {
			sendMessage(new SendMessage(messageChatId, "\uD83D\uDED1⛔️\uD83D\uDEAB"), sender);
			return false;
		}

		if (!repository.existsByIdAndActiveTrue(id)) {
			val timeout = timeoutService.testTimeout(message);
			if (timeoutService.isTimeouted(timeout)) {
				log.debug("TIMEOUT limit: {}ms, user: {}", timeout, userName + " | " + id);
				sender.execute(new SendMessage()
						.setChatId(update.getMessage().getChatId())
						.setText(timeoutMsg + " " + (((float) timeout) / 1000f) + " sec"));
				return false;
			}
		}

		updateMailerInfo(id, messageChatId, userName);
		saveHistoryRecord(message);

		for (val subscriber : repository.getAllByActiveTrue()) {
			if (subscriber.getId() == id) {
				log.debug("Message ignored because sender == receiver: {}", userName + " | " + id);
				continue;
			}

			val chatId = subscriber.getChatId();

			if (message.getSticker() != null) {
				val textMsg = new SendMessage(); {
					val from = message.getFrom();
					textMsg.setChatId(chatId);
					textMsg.setText("\uD83D\uDCE8 " + ((from.getUserName() == null || from.getUserName().isEmpty()) ?
							from.getFirstName() + " " + from.getLastName()
							: "@" + from.getUserName())
					);
				}
				sender.execute(textMsg);
			}


			val forwardMessage = new ForwardMessage(); {
				forwardMessage.setFromChatId(messageChatId);
				forwardMessage.setMessageId(messageId);
				forwardMessage.setChatId(chatId);
			}

			sender.execute(forwardMessage);

			log.debug("Message forwarded: {}", forwardMessage);
		}

		return true;
	}


	private void updateMailerInfo(int id, long chatId, String username) {

		val mailer = new Mailer(); {
			mailer.setUsername(username);
			mailer.setChatId(chatId);
			mailer.setMuted(false);
			mailer.setId(id);
		}

		try {
			mailersRepository.saveAndFlush(mailer);
		} catch (Exception e) {
			log.error("Cannot update Mailer info", e);
		}
	}

	private void saveHistoryRecord(Message message) {
		// todo media
		historyService.saveHistoryMessage(
				message.getFrom().getId(),
				message.getText()
		);
	}

}