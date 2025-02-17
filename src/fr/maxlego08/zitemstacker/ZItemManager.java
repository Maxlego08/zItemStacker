package fr.maxlego08.zitemstacker;

import fr.maxlego08.zitemstacker.api.ItemManager;
import fr.maxlego08.zitemstacker.api.StackedItem;
import fr.maxlego08.zitemstacker.save.Config;
import fr.maxlego08.zitemstacker.zcore.utils.ZUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ZItemManager extends ZUtils implements ItemManager {

    private final ItemStackerPlugin plugin;

    public ZItemManager(ItemStackerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public StackedItem getItem(Item item) {
        return new ZItem(this.plugin.getTranslationManager(), item);
    }

    @Override
    public int getItemAmount(Item item) {
        return getItem(item).getAmount();
    }

    @Override
    public void setAmount(Item item, int amount) {
        StackedItem stackedItem = getItem(item);
        stackedItem.setAmount(amount);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMerge(ItemMergeEvent event) {

        var entity = event.getEntity();
        if (Config.disableWorlds.contains(entity.getWorld().getName())) return;

        var target = event.getTarget();

        StackedItem currentItem = getItem(entity);
        StackedItem targetStackedItem = getItem(target);

        if (targetStackedItem.isSimilar(entity.getItemStack())) {

            targetStackedItem.add(currentItem.getAmount());
            currentItem.remove();

            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSpawn(ItemSpawnEvent event) {

        var entity = event.getEntity();
        if (Config.disableWorlds.contains(entity.getWorld().getName())) return;

        ItemStack itemStack = entity.getItemStack();

        Optional<StackedItem> optional = getNearbyItems(entity.getLocation(), itemStack);
        if (optional.isPresent()) {

            StackedItem item = optional.get();

            if (!item.isValid()) {
                return;
            }

            item.add(itemStack.getAmount());
            entity.remove();
        } else {

            getItem(entity);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickUp(EntityPickupItemEvent event) {

        var item = event.getItem();
        if (Config.disableWorlds.contains(item.getWorld().getName())) return;

        var entity = event.getEntity();
        var stackedItem = getItem(item);

        event.setCancelled(true);

        if (entity instanceof Player player) {

            Inventory inventory = player.getInventory();
            if (!stackedItem.give(inventory)) {
                stackedItem.remove();
                return;
            }

            /*if (Config.pickupSound != null && Config.enablePickupSound) {
                XSound sound = Config.pickupSound;
                sound.play(player);
            }*/

            if (stackedItem.getAmount() <= 0) {

                stackedItem.remove();
            }

        } else {

            if (Config.disableEntityPickUp) return;

            if (entity instanceof Villager villager) {

                Inventory inventory = villager.getInventory();

                if (!stackedItem.give(inventory)) {
                    stackedItem.remove();
                    return;
                }

                if (stackedItem.getAmount() <= 0) {
                    stackedItem.remove();
                }

            } else {

                EntityEquipment entityEquipment = entity.getEquipment();

                EquipmentSlot slot = this.getEquipmentSlot(entityEquipment, item.getItemStack().clone());

                if (slot == null && event.getRemaining() == 0) {

                    int maxAmount = Math.min(stackedItem.getAmount(), stackedItem.getItemStack().getMaxStackSize());

                    slot = EquipmentSlot.HAND;
                    ItemStack itemStack = item.getItemStack().clone();
                    itemStack.setAmount(maxAmount);

                    int newAmount = stackedItem.getAmount() - maxAmount;
                    if (newAmount > 0) {
                        stackedItem.remove(maxAmount);
                    } else {
                        stackedItem.remove();
                    }

                    entityEquipment.setItem(slot, itemStack);
                    entityEquipment.setItemInMainHandDropChance(2.0f);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDespawn(ItemDespawnEvent event) {

        if (Config.disableWorlds.contains(event.getEntity().getWorld().getName())) return;

        if (Config.disableItemDespawn) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryPickUp(InventoryPickupItemEvent event) {

        var target = event.getItem();
        if (Config.disableWorlds.contains(target.getWorld().getName())) return;

        var inventory = event.getInventory();

        event.setCancelled(true);

        StackedItem item = getItem(target);

        item.give(inventory);

        if (item.getAmount() <= 0) {
            item.remove();
        }
    }

    private Optional<StackedItem> getNearbyItems(Location location, ItemStack itemStack) {

        World world = location.getWorld();
        Optional<Entity> optional = world.getNearbyEntities(location, Config.distanceOnDrop, Config.distanceOnDrop, Config.distanceOnDrop).parallelStream().filter(entity -> entity instanceof Item && ((Item) entity).getItemStack().isSimilar(itemStack)).findFirst();

        return optional.map(entity -> getItem((Item) entity));
    }

    private EquipmentSlot getEquipmentSlot(EntityEquipment entityEquipment, ItemStack itemStack) {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack currentItemStack = entityEquipment.getItem(equipmentSlot);
            if (currentItemStack.isSimilar(itemStack)) {
                return equipmentSlot;
            }
        }
        return null;
    }
}
