package fr.maxlego08.zitemstacker.api;

import org.bukkit.entity.Item;
import org.bukkit.event.Listener;

public interface ItemManager extends Listener {

    StackedItem getItem(Item item);

    int getItemAmount(Item item);

    void setAmount(Item item, int amount);

}
