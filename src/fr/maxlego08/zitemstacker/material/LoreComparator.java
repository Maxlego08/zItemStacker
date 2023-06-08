package fr.maxlego08.zitemstacker.material;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.zitemstacker.api.materials.ItemStackComparator;

public class LoreComparator implements ItemStackComparator {

	private final String lore;

	/**
	 * @param lore
	 */
	public LoreComparator(String lore) {
		super();
		this.lore = lore;
	}

	@Override
	public String getName() {
		return "zitemstacker:contains_lore";
	}

	@Override
	public boolean isSimilar(ItemStack itemStack) {
		ItemMeta meta = itemStack.getItemMeta();
		return meta.hasLore() && meta.getLore().stream().anyMatch(e -> e != null && e.contains(this.lore));
	}

}
