package fr.maxlego08.zitemstacker;

import fr.maxlego08.zitemstacker.api.ItemManager;
import fr.maxlego08.zitemstacker.api.TranslationManager;
import fr.maxlego08.zitemstacker.command.commands.CommandItem;
import fr.maxlego08.zitemstacker.placeholder.LocalPlaceholder;
import fr.maxlego08.zitemstacker.save.Config;
import fr.maxlego08.zitemstacker.save.MessageLoader;
import fr.maxlego08.zitemstacker.zcore.ZPlugin;
import fr.maxlego08.zitemstacker.zcore.utils.plugins.Metrics;
import fr.maxlego08.zitemstacker.zcore.utils.plugins.VersionChecker;
import org.bukkit.NamespacedKey;

/**
 * System to create your plugins very simple Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class ItemStackerPlugin extends ZPlugin {

    private final ItemManager itemManager = new ZItemManager(this);
    private final TranslationManager translationManager = new ZTranslationManager(this);

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zitemstacker");

        this.preEnable();

        this.translationManager.loadTranslations();
        this.registerCommand("zitemstacker", new CommandItem(this));

        saveDefaultConfig();
        Config.getInstance().load(getConfig());
        this.addSave(new MessageLoader(this));

        this.loadFiles();

        new Metrics(this, 9330);

        VersionChecker checker = new VersionChecker(this, 15);
        checker.checkVersion();

        this.addListener(this.itemManager);

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.saveFiles();

        this.postDisable();
    }

    @Override
    public void reloadFiles() {
        reloadConfig();
        Config.getInstance();
        super.reloadFiles();
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public TranslationManager getTranslationManager() {
        return translationManager;
    }
}
