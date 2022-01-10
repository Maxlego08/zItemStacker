package fr.maxlego08.zitemstacker;

import org.bukkit.plugin.ServicePriority;

import fr.maxlego08.zitemstacker.api.ItemManager;
import fr.maxlego08.zitemstacker.command.CommandManager;
import fr.maxlego08.zitemstacker.command.commands.CommandZItem;
import fr.maxlego08.zitemstacker.integration.UpgradeableHoppers;
import fr.maxlego08.zitemstacker.listener.AdapterListener;
import fr.maxlego08.zitemstacker.listener.AdapterListener2;
import fr.maxlego08.zitemstacker.save.Config;
import fr.maxlego08.zitemstacker.save.Lang;
import fr.maxlego08.zitemstacker.zcore.ZPlugin;
import fr.maxlego08.zitemstacker.zcore.utils.nms.NMSUtils;
import fr.maxlego08.zitemstacker.zcore.utils.plugins.MetricsLite;
import fr.maxlego08.zitemstacker.zcore.utils.plugins.Plugins;
import fr.maxlego08.zitemstacker.zcore.utils.plugins.VersionChecker;

/**
 * System to create your plugins very simply Projet:
 * https://github.com/Maxlego08/TemplatePlugin
 * 
 * @author Maxlego08
 *
 */
public class ZItemPlugin extends ZPlugin {

	private ZItemManager itemManager;

	@Override
	public void onEnable() {

		preEnable();

		commandManager = new CommandManager(this);

		/* Commands */

		this.registerCommand("zitemstacker", new CommandZItem(), "zitem");

		/* Add Listener */

		addListener(new AdapterListener(this));

		if (!NMSUtils.isNotEventVersion()) {
			addListener(new AdapterListener2(this));
		}

		addListener(itemManager = new ZItemManager(this));

		/* Add Saver */
		addSave(Config.getInstance());
		addSave(Lang.getInstance());

		getSavers().forEach(saver -> saver.load(getPersist()));

		new MetricsLite(this, 9330);

		// Register provider
		getServer().getServicesManager().register(ItemManager.class, itemManager, this, ServicePriority.Highest);

		if (isEnable(Plugins.UPGRADEABLEHOPPER)) {
			new UpgradeableHoppers(itemManager);
		}

		if (isEnable(Plugins.TRANSLATIONAPI)) {
			this.useTranslateAPI = true;
			this.getLog().log("Translation API found. We will use for translations.");
		}

		VersionChecker checker = new VersionChecker(this, 15);
		checker.useLastVersion();

		postEnable();
	}

	@Override
	public void onDisable() {

		preDisable();

		getSavers().forEach(saver -> saver.save(getPersist()));

		postDisable();

	}

	public ZItemManager getItemManager() {
		return itemManager;
	}

}
