package io.papermc.paper.event.server;

import java.util.List;
import java.util.Objects;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a {@link LootTable} is rolled to generate loot.
 * <p>
 * This event allows plugins to inspect or modify the generated loot for a specific {@link LootContext}.
 */
@NullMarked
public final class LootTableRollEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final LootTable lootTable;
    private final LootContext lootContext;
    private List<ItemStack> loot;

    @ApiStatus.Internal
    public LootTableRollEvent(final LootTable lootTable, final LootContext lootContext, final List<ItemStack> loot) {
        this.lootTable = lootTable;
        this.lootContext = lootContext;
        this.loot = loot;
    }

    /**
     * Gets the loot table used to generate the initial loot.
     *
     * @return the loot table used
     */
    public LootTable getLootTable() {
        return this.lootTable;
    }

    /**
     * Gets the context in which the loot table was rolled.
     *
     * @return the loot context for the loot table roll
     */
    public LootContext getLootContext() {
        return this.lootContext;
    }

    /**
     * Gets the current unmodifiable list of items that will become loot.
     * <p>
     * To modify this list, use {@link #setLoot(List)}.
     *
     * @return the unmodifiable list of items
     */
    public List<ItemStack> getLoot() {
        return this.loot;
    }

    /**
     * Sets the list of items that will become loot.
     * <p>
     * A defensive copy of the provided list is used, so any later
     * changes to the original list will not affect the loot.
     *
     * @param loot the new list of {@link ItemStack}s
     * @throws NullPointerException if the provided loot list is null
     */
    public void setLoot(final List<ItemStack> loot) throws NullPointerException {
        Objects.requireNonNull(loot, "loot");
        this.loot = List.copyOf(loot);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
