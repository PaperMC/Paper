package io.papermc.paper.event.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player picks up item from result slot in grindstone
 */
public class GrindstoneDisenchantEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final ItemStack firstItem;
    private final ItemStack secondItem;
    private final ItemStack resultItem;
    private int experience;
    private boolean cancelled;

    public GrindstoneDisenchantEvent(@NotNull Player player, ItemStack firstItem, ItemStack secondItem, @NotNull ItemStack resultItem, int experience) {
        super(player);
        this.firstItem = firstItem;
        this.secondItem = secondItem;
        this.resultItem = resultItem;
        this.experience = experience;
    }

    public ItemStack getFirstItem() {
        return firstItem;
    }

    public ItemStack getSecondItem() {
        return secondItem;
    }

    @NotNull
    public ItemStack getResultItem() {
        return this.resultItem;
    }

    public int getExperience() {
        return this.experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
