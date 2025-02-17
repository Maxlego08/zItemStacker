package fr.maxlego08.zitemstacker.api;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public interface StackedItem {

    org.bukkit.entity.Item toBukkitEntity();

    UUID getUniqueId();

    int getAmount();

    void setAmount(int amount);

    boolean isSimilar(ItemStack itemStack);

    void setItemName();

    void add(int amount);

    void remove(int amount);

    boolean isValid();

    boolean give(Inventory inventory);

    long getCreatedAt();

    long getExpireAt();

    void remove();

    ItemStack getItemStack();
}
