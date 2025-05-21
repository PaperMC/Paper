package io.papermc.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player creates a filled map by right-clicking an empty map
 */
@NullMarked
public class PlayerMapFilledEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack emptyMap;
    private final ItemStack createdMap;

    @ApiStatus.Internal
    public PlayerMapFilledEvent(final @NotNull Player player, final ItemStack emptyMap, final ItemStack createdMap) {
        super(player);
        this.emptyMap = emptyMap;
        this.createdMap = createdMap;
    }

    /**
     * Returns a copy of the empty map before it was consumed
     */
    public ItemStack getEmptyMap() {
        return this.emptyMap;
    }

    /**
     * Returns the filled map which was created
     */
    public ItemStack getCreatedMap() {
        return this.createdMap;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
