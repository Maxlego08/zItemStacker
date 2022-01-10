package fr.maxlego08.zitemstacker.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import fr.maxlego08.zitemstacker.ZItemPlugin;

public class AdapterListener2 implements Listener{

	private final ZItemPlugin plugin;

	/**
	 * @param plugin
	 */
	public AdapterListener2(ZItemPlugin plugin) {
		super();
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntityPickup(EntityPickupItemEvent event) {
		this.plugin.getListenerAdapters().forEach(adapter -> adapter.onEntityPickUp(event, event.getEntity(), event.getItem()));
	}
	
}
