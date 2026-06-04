package io.papermc.paper.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;
import java.util.Collection;
import java.util.List;

/**
 * Called when a {@link LootTable} is generated for an {@link Entity}.
 * <br>
 * For example: When an entity dies, an armadillo sheds, etc.
 */
@NullMarked
public class EntityLootGenerateEvent extends EntityEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LootTable lootTable;
    private final LootContext lootContext;
    private final List<ItemStack> loot;

    private boolean cancelled;

    @ApiStatus.Internal
    public EntityLootGenerateEvent(Entity entity, LootTable lootTable, LootContext lootContext, List<ItemStack> loot) {
        super(entity);
        this.lootTable = lootTable;
        this.lootContext = lootContext;
        this.loot = loot;
    }

    /**
     * Get the loot table used to generate loot.
     *
     * @return the loot table
     */
    public LootTable getLootTable() {
        return this.lootTable;
    }

    /**
     * Get the loot context used to provide context to the loot table's loot
     * generation.
     *
     * @return the loot context
     */
    public LootContext getLootContext() {
        return this.lootContext;
    }

    /**
     * Set the loot to be generated. {@code null} items will be treated as air.
     * <br>
     * Note: the set collection is not the one which will be returned by
     * {@link #getLoot()}.
     *
     * @param loot the loot to generate, {@code null} to clear all loot
     */
    public void setLoot(@Nullable Collection<ItemStack> loot) {
        this.loot.clear();
        if (loot != null) {
            this.loot.addAll(loot);
        }
    }

    /**
     * Get a mutable list of all loot to be generated.
     * <p>
     * Any items added or removed from the returned list will be reflected in
     * the loot generation. {@code null} items will be treated as air.
     *
     * @return the loot to generate
     */
    public List<ItemStack> getLoot() {
        return this.loot;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
