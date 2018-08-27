package net.tindersamurai.russophobot.mvc.data.repository;

import net.tindersamurai.russophobot.mvc.data.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {

}