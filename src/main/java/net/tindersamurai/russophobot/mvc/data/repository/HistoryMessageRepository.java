package net.tindersamurai.russophobot.mvc.data.repository;

import net.tindersamurai.russophobot.mvc.data.entity.HistoryMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryMessageRepository extends JpaRepository<HistoryMessage, Long> {

	List<HistoryMessage> getAllByIdNotNullOrderByTimestampDesc(Pageable pageable);

	List<HistoryMessage> getAllByMailerOrderByTimestampDesc(int mailer, Pageable pageable);

}