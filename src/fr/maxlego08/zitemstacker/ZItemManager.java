package fr.maxlego08.zitemstacker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maxlego08.zitemstacker.listener.ListenerAdapter;
import fr.maxlego08.zitemstacker.save.Config;
import fr.maxlego08.zitemstacker.zcore.utils.loader.ItemStackLoader;
import fr.maxlego08.zitemstacker.zcore.utils.loader.Loader;
import fr.maxlego08.zitemstacker.zcore.utils.storage.Persist;
import fr.maxlego08.zitemstacker.zcore.utils.storage.Saveable;

@SuppressWarnings("deprecation")
public class ZItemManager extends ListenerAdapter implements Saveable {

	private static Map<UUID, ZItem> items = new HashMap<UUID, ZItem>();
	private transient List<ItemStack> whitelistItems = new ArrayList<ItemStack>();
	private transient boolean enableWhitelist = false;

	public ZItemManager(JavaPlugin plugin) {
		super(plugin);
	}

	private Optional<ZItem> getItem(Item item) {
		return Optional.ofNullable(items.get(item.getUniqueId()));
	}

	@Override
	public void onInventoryPickUp(InventoryPickupItemEvent event, Inventory inventory, Item target) {

		if (event.isCancelled())
			return;

		Optional<ZItem> optional = getItem(target);

		if (optional.isPresent()) {

			event.setCancelled(true);

			ZItem item = optional.get();

			item.give(inventory);

			if (item.getAmount() <= 0) {
				items.remove(item.getUniqueId());
				item.remove();
			}

		}
	}

	@Override
	public void onDeSpawn(ItemDespawnEvent event, Item entity, Location location) {
		if (event.isCancelled())
			return;

		Optional<ZItem> optional = getItem(entity);
		if (optional.isPresent()) {

			if (Config.disableItemDespawn)
				event.setCancelled(true);
			else
				items.remove(entity.getUniqueId());

		}
	}

	@Override
	public void onPickUp(PlayerPickupItemEvent event, Player player) {

		if (event.isCancelled())
			return;

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

		if (event.isCancelled())
			return;

		ItemStack itemStack = entity.getItemStack();
		
		if (isEnable() && !isWhitelist(itemStack))
			return;
		
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

		if (event.isCancelled())
			return;

		
		ItemStack itemStack = entity.getItemStack();
		
		if (isEnable() && !isWhitelist(itemStack))
			return;
		
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
	 * @param itemStack
	 * @return true if item is whitelist
	 */
	private boolean isWhitelist(ItemStack itemStack) {
		return itemStack != null 
				&& this.whitelistItems.stream().filter(item -> item.isSimilar(itemStack)).findFirst().isPresent();
	}

	private boolean isEnable() {
		return this.enableWhitelist;
	}

	/**
	 * 
	 * @param location
	 * @param itemStack
	 * @return
	 */
	private Optional<ZItem> getNearbyItems(Location location, ItemStack itemStack) {
		World world = location.getWorld();
		Optional<Entity> optional = world
				.getNearbyEntities(location, Config.distanceOnDrop, Config.distanceOnDrop, Config.distanceOnDrop)
				.parallelStream()
				.filter(entity -> entity instanceof Item && ((Item) entity).getItemStack().isSimilar(itemStack))
				.findFirst();
		return !optional.isPresent() ? Optional.empty() : getItem((Item) optional.get());
	}

	@Override
	public void save(Persist persist) {
		Iterator<Entry<UUID, ZItem>> iterator = items.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<UUID, ZItem> entry = iterator.next();
			ZItem item = entry.getValue();
			if (!item.isValid())
				iterator.remove();
		}
		persist.save(this, "items");

		File file = new File(plugin.getDataFolder(), "whitelist.yml");
		if (!file.exists()) {
			try {
				createDefaultFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, ZItemManager.class, "items");
		Iterator<Entry<UUID, ZItem>> iterator = items.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<UUID, ZItem> entry = iterator.next();
			ZItem item = entry.getValue();
			if (!item.isValid()) {
				iterator.remove();
			}
		}

		// Whitelist system
		this.loadConfiguration();
	}

	public void loadConfiguration(){
		File file = new File(plugin.getDataFolder(), "whitelist.yml");
		if (!file.exists()) {
			try {
				createDefaultFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		YamlConfiguration configuration = getConfig(file);
		enableWhitelist = configuration.getBoolean("enableWhitelist", false);
		ConfigurationSection configurationSection = configuration.getConfigurationSection("whitelist.");
		Loader<ItemStack> loader = new ItemStackLoader();

		this.whitelistItems = new ArrayList<>();

		for (String key : configurationSection.getKeys(false)) {

			String path = "whitelist." + key + ".";

			ItemStack itemStack = loader.load(configuration, path);
			whitelistItems.add(itemStack);

		}
	}
	
	private void createDefaultFile(File file) throws IOException {

		file.createNewFile();
		YamlConfiguration configuration = getConfig(file);
		configuration.set("enableWhitelist", enableWhitelist);

		List<ItemStack> itemStacks = new ArrayList<>();
		itemStacks.add(new ItemStack(Material.MELON));
		itemStacks.add(new ItemStack(Material.CACTUS));

		Loader<ItemStack> loader = new ItemStackLoader();
		AtomicInteger atomicInteger = new AtomicInteger(1);
		itemStacks.forEach(itemStack -> {
			loader.save(itemStack, configuration, "whitelist." + atomicInteger.getAndIncrement() + ".");
		});

		configuration.save(file);

	}

}
