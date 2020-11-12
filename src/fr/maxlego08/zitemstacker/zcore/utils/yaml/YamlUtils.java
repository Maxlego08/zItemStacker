package fr.maxlego08.zitemstacker.zcore.utils.yaml;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import fr.maxlego08.zitemstacker.zcore.logger.Logger;
import fr.maxlego08.zitemstacker.zcore.logger.Logger.LogType;
import fr.maxlego08.zitemstacker.zcore.utils.ZUtils;

public abstract class YamlUtils extends ZUtils {

	protected transient final Plugin plugin;

	/**
	 * @param plugin
	 */
	public YamlUtils(Plugin plugin) {
		super();
		this.plugin = plugin;
	}

	/**
	 * 
	 * @return file confirguration
	 */
	protected FileConfiguration getConfig() {
		return plugin.getConfig();
	}

	/**
	 * Get config
	 * 
	 * @param path
	 * @return {@link YamlConfiguration}
	 */
	protected YamlConfiguration getConfig(File file) {
		if (file == null)
			return null;
		return YamlConfiguration.loadConfiguration(file);
	}

	/**
	 * Get config
	 * 
	 * @param path
	 * @return {@link YamlConfiguration}
	 * @throws InventoryFileNotFoundException
	 */
	protected YamlConfiguration getConfig(String path) {
		File file = new File(plugin.getDataFolder() + "/" + path);
		if (!file.exists())
			return null;
		return getConfig(file);
	}

	/**
	 * Send info to console
	 * 
	 * @param message
	 */
	protected void info(String message) {
		Logger.info(message);
	}

	/**
	 * Send success to console
	 * 
	 * @param message
	 */
	protected void success(String message) {
		Logger.info(message, LogType.SUCCESS);
	}

	/**
	 * Send error to console
	 * 
	 * @param message
	 */
	protected void error(String message) {
		Logger.info(message, LogType.ERROR);
	}

	/**
	 * Send warn to console
	 * 
	 * @param message
	 */
	protected void warn(String message) {
		Logger.info(message, LogType.WARNING);
	}

}
