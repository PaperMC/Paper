package org.bukkit.event.world;

import java.util.Collection;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.jspecify.annotations.Nullable;

/**
 * Called when a {@link LootTable} is generated in the world for an
 * {@link InventoryHolder}.
 * <p>
 * This event is NOT currently called when an entity's loot table has been
 * generated (use {@link EntityDeathEvent#getDrops()}), but WILL be called by
 * plugins invoking
 * {@link LootTable#fillInventory(org.bukkit.inventory.Inventory, java.util.Random, LootContext)}.
 */
public interface LootGenerateEvent extends WorldEvent, Cancellable {

    /**
     * Get the entity used as context for loot generation (if applicable).
     * <p>
     * For inventories where entities are not required to generate loot, such as
     * hoppers, {@code null} will be returned.
     * <br>
     * This is a convenience method for
     * {@code getLootContext().getLootedEntity()}.
     *
     * @return the entity
     */
    @Nullable Entity getEntity();

    /**
     * Get the inventory holder in which the loot was generated.
     * <p>
     * If the loot was generated as a result of the block being broken, the
     * inventory holder will be {@code null} as this event is called post block break.
     *
     * @return the inventory holder
     */
    @Nullable InventoryHolder getInventoryHolder();

    /**
     * Get the loot table used to generate loot.
     *
     * @return the loot table
     */
    LootTable getLootTable();

    /**
     * Get the loot context used to provide context to the loot table's loot
     * generation.
     *
     * @return the loot context
     */
    LootContext getLootContext();

    /**
     * Get a mutable list of all loot to be generated.
     * <p>
     * Any items added or removed from the returned list will be reflected in
     * the loot generation. {@code null} items will be treated as air.
     *
     * @return the loot to generate
     */
    List<ItemStack> getLoot();

    /**
     * Set the loot to be generated. {@code null} items will be treated as air.
     * <br>
     * Note: the set collection is not the one which will be returned by
     * {@link #getLoot()}.
     *
     * @param loot the loot to generate, {@code null} to clear all loot
     */
    void setLoot(@Nullable Collection<ItemStack> loot);

    /**
     * Check whether this event was called as a result of a plugin
     * invoking
     * {@link LootTable#fillInventory(org.bukkit.inventory.Inventory, java.util.Random, LootContext)}.
     *
     * @return {@code true} if plugin caused, {@code false} otherwise
     */
    boolean isPlugin();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
