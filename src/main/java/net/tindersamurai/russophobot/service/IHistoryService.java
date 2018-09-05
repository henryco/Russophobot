package net.tindersamurai.russophobot.service;

import net.tindersamurai.russophobot.mvc.data.entity.HistoryMessage;
import org.springframework.lang.Nullable;

import java.util.Collection;

public interface IHistoryService {

	Collection<HistoryMessage> getAllHistory(int page, int size);

	Collection<HistoryMessage> getAllHistoryOfMailer(int mailerId, int page, int size);

	boolean saveHistoryMessage(@Nullable Integer mailerId, String message, String... media);
}