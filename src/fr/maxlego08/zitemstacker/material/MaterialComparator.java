package fr.maxlego08.zitemstacker.material;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.zitemstacker.api.materials.ItemStackComparator;

public class MaterialComparator implements ItemStackComparator {

	private final Material material;

	/**
	 * @param material
	 */
	public MaterialComparator(Material material) {
		super();
		this.material = material;
	}

	@Override
	public String getName() {
		return "zitemstacker:material_similar";
	}

	@Override
	public boolean isSimilar(ItemStack itemStack) {
		return itemStack.getType() == material;
	}

}
