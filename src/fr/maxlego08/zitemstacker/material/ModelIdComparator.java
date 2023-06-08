package fr.maxlego08.zitemstacker.material;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.zitemstacker.api.materials.ItemStackComparator;
import fr.maxlego08.zitemstacker.zcore.utils.nms.NMSUtils;

public class ModelIdComparator implements ItemStackComparator{

	private final String key;
	private final int modelId;

	/**
	 * @param key
	 * @param modelId
	 */
	public ModelIdComparator(String key, int modelId) {
		super();
		this.key = key;
		this.modelId = modelId;
	}

	@Override
	public String getName() {
		return "zitemstacker:similar_model_id";
	}

	@Override
	public boolean isSimilar(ItemStack itemStack) {

		if (!NMSUtils.isHexColor()) {
			return false;
		}

		ItemMeta meta = itemStack.getItemMeta();

		return itemStack.getType().name().equalsIgnoreCase(this.key) && meta.hasCustomModelData()
				&& meta.getCustomModelData() == this.modelId;

	}
}
