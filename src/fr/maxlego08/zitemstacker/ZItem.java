package fr.maxlego08.zitemstacker;

import java.util.UUID;

import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.zitemstacker.zcore.utils.ZUtils;

public class ZItem extends ZUtils {

	private final Item item;
	private int amount;

	/**
	 * @param item
	 */
	public ZItem(Item item) {
		super();
		this.item = item;
		this.amount = item.getItemStack().getAmount();
		this.item.getItemStack().setAmount(1);

		item.setCustomName("§6" + amount + " §e" + getItemName(item.getItemStack()));
		item.setCustomNameVisible(true);
	}

	public boolean isValid() {
		return item != null && item.isValid();
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void add(int amount) {
		this.amount += amount;
		this.item.setCustomName("§6" + this.amount + " §e" + getItemName(item.getItemStack()));
	}

	public boolean isSimilar(ItemStack itemStack) {
		return itemStack != null && isValid() && this.item.getItemStack().isSimilar(itemStack);
	}

	public UUID getUniqueId() {
		return item.getUniqueId();
	}

	public void delete() {
		if (isValid())
			item.remove();
	}

}
