package net.tindersamurai.russophobot.service;

public interface IConfigService {

	String getProp(String name);
	void saveProp(String name, String value);

	void removeProp(String name);
	boolean isPropExists(String name);
}