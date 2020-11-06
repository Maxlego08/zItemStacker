package fr.maxlego08.zitemstacker.save;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.maxlego08.zitemstacker.zcore.enums.Message;
import fr.maxlego08.zitemstacker.zcore.utils.storage.Persist;
import fr.maxlego08.zitemstacker.zcore.utils.storage.Saveable;

public class Lang implements Saveable {

	public static Map<Message, Object> messages = new HashMap<Message, Object>();

	static {

		for (Message message : Message.values()) {

			if (message.isTitle())
				messages.put(message, message.getTitles());
			else if (message.isMessage())
				messages.put(message, message.getMessages());
			else
				messages.put(message, message.getMessage());
		}

	}

	/**
	 * static Singleton instance.
	 */
	private static volatile Lang instance;

	/**
	 * Private constructor for singleton.
	 */
	private Lang() {
	}

	/**
	 * Return a singleton instance of Config.
	 */
	public static Lang getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (Lang.class) {
				if (instance == null) {
					instance = new Lang();
				}
			}
		}
		return instance;
	}

	public void save(Persist persist) {
		for (Message message : Message.values())
			if (message.isUse()) {
				if (message.isTitle())
					messages.putIfAbsent(message, message.getTitles());
				else if (message.isMessage())
					messages.putIfAbsent(message, message.getMessages());
				else
					messages.putIfAbsent(message, message.getMessage());
			}
		persist.save(getInstance());
	}

	@SuppressWarnings("unchecked")
	public void load(Persist persist) {
		persist.loadOrSaveDefault(getInstance(), Lang.class);
		messages.forEach((key, value) -> {
			if (value instanceof String)
				key.setMessage((String) value);
			else if (value instanceof List)
				key.setMessages((List<String>) value);
			else if (value instanceof Map)
				key.setTitles((Map<String, Object>) value);
		});
	}

}
