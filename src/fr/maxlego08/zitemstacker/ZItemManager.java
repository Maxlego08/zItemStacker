package fr.maxlego08.zitemstacker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.zitemstacker.listener.ListenerAdapter;

@SuppressWarnings("deprecation")
public class ZItemManager extends ListenerAdapter {

	private final Map<UUID, ZItem> items = new HashMap<UUID, ZItem>();
	private final double distance = 8.0;

	private Optional<ZItem> getItem(Item item) {
		return Optional.ofNullable(items.get(item.getUniqueId()));
	}

	@Override
	public void onPickUp(PlayerPickupItemEvent event, Player player) {
		
		Item target = event.getItem();
		Optional<ZItem> optional = getItem(target);
		
		if (optional.isPresent()) {
			
			event.setCancelled(true);
			
			ZItem item = optional.get();
			Inventory inventory = player.getInventory();
			item.give(inventory);
			
			if (item.getAmount() <= 0) {
				
				items.remove(item.getUniqueId());
				item.remove();
				
			}
			
		}
		
		
	}
	
	@Override
	public void onItemMerge(ItemMergeEvent event, Item entity, Item target) {

		ItemStack itemStack = entity.getItemStack();
		Optional<ZItem> optional = getItem(target);
		Optional<ZItem> optional2 = getItem(entity);

		if (optional.isPresent()) {

			ZItem item = optional.get();

			if (item.isSimilar(itemStack)) {

				if (optional2.isPresent()) {
					
					ZItem zItem = optional2.get();
					item.add(zItem.getAmount());
					items.remove(zItem.getUniqueId());
					zItem.remove();
					
				} else
					
					item.add(itemStack.getAmount());

				event.setCancelled(true);
				entity.remove();

			}
		}
	}

	@Override
	public void onItemSpawn(ItemSpawnEvent event, Item entity, Location location) {

		ItemStack itemStack = entity.getItemStack();
		Optional<ZItem> optional = getNearbyItems(location, itemStack);
		if (optional.isPresent()) {

			ZItem item = optional.get();

			if (!item.isValid())
				return;

			item.add(itemStack.getAmount());
			entity.remove();

		} else {

			ZItem item = new ZItem(entity);
			items.put(entity.getUniqueId(), item);

		}

	}

	/**
	 * 
	 * @param location
	 * @param itemStack
	 * @return
	 */
	private Optional<ZItem> getNearbyItems(Location location, ItemStack itemStack) {
		World world = location.getWorld();
		List<Entity> entities = world.getNearbyEntities(location, distance, distance, distance).stream()
				.filter(entity -> entity instanceof Item && ((Item) entity).getItemStack().isSimilar(itemStack))
				.collect(Collectors.toList());
		return entities.size() == 0 ? Optional.empty() : getItem((Item) entities.get(0));
	}

}
