package fr.maxlego08.zitemstacker.integration;

import org.bukkit.entity.Item;

import com.bgsoftware.wildchests.api.WildChestsAPI;
import com.bgsoftware.wildchests.api.hooks.StackerProvider;

import fr.maxlego08.zitemstacker.api.ItemManager;

public class WildChests implements StackerProvider {

	private final ItemManager manager;

	/**
	 * @param manager
	 */
	public WildChests(ItemManager manager) {
		super();
		this.manager = manager;
		
		WildChestsAPI.getInstance().getProviders().setStackerProvider(this);
	}

	@Override
	public int getItemAmount(Item arg0) {
		return this.manager.getItemAmount(arg0);
	}

	@Override
	public void setItemAmount(Item arg0, int arg1) {
		this.manager.setAmount(arg0, arg1);
	}

}
