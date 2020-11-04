package fr.maxlego08.zitemstacker;

import fr.maxlego08.zitemstacker.command.CommandManager;
import fr.maxlego08.zitemstacker.inventory.InventoryManager;
import fr.maxlego08.zitemstacker.listener.AdapterListener;
import fr.maxlego08.zitemstacker.save.Config;
import fr.maxlego08.zitemstacker.zcore.ZPlugin;

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

		if (!isEnabled())
			return;
		inventoryManager = InventoryManager.getInstance();

		/* Add Listener */

		addListener(new AdapterListener(this));
		addListener(inventoryManager);
		addListener(itemManager = new ZItemManager());

		/* Add Saver */
		addSave(Config.getInstance());
		// addSave(new CooldownBuilder());

		getSavers().forEach(saver -> saver.load(getPersist()));

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
