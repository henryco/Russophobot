package net.tindersamurai.russophobot.mvc.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Data @NoArgsConstructor
@AllArgsConstructor
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
			name = "user",
			nullable = false,
			updatable = false
	) String user;

}