package fr.maxlego08.zitemstacker.save;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class Config {

    public static boolean enableDebug = true;
    public static boolean enableDebugTime = false;
    public static String itemName = "#1bfaefx%amount% #1bfa6d%name%";
    public static long expireItemSeconds = 86400 * 7;
    public static boolean disableItemDespawn = false;
    public static double distanceOnDrop = 5.0;
    public static boolean disableEntityPickUp = true;
    public static String language = "en-us";
    public static List<String> disableWorlds = new ArrayList<>();

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

    public void load(FileConfiguration configuration) {
        enableDebug = configuration.getBoolean("enable-debug", true);
        enableDebugTime = configuration.getBoolean("enable-debug-time", false);
        itemName = configuration.getString("item-name", "#1bfaefx%amount% #1bfa6d%name%");
        expireItemSeconds = configuration.getLong("expire-item-seconds", 86400 * 7);
        disableItemDespawn = configuration.getBoolean("disable-item-despawn", false);
        distanceOnDrop = configuration.getDouble("distance-on-drop", 5.0);
        disableEntityPickUp = configuration.getBoolean("disable-entity-pickup", true);
        language = configuration.getString("language", "en-us");
        disableWorlds = configuration.getStringList("disable-worlds");
    }

}
