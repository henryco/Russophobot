package net.tindersamurai.russophobot.bot.commands.context;

public interface ICommandContext {

	void setActualCommand(String key, String command);
	String getActualCommand(String key);

	default void setActualCommand(int key, String command) {
		setActualCommand(Integer.toString(key), command);
	}

	default String getActualCommand(int key) {
		return getActualCommand(Integer.toString(key));
	}

	void removeCommand(String key);

	default void removeCommand(int key) {
		removeCommand(Integer.toString(key));
	}
}