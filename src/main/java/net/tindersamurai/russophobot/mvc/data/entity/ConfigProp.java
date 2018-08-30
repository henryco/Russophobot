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
@Table(name = "configurations")
@Proxy(lazy=false)
public class ConfigProp {

	private @Id @Column(
			name = "id",
			unique = true,
			nullable = false
	) String id;


	private @Column(
			name = "prop"
	) String value;

}