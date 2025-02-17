package fr.maxlego08.zitemstacker;

import fr.maxlego08.zitemstacker.api.StackedItem;
import fr.maxlego08.zitemstacker.api.TranslationManager;
import fr.maxlego08.zitemstacker.save.Config;
import fr.maxlego08.zitemstacker.zcore.utils.ZUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class ZItem extends ZUtils implements StackedItem {

    public static final NamespacedKey AMOUNT_KEY = new NamespacedKey("zitemstacker", "amount");

    private final TranslationManager translationManager;
    private final UUID uniqueId;
    private final long createdAt;
    private final long expireAt;
    private final Item item;
    private int amount;

    public ZItem(TranslationManager translationManager, Item item) {

        this.translationManager = translationManager;
        this.item = item;

        this.createdAt = System.currentTimeMillis();
        this.expireAt = System.currentTimeMillis() + (1000 * Config.expireItemSeconds);
        this.uniqueId = item.getUniqueId();

        var data = item.getPersistentDataContainer();

        this.amount = data.getOrDefault(AMOUNT_KEY, PersistentDataType.INTEGER, item.getItemStack().getAmount());
        data.set(AMOUNT_KEY, PersistentDataType.INTEGER, this.amount);

        this.item.getItemStack().setAmount(1);
        setItemName();
    }

    @Override
    public Item toBukkitEntity() {
        return this.item;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
        item.getPersistentDataContainer().set(AMOUNT_KEY, PersistentDataType.INTEGER, amount);
        setItemName();
    }

    @Override
    public boolean isSimilar(ItemStack itemStack) {
        return itemStack != null && isValid() && item.getItemStack().isSimilar(itemStack);
    }

    @Override
    public void setItemName() {

        if (this.amount <= 0) {
            remove();
            return;
        }

        String name = Config.itemName.replace("%amount%", String.valueOf(this.amount));
        name = name.replace("%name%", translationManager.translateItemStack(item.getItemStack()));
        item.setCustomNameVisible(true);
        item.setCustomName(color(name));
    }

    @Override
    public void add(int amount) {
        setAmount(this.getAmount() + amount);
    }

    @Override
    public void remove(int amount) {
        setAmount(this.getAmount() - amount);
    }

    @Override
    public boolean isValid() {
        return this.expireAt > System.currentTimeMillis();
    }

    @Override
    public boolean give(Inventory inventory) {

        int inventorySize = inventory.getType().equals(InventoryType.HOPPER) ? 5 : 36;
        ItemStack itemStack = this.item.getItemStack();
        for (int a = 0; a != inventorySize; a++) {

            if (this.amount <= 0) {
                return true;
            }

            ItemStack currentItem = inventory.getItem(a);

            if (currentItem == null) {

                int newAmount = Math.min(itemStack.getMaxStackSize(), this.amount);
                this.amount -= newAmount;
                item.getPersistentDataContainer().set(AMOUNT_KEY, PersistentDataType.INTEGER, this.amount);

                ItemStack newItemStack = itemStack.clone();
                newItemStack.setAmount(newAmount);

                inventory.addItem(newItemStack);

            } else if (itemStack.isSimilar(currentItem) && currentItem.getAmount() < currentItem.getMaxStackSize()) {

                int freeAmount = currentItem.getMaxStackSize() - currentItem.getAmount();
                int newAmount = Math.min(freeAmount, this.amount);

                this.amount -= newAmount;
                item.getPersistentDataContainer().set(AMOUNT_KEY, PersistentDataType.INTEGER, this.amount);

                currentItem.setAmount(currentItem.getAmount() + newAmount);
            }

            if (this.amount <= 0) {
                return true;
            }
        }

        setItemName();
        return true;
    }

    @Override
    public long getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public long getExpireAt() {
        return this.expireAt;
    }

    @Override
    public void remove() {
        item.remove();
    }

    @Override
    public ItemStack getItemStack() {
        return item.getItemStack();
    }

    @Override
    public String toString() {
        return "ZItem{" +
                "uniqueId=" + uniqueId +
                ", createdAt=" + createdAt +
                ", expireAt=" + expireAt +
                ", item=" + item +
                ", amount=" + amount +
                '}';
    }
}
