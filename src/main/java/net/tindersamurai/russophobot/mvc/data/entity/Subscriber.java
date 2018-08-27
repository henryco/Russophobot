package net.tindersamurai.russophobot.mvc.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity @Data @NoArgsConstructor
@AllArgsConstructor
public class Subscriber {

	private @Id @Column(
			name = "id",
			nullable = false,
			updatable = false,
			unique = true
	) String id;


	private @Column(
			name = "chat",
			nullable = false,
			unique = true
	) long chatId;


	private @Column(
			name = "active",
			nullable = false
	) boolean active;

}