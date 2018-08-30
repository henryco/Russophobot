package net.tindersamurai.russophobot.mvc.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity @Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mailers")
@Proxy(lazy=false)
public class Mailer {

	private @Id @Column(
			name = "id",
			nullable = false,
			updatable = false,
			unique = true
	) int id;


	private @Column(
			name = "chat",
			nullable = false,
			unique = true
	) long chatId;


	private @Column(
			name = "muted",
			nullable = false
	) boolean muted;


	private @Column(
			name = "username",
			unique = true
	) String username;

}