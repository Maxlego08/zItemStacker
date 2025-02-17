package fr.maxlego08.zitemstacker.zcore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.maxlego08.zitemstacker.ItemStackerPlugin;
import fr.maxlego08.zitemstacker.command.CommandManager;
import fr.maxlego08.zitemstacker.command.VCommand;
import fr.maxlego08.zitemstacker.placeholder.LocalPlaceholder;
import fr.maxlego08.zitemstacker.placeholder.Placeholder;
import fr.maxlego08.zitemstacker.zcore.logger.Logger;
import fr.maxlego08.zitemstacker.zcore.logger.Logger.LogType;
import fr.maxlego08.zitemstacker.zcore.utils.gson.LocationAdapter;
import fr.maxlego08.zitemstacker.zcore.utils.gson.PotionEffectAdapter;
import fr.maxlego08.zitemstacker.zcore.utils.plugins.Plugins;
import fr.maxlego08.zitemstacker.zcore.utils.storage.NoReloadable;
import fr.maxlego08.zitemstacker.zcore.utils.storage.Persist;
import fr.maxlego08.zitemstacker.zcore.utils.storage.Savable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class for plugins that uses the ZCore API. This class contains
 * methods for getting the plugin's logger, gson, persist, command manager,
 * listener adapters, savers and provider.
 *
 * @author MaximeLECORGUILLE
 */
public abstract class ZPlugin extends JavaPlugin {

    /**
     * The logger for the plugin.
     */
    private final Logger log = new Logger(this.getDescription().getFullName());

    /**
     * The list of savers for the plugin.
     */
    private final List<Savable> savers = new ArrayList<>();

    /**
     * The command manager for the plugin.
     */
    protected CommandManager commandManager;

    /**
     * The gson for the plugin.
     */
    private Gson gson;

    /**
     * The persist for the plugin.
     */
    private Persist persist;

    /**
     * The time when the plugin is enabled.
     */
    private long enableTime;

    /**
     * Called when the plugin is enabled. This method is called after the
     * {@link #onEnable()} method.
     */
    protected void preEnable() {

        LocalPlaceholder.getInstance().setPlugin((ItemStackerPlugin) this);
        Placeholder.getPlaceholder();

        this.enableTime = System.currentTimeMillis();

        this.log.log("=== ENABLE START ===");
        this.log.log("Plugin Version V<&>c" + getDescription().getVersion(), LogType.INFO);

        this.getDataFolder().mkdirs();

        this.gson = getGsonBuilder().create();
        this.persist = new Persist(this);

        this.commandManager = new CommandManager((ItemStackerPlugin) this);
    }

    /**
     * Called when the plugin is enabled. This method is called after the
     * {@link #preEnable()} method.
     */
    protected void postEnable() {

        if (this.commandManager != null) {
            this.commandManager.validCommands();
        }

        this.log.log("=== ENABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

    }

    /**
     * Called when the plugin is disabled. This method is called before the
     * {@link #onDisable()} method.
     */
    protected void preDisable() {
        this.enableTime = System.currentTimeMillis();
        this.log.log("=== DISABLE START ===");
    }

    /**
     * Called when the plugin is disabled. This method is called after the
     * {@link #preDisable()} method.
     */
    protected void postDisable() {
        this.log.log("=== DISABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");
    }

    /**
     * Builds the gson for the plugin.
     *
     * @return the gson builder
     */
    public GsonBuilder getGsonBuilder() {
        return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE).registerTypeAdapter(PotionEffect.class, new PotionEffectAdapter(this)).registerTypeAdapter(Location.class, new LocationAdapter(this));
    }

    /**
     * Adds a listener to the plugin.
     *
     * @param listener the listener to add
     */
    public void addListener(Listener listener) {
        if (listener instanceof Savable) this.addSave((Savable) listener);
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    /**
     * Adds a savable to the plugin.
     *
     * @param saver the savable to add
     */
    public void addSave(Savable saver) {
        this.savers.add(saver);
    }

    /**
     * Gets the logger for the plugin.
     *
     * @return the logger
     */
    public Logger getLog() {
        return this.log;
    }

    /**
     * Gets the gson for the plugin.
     *
     * @return the gson
     */
    public Gson getGson() {
        return gson;
    }

    public Persist getPersist() {
        return persist;
    }

    /**
     * Gets all the savers for the plugin.
     *
     * @return the list of savers
     */
    public List<Savable> getSavers() {
        return savers;
    }

    /**
     * Gets the provider for the plugin.
     *
     * @param classz the class to get the provider for
     * @return the provider
     */
    protected <T> T getProvider(Class<T> classz) {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(classz);
        if (provider == null) {
            log.log("Unable to retrieve the provider " + classz, LogType.WARNING);
            return null;
        }
        return provider.getProvider() != null ? provider.getProvider() : null;
    }

    /**
     * Gets the command manager for the plugin.
     *
     * @return the command manager
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Checks if a plugin is enabled.
     *
     * @param plugins the name of the plugin to check
     * @return true if the plugin is enabled, false otherwise
     */
    protected boolean isEnable(Plugins plugins) {
        Plugin plugin = getPlugin(plugins);
        return plugin != null && plugin.isEnabled();
    }

    /**
     * Gets a plugin for the given plugin name.
     *
     * @param plugins the name of the plugin to get
     * @return the plugin
     */
    protected Plugin getPlugin(Plugins plugins) {
        return Bukkit.getPluginManager().getPlugin(plugins.getName());
    }

    /**
     * Registers a command.
     *
     * @param command  the command to register
     * @param vCommand the command to register
     * @param aliases  the aliases for the command
     */
    protected void registerCommand(String command, VCommand vCommand, String... aliases) {
        this.commandManager.registerCommand(this, command, vCommand, Arrays.asList(aliases));
    }

    /**
     * Loads all the savers for the plugin.
     */
    public void loadFiles() {
        this.savers.forEach(save -> save.load(this.persist));
    }

    /**
     * Saves all the savers for the plugin.
     */
    public void saveFiles() {
        this.savers.forEach(save -> save.save(this.persist));
    }

    /**
     * Reloads all the savers for the plugin.
     */
    public void reloadFiles() {
        this.savers.forEach(save -> {
            if (!(save instanceof NoReloadable)) {
                save.load(this.persist);
            }
        });
    }

}