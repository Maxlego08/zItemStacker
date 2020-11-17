package fr.maxlego08.zitemstacker.api;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Item;

public interface ItemManager {

	/**
	 * Get item
	 * @param item
	 * @return {@link fr.maxlego08.zitemstacker.api.Item}
	 */
	Optional<fr.maxlego08.zitemstacker.api.Item> getItem(Item item);
	
	/**
	 * Get item by uuid
	 * @param uuid
	 * @return {@link fr.maxlego08.zitemstacker.api.Item}
	 */
	Optional<fr.maxlego08.zitemstacker.api.Item> getItem(UUID uuid);
	
	/**
	 * Get item amount
	 * @param item
	 * @return item amount
	 */
	int getItemAmount(Item item);
	
	/**
	 * Set item amount
	 * @param item
	 * @param amount
	 */
	void setAmount(Item item, int amount);
	
}
