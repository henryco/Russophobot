package net.tindersamurai.russophobot.service;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface ITimeoutService {
	boolean isTimeouted(long time);

	long testTimeout(Message message) throws RuntimeException;
}