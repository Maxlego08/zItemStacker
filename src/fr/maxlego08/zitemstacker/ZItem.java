package fr.maxlego08.zitemstacker;

import java.util.UUID;

import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
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

	public void give(Inventory inventory) {

		int inventorySize = inventory.getType().equals(InventoryType.HOPPER) ? 5 : 36;
		ItemStack itemStack = this.item.getItemStack();
		for (int a = 0; a != inventorySize; a++) {

			ItemStack currentItem = inventory.getItem(a);

			// Si l'item est null alors on peut ajouter 64
			if (currentItem == null) {

				int newAmount = Math.min(itemStack.getMaxStackSize(), this.amount);
				this.amount -= newAmount;

				ItemStack newItemStack = itemStack.clone();
				newItemStack.setAmount(newAmount);
				inventory.setItem(a, newItemStack);

			}
			// Si l'item est le même
			else if (itemStack.isSimilar(currentItem) && currentItem.getAmount() < currentItem.getMaxStackSize()) {

				int freeAmount = currentItem.getMaxStackSize() - currentItem.getAmount();
				int newAmount = Math.min(freeAmount, this.amount);
				this.amount -= newAmount;

				ItemStack newItemStack = itemStack.clone();
				newItemStack.setAmount(newAmount);
				inventory.setItem(a, newItemStack);

			}

			if (this.amount <= 0)
				return;

		}
		
		this.item.setCustomName("§6" + this.amount + " §e" + getItemName(item.getItemStack()));

	}

	public void remove() {
		this.item.remove();
	}

}
