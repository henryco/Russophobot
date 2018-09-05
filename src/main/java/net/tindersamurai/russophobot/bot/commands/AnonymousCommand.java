package net.tindersamurai.russophobot.bot.commands;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.tindersamurai.russophobot.mvc.data.entity.Subscriber;
import net.tindersamurai.russophobot.service.IDataService;
import net.tindersamurai.russophobot.service.ITimeoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component @Slf4j
@PropertySource(value = "classpath:/values.properties", encoding = "UTF-8")
public class AnonymousCommand  extends ABotCommand {

	private static final String ERR_MSG = "⛔️ Only text, sticker, audio, voice, video and photos supported";

	private final ITimeoutService timeoutService;
	private final IDataService dataService;

	@Value("${timeout.message}")
	private String timeoutMsg;

	// todo @Value("${anon.message}")
	private String commandMessage =
			"Следующее сообщение отправленное в течении ближайших 60 секунд будет анонимным.";

	// todo @Value("${anon.done}")
	private String commandDone = "Анонимное сообщение отправлено";

	@Autowired
	public AnonymousCommand(
			ITimeoutService timeoutService,
			IDataService dataService
	) {
		this.timeoutService = timeoutService;
		this.dataService = dataService;
	}


	@Override
	protected void onCommand(Update update, AbsSender sender) {
		val id = update.getMessage().getFrom().getId();
		val chatId = update.getMessage().getChatId();
		val command = update.getMessage().getText().substring(1);

		if (dataService.isMailerMuted(id)) {
			return;
		}

		setContextCommand(id, command);
		sendMessage(new SendMessage(chatId, commandMessage), sender);
	}

	@Override
	protected void onContextCommand(Update update, AbsSender sender) {

		val message = update.getMessage();
		val chatId = update.getMessage().getChatId();
		val userName = message.getFrom().getUserName();
		val id = message.getFrom().getId();

		val timeout = timeoutService.testTimeout(message);
		if (timeoutService.isTimeouted(timeout)) {
			log.debug("TIMEOUT limit: {}ms, user: {}", timeout, userName + " | " + id);
			sendMessage(new SendMessage(chatId, timeoutMsg + " " + (((float) timeout) / 1000f) + " sec"), sender);
			return;
		}


		boolean updated = false;
		String mess = null;

		if (message.getText() != null && !message.getText().trim().isEmpty()) {
			mess = message.getText().trim();
		}

		val sticker = message.getSticker();
		if (sticker != null) {
			updated = true;
		}

		val photo = message.getPhoto();
		if (photo != null) {
			updated = true;
		}

		val audio = message.getAudio();
		if (audio != null) {
			updated = true;
		}

		val video = message.getVideo();
		if (video != null) {
			updated = true;
		}

		val voice = message.getVoice();
		if (voice != null) {
			updated = true;
		}

		if (!updated && mess == null) {
			sendMessage(new SendMessage(chatId, ERR_MSG), sender);
			return;
		}

		val finalMess = mess;
		val finalUpdated = updated;
		dataService.getAllSubscribers().stream().filter(Subscriber::isActive).forEach(subscriber -> {

			final long chat = subscriber.getChatId();

			sendMessage(new SendMessage(chat, "\uD83D\uDEA6Anonymous:"), sender);
			if (finalMess != null) sendMessage(new SendMessage(chat, finalMess), sender);
			if (!finalUpdated) return;

			if (sticker != null) {
				SendSticker s = new SendSticker().setChatId(chat)
						.setSticker(sticker.getFileId());
				try {
					log.debug("Send sticker: {}", s);
					sender.execute(s);
				} catch (TelegramApiException e) {
					log.error("Cannot send sticker", e);
				}
			}

			if (audio != null) {
				SendAudio s = new SendAudio();
				s.setAudio(audio.getFileId());
				s.setChatId(chat);
				try {
					log.debug("Send audio: {}", s);
					sender.execute(s);
				} catch (TelegramApiException e) {
					log.error("Cannot send audio", e);
				}
			}

			if (video != null) {
				SendVideo s = new SendVideo();
				s.setChatId(chat);
				s.setVideo(video.getFileId());
				try {
					log.debug("Send video: {}", s);
					sender.execute(s);
				} catch (TelegramApiException e) {
					log.error("Cannot send video", e);
				}
			}

			if (voice != null) {
				SendVoice s = new SendVoice();
				s.setChatId(chat);
				s.setVoice(voice.getFileId());
				try {
					log.debug("Send voice: {}", s);
					sender.execute(s);
				} catch (TelegramApiException e) {
					log.error("Cannot send voice", e);
				}
			}

			if (photo != null) {
				for (PhotoSize p : photo) {
					SendPhoto s = new SendPhoto();
					s.setChatId(chat);
					s.setPhoto(p.getFileId());
					try {
						log.debug("Send photo: {}", s);
						sender.execute(s);
					} catch (TelegramApiException e) {
						log.error("Cannot send photo", e);
					}
				}
			}

		});

		sendMessage(new SendMessage(chatId, commandDone), sender);
	}

	@Override
	protected String getCommandName() {
		return "anon";
	}
}