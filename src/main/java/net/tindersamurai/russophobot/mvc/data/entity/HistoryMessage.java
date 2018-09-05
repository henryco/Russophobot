package net.tindersamurai.russophobot.mvc.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.TIMESTAMP;

@Entity @Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "history")
@Proxy(lazy=false)
public class HistoryMessage {

	private @Id @Column(
			name = "id",
			nullable = false,
			updatable = false,
			unique = true
	) @GeneratedValue(
			strategy = AUTO
	) long id;


	private @Column(
			name = "message",
			updatable = false,
			length = 4096
	) String message;


	private @Column(
			name = "mailer",
			updatable = false
	) Integer mailer;


	private @Column(
			name = "timestamp",
			nullable = false,
			updatable = false
	) @Temporal(
			value = TIMESTAMP
	) Date timestamp;


	private @ElementCollection @Column(
			name = "media",
			updatable = false
	) @CollectionTable(
			name = "media",
			joinColumns = @JoinColumn(
					name = "id"
			)
	) Set<String> media;

}