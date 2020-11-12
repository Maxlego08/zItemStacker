package fr.maxlego08.zitemstacker;

import fr.maxlego08.zitemstacker.command.CommandManager;
import fr.maxlego08.zitemstacker.command.commands.CommandZItem;
import fr.maxlego08.zitemstacker.listener.AdapterListener;
import fr.maxlego08.zitemstacker.save.Config;
import fr.maxlego08.zitemstacker.save.Lang;
import fr.maxlego08.zitemstacker.zcore.ZPlugin;
import fr.maxlego08.zitemstacker.zcore.utils.plugins.MetricsLite;

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
		addListener(itemManager = new ZItemManager(this));

		/* Add Saver */
		addSave(Config.getInstance());
		addSave(Lang.getInstance());

		getSavers().forEach(saver -> saver.load(getPersist()));

		new MetricsLite(this, 9330);
		
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
