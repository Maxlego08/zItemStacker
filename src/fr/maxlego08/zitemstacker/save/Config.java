package fr.maxlego08.zitemstacker.save;

import fr.maxlego08.zitemstacker.api.enums.XSound;
import fr.maxlego08.zitemstacker.zcore.utils.storage.Persist;
import fr.maxlego08.zitemstacker.zcore.utils.storage.Saveable;

public class Config implements Saveable {

	public static double distanceOnDrop = 8.0;
	public static boolean disableItemDespawn = true;
	public static String itemName = "§bx%amount% §3%item%";
	public static long expireItemSeconds = 86400 * 7;
	public static boolean enablePickupSound = false;
	public static boolean disableEntityPickUp = true;
	public static XSound pickupSound = XSound.BLOCK_NOTE_BLOCK_PLING;

	/**
	 * static Singleton instance.
	 */
	private static volatile Config instance;

	/**
	 * Private constructor for singleton.
	 */
	private Config() {
	}

	/**
	 * Return a singleton instance of Config.
	 */
	public static Config getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (Config.class) {
				if (instance == null) {
					instance = new Config();
				}
			}
		}
		return instance;
	}

	public void save(Persist persist) {
		persist.save(getInstance());
	}

	public void load(Persist persist) {
		persist.loadOrSaveDefault(getInstance(), Config.class);
	}

}
