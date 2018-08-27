package net.tindersamurai.russophobot.mvc.data.repository;

import net.tindersamurai.russophobot.mvc.data.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriberRepository extends JpaRepository<Subscriber, Integer> {
	List<Subscriber> getAllByActiveTrue();
}