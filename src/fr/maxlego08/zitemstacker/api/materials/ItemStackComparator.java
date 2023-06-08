package fr.maxlego08.zitemstacker.api.materials;

import org.bukkit.inventory.ItemStack;

public interface ItemStackComparator {

	String getName();

	/***
	 * Check if item is similar
	 * 
	 * @param itemStack
	 * @return item is similar
	 */
	boolean isSimilar(ItemStack itemStack);

}
