package fr.maxlego08.zitemstacker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maxlego08.zitemstacker.api.ItemManager;
import fr.maxlego08.zitemstacker.api.enums.XSound;
import fr.maxlego08.zitemstacker.api.materials.ItemStackComparator;
import fr.maxlego08.zitemstacker.listener.ListenerAdapter;
import fr.maxlego08.zitemstacker.material.LoreComparator;
import fr.maxlego08.zitemstacker.material.MaterialComparator;
import fr.maxlego08.zitemstacker.material.ModelIdComparator;
import fr.maxlego08.zitemstacker.material.NameComparator;
import fr.maxlego08.zitemstacker.save.Config;
import fr.maxlego08.zitemstacker.zcore.logger.Logger;
import fr.maxlego08.zitemstacker.zcore.logger.Logger.LogType;
import fr.maxlego08.zitemstacker.zcore.utils.storage.Persist;
import fr.maxlego08.zitemstacker.zcore.utils.storage.Saveable;

@SuppressWarnings("deprecation")
public class ZItemManager extends ListenerAdapter implements Saveable, ItemManager {

	private static Map<UUID, ZItem> items = new HashMap<UUID, ZItem>();
	private transient List<ItemStackComparator> whitelistItems = new ArrayList<ItemStackComparator>();
	private transient List<ItemStackComparator> blacklistItems = new ArrayList<ItemStackComparator>();
	private transient boolean enableWhitelist = false;
	private transient boolean enableBlacklist = false;

	public ZItemManager(JavaPlugin plugin) {
		super(plugin);
	}

	private Optional<ZItem> getZItem(Item item) {
		return Optional.ofNullable(items.get(item.getUniqueId()));
	}

	@Override
	public void onInventoryPickUp(InventoryPickupItemEvent event, Inventory inventory, Item target) {

		if (event.isCancelled())
			return;

		Optional<ZItem> optional = getZItem(target);

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

	/**
	 * 
	 * @param entityEquipment
	 * @param itemStack
	 * @return
	 */
	private EquipmentSlot getEquipmentSlot(EntityEquipment entityEquipment, ItemStack itemStack) {
		for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
			ItemStack currentItemStack = entityEquipment.getItem(equipmentSlot);
			if (currentItemStack.isSimilar(itemStack)) {
				return equipmentSlot;
			}
		}
		return null;
	}

	@Override
	public void onEntityPickUp(EntityPickupItemEvent event, LivingEntity entity, Item target) {

		if (event.isCancelled()) {
			return;
		}

		Optional<ZItem> optional = this.getZItem(target);

		if (optional.isPresent()) {

			event.setCancelled(true);

			if (Config.disableEntityPickUp) {
				return;
			}

			ZItem item = optional.get();
			EntityEquipment entityEquipment = entity.getEquipment();

			EquipmentSlot slot = this.getEquipmentSlot(entityEquipment, target.getItemStack().clone());

			if (slot == null && event.getRemaining() == 0) {

				int maxAmount = Math.min(item.getAmount(), item.getItem().getItemStack().getMaxStackSize());

				slot = EquipmentSlot.HAND;
				ItemStack itemStack = target.getItemStack().clone();
				itemStack.setAmount(maxAmount);

				int newAmount = item.getAmount() - maxAmount;
				if (newAmount > 0) {
					item.remove(maxAmount);
				} else {
					item.remove();
				}

				entityEquipment.setItem(slot, itemStack);
				entityEquipment.setItemInMainHandDropChance(2.0f);

				return;
			}

			// System.out.println(slot);
			// System.out.println(event.getRemaining());
		}

	}

	@Override
	public void onDeSpawn(ItemDespawnEvent event, Item entity, Location location) {

		if (event.isCancelled()) {
			return;
		}

		Optional<ZItem> optional = getZItem(entity);
		if (optional.isPresent()) {

			if (Config.disableItemDespawn) {
				event.setCancelled(true);
			} else {
				items.remove(entity.getUniqueId());
			}

		}
	}

	@Override
	protected void onConnect(PlayerJoinEvent event, Player player) {
		if (!Config.disableAds) {
			schedule(1000, () -> {
				player.sendMessage(
						"§8(§fServeur Minecraft Vote§8) §azItemStacker Sponsor§7: §chttps://serveur-minecraft-vote.fr/?ref=345");
			});
		}
	}

	@Override
	public void onPickUp(PlayerPickupItemEvent event, Player player) {

		Item target = event.getItem();

		if (event.isCancelled()) {
			return;
		}

		Optional<ZItem> optional = getZItem(target);

		if (optional.isPresent()) {

			event.setCancelled(true);
			// target.remove();

			ZItem item = optional.get();

			Inventory inventory = player.getInventory();
			if (!item.give(inventory)) {
				target.remove();
				item.remove();
				return;
			}

			if (Config.pickupSound != null && Config.enablePickupSound) {
				XSound sound = Config.pickupSound;
				sound.play(player);
			}

			if (item.getAmount() <= 0) {

				items.remove(item.getUniqueId());
				target.remove();
				item.remove();
			}
		}
	}

	@Override
	public void onItemMerge(ItemMergeEvent event, Item entity, Item target) {

		if (event.isCancelled()) {
			return;
		}

		ItemStack itemStack = entity.getItemStack();

		if (isEnable() && !isWhitelist(itemStack)) {
			return;
		}

		if (isEnableBlacklist() && isBlacklist(itemStack)) {
			return;
		}

		Optional<ZItem> optional = getZItem(target);
		Optional<ZItem> optional2 = getZItem(entity);

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

		if (isEnable() && !isWhitelist(itemStack)) {
			return;
		}

		if (isEnableBlacklist() && isBlacklist(itemStack)) {
			return;
		}

		Optional<ZItem> optional = getNearbyItems(location, itemStack);
		if (optional.isPresent()) {

			ZItem item = optional.get();

			if (!item.isValid()) {
				return;
			}

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
		return itemStack != null && this.whitelistItems.stream().anyMatch(i -> i.isSimilar(itemStack));
	}

	private boolean isEnable() {
		return this.enableWhitelist;
	}

	public boolean isEnableBlacklist() {
		return enableBlacklist;
	}

	/**
	 * 
	 * @param itemStack
	 * @return true if item is whitelist
	 */
	private boolean isBlacklist(ItemStack itemStack) {
		return itemStack != null && this.blacklistItems.stream().anyMatch(i -> i.isSimilar(itemStack));
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
		return !optional.isPresent() ? Optional.empty() : getZItem((Item) optional.get());
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
		// blacklist system
		this.loadBlackConfiguration();
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void loadConfiguration() {

		this.whitelistItems.clear();

		File file = new File(this.plugin.getDataFolder(), "whitelist.yml");
		if (!file.exists()) {
			this.plugin.saveResource("whitelist.yml", false);
			file = new File(this.plugin.getDataFolder(), "whitelist.yml");
		}

		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		this.enableWhitelist = configuration.getBoolean("enableWhitelist", false);
		List<Map<String, Object>> values = (List<Map<String, Object>>) configuration.getList("whitelist");

		if (values != null) {
			values.forEach(map -> {
				ItemStackComparator comparator = getComparator(map);
				if (comparator != null) {
					this.whitelistItems.add(comparator);
				}
			});
		}

		Logger.info("Loaded " + this.whitelistItems.size() + " whitelist items", LogType.INFO);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void loadBlackConfiguration() {

		this.blacklistItems.clear();

		File file = new File(this.plugin.getDataFolder(), "blacklist.yml");
		if (!file.exists()) {
			this.plugin.saveResource("blacklist.yml", false);
			file = new File(this.plugin.getDataFolder(), "blacklist.yml");
		}

		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		this.enableBlacklist = configuration.getBoolean("enableBlacklist", false);
		List<Map<String, Object>> values = (List<Map<String, Object>>) configuration.getList("blacklists");

		if (values != null) {
			values.forEach(map -> {
				ItemStackComparator comparator = getComparator(map);
				if (comparator != null) {
					this.blacklistItems.add(comparator);
				}
			});
		}

		Logger.info("Loaded " + this.blacklistItems.size() + " blacklist items", LogType.INFO);
	}

	private ItemStackComparator getComparator(Map<String, Object> map) {

		String type = (String) map.get("type");
		switch (type) {
		case "zitemstacker:material_similar":
			try {
				Material material = Material.valueOf(((String) map.get("key")).toUpperCase());
				return new MaterialComparator(material);
			} catch (Exception e) {
				Logger.info("Material was not found for zitemstacker:material_similar", LogType.ERROR);
			}
			break;
		case "zitemstacker:contains_lore":
			try {
				return new LoreComparator((String) map.get("key"));
			} catch (Exception e) {
				Logger.info("Lore was not found for zitemstacker:contains_lore", LogType.ERROR);
			}
			break;
		case "zitemstacker:similar_model_id":
			try {
				return new ModelIdComparator((String) map.get("key"), (int) map.get("modelId"));
			} catch (Exception e) {
				Logger.info("Material or ModelId was not found for zitemstacker:similar_model_id", LogType.ERROR);
			}
			break;
		case "zitemstacker:names_contains":
			try {
				return new NameComparator((String) map.get("key"));
			} catch (Exception e) {
				Logger.info("Name was not found for zitemstacker:names_contains", LogType.ERROR);
			}
			break;

		default:
			Logger.info(type + " type was not found !", LogType.ERROR);
			break;
		}
		return null;
	}

	@Override
	public Optional<fr.maxlego08.zitemstacker.api.Item> getItem(UUID uuid) {
		return Optional.ofNullable(items.get(uuid));
	}

	@Override
	public Optional<fr.maxlego08.zitemstacker.api.Item> getItem(Item item) {
		return this.getItem(item.getUniqueId());
	}

	@Override
	public int getItemAmount(Item item) {
		Optional<fr.maxlego08.zitemstacker.api.Item> optional = getItem(item);
		return optional.isPresent() ? optional.get().getAmount() : item.getItemStack().getAmount();
	}

	@Override
	public void setAmount(Item item, int amount) {
		Optional<fr.maxlego08.zitemstacker.api.Item> optional = getItem(item);
		if (optional.isPresent()) {
			fr.maxlego08.zitemstacker.api.Item zItem = optional.get();
			zItem.setAmount(amount);
		} else
			item.getItemStack().setAmount(amount);
	}

}
