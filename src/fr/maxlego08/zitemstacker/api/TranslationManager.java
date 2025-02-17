package fr.maxlego08.zitemstacker.api;

import org.bukkit.inventory.ItemStack;

public interface TranslationManager {

    String translateItemStack(ItemStack itemStack);

    String replaceValue(String searchValue);

    void loadTranslations();

}
