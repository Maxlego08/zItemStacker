package fr.maxlego08.zitemstacker.material;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.zitemstacker.api.materials.ItemStackComparator;

public class NameComparator implements ItemStackComparator {

	private final String name;

	

	/**
	 * @param name
	 */
	public NameComparator(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return "zitemstacker:names_contains";
	}

	@Override
	public boolean isSimilar(ItemStack itemStack) {
		ItemMeta meta = itemStack.getItemMeta();
		return meta.hasDisplayName() && meta.getDisplayName().contains(this.name);
	}

}
