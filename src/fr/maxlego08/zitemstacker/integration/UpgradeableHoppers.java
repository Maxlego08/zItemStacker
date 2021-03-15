package fr.maxlego08.zitemstacker.integration;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.zitemstacker.api.Item;
import fr.maxlego08.zitemstacker.api.ItemManager;
import me.angeschossen.upgradeablehoppers.api.ItemStackerIntegration;

public class UpgradeableHoppers implements ItemStackerIntegration {

	private final ItemManager itemManager;

	/**
	 * @param itemManager
	 */
	public UpgradeableHoppers(ItemManager itemManager) {
		super();
		this.itemManager = itemManager;
	}

	/**
	 * Get item stack from your custom stacked item. Your integration must
	 * handle the creation of the ItemStack object. That means that you need to
	 * return the correct ItemStack amount {@param item} and other values like
	 * NBT data.
	 *
	 * @param item
	 *            The item entity
	 * @param maxAmount
	 *            The returned ItemStack's amount should not be higher than
	 *            this. Usually the value is 64.
	 * @return null, if the item is not a stacked item of your plugin.
	 */
	public ItemStack getItemStack(org.bukkit.entity.Item entity, final int maxAmount) {
		Optional<Item> optional = itemManager.getItem(entity);
		if (!optional.isPresent())
			return null;
		Item item = optional.get();
		if (maxAmount > 64 && item.getAmount() > 64) {
			ItemStack itemStack = entity.getItemStack();
			itemStack.setAmount(64);
			item.remove(64);
			return itemStack;
		}
		ItemStack itemStack = entity.getItemStack();
		itemStack.setAmount(item.getAmount());
		return itemStack;
	}

	/**
	 * Add amount to a stacked item of your plugin.
	 *
	 * @param item
	 *            The item entity
	 * @param amount
	 *            The amount to add
	 * @return false, if the item is not a stacked item of your plugin.
	 */
	public boolean addAmountToItem(org.bukkit.entity.Item entity, final int amount) {
		Optional<Item> optional = itemManager.getItem(entity);
		if (!optional.isPresent())
			return false;
		Item item = optional.get();
		item.add(amount);
		return true;
	}

	@Override
	public String getName() {
		return "zItemStacker";
	}

}
