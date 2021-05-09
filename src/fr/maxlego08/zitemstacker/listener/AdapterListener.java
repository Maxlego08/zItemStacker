package fr.maxlego08.zitemstacker.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import fr.maxlego08.zitemstacker.ZItemPlugin;
import fr.maxlego08.zitemstacker.zcore.utils.ZUtils;

@SuppressWarnings("deprecation")
public class AdapterListener extends ZUtils implements Listener {

	private final ZItemPlugin template;

	public AdapterListener(ZItemPlugin template) {
		this.template = template;
	}

	@EventHandler
	public void onConnect(PlayerJoinEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onConnect(event, event.getPlayer()));
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		template.getListenerAdapters()
				.forEach(adapter -> adapter.onInventoryClick(event, (Player) event.getWhoClicked()));
	}

	@EventHandler
	public void onCraftItem(CraftItemEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onCraftItem(event));
	}

	@EventHandler
	public void onDrag(InventoryDragEvent event) {
		template.getListenerAdapters()
				.forEach(adapter -> adapter.onInventoryDrag(event, (Player) event.getWhoClicked()));
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onInventoryClose(event, (Player) event.getPlayer()));
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onDrop(event, event.getPlayer(), event.getItemDrop()));
	}
	
	@EventHandler
	public void onDrop(ItemSpawnEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onItemSpawn(event, event.getEntity(), event.getLocation()));
	}
	
	@EventHandler
	public void onDrop(ItemMergeEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onItemMerge(event, event.getEntity(), event.getTarget()));
	}

	@EventHandler
	public void onPick(PlayerPickupItemEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onPickUp(event, event.getPlayer()));
	}

	@EventHandler
	public void onInventoryPickUp(InventoryPickupItemEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onInventoryPickUp(event, event.getInventory(), event.getItem()));
	}
	
	@EventHandler
	public void onDeSpawn(ItemDespawnEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onDeSpawn(event, event.getEntity(), event.getLocation()));
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onChunkLoad(event, event.getChunk(), event.getWorld()));
	}
	
	/*@EventHandler
	public void onEntityPickup(EntityPickupItemEvent event) {
		template.getListenerAdapters().forEach(adapter -> adapter.onEntityPickUp(event, event.getEntity(), event.getItem()));
	}*/
}
