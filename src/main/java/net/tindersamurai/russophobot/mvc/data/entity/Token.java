package net.tindersamurai.russophobot.mvc.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Data @NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens")
@Proxy(lazy=false)
public class Token {

	private @Id @Column(
			unique = true,
			nullable = false,
			updatable = false
	) String id;


	private @Column(
			name = "until",
			nullable = false,
			updatable = false
	) @Temporal(
			TIMESTAMP
	) Date expiration;


	private @Column(
			name = "user_id",
			nullable = false,
			updatable = false
	) int user;

}