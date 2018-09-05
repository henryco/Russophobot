package net.tindersamurai.russophobot.mvc.data.repository;

import net.tindersamurai.russophobot.mvc.data.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TokenRepository extends JpaRepository<Token, String> {

	void deleteAllByUser(int user);

	Token getByIdAndExpirationAfterAndType(String id, Date expiration, Token.Type type);

}