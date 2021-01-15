package fr.maxlego08.zitemstacker.api;

import java.util.UUID;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface Item {

	/**
	 * Get item as entity
	 * @return {@link org.bukkit.entity.Item}
	 */
	public org.bukkit.entity.Item toBukkitEntity();
	
	/**
	 * Get unique id
	 * @return uuid of entity
	 */
	public UUID getUniqueId();
	
	/**
	 * 
	 * @return amount of item
	 */
	public int getAmount();

	/**
	 * Set amount of item
	 * @param amount
	 */
	public void setAmount(int amount);

	/**
	 * 
	 * @param itemStack
	 * @return true if item is similar
	 */
	public boolean isSimilar(ItemStack itemStack);
	
	/**
	 * Update item name
	 */
	public void setItemName();
	
	/**
	 * Add items
	 * @param amount
	 */
	public void add(int amount);
	
	/**
	 * Remove item
	 * @param amount
	 */
	public void remove(int amount);
	
	/**
	 * 
	 * @return true if item is valid
	 */
	public boolean isValid();
	
	/**
	 * Give items in inventory
	 * @param inventory
	 */
	public boolean give(Inventory inventory);
	
	public long getCreatedAt();
	
	public long getExpireAt();
}
