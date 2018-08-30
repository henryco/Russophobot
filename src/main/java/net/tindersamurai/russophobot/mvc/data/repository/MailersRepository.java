package net.tindersamurai.russophobot.mvc.data.repository;

import net.tindersamurai.russophobot.mvc.data.entity.Mailer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailersRepository extends JpaRepository<Mailer, Integer> {

	boolean existsByIdAndMuted(int id, boolean muted);
}