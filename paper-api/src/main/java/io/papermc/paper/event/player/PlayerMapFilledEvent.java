package io.papermc.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player creates a filled map by right-clicking an empty map.
 */
@NullMarked
public class PlayerMapFilledEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack originalItem;
    private ItemStack createdMap;

    @ApiStatus.Internal
    public PlayerMapFilledEvent(final Player player, final ItemStack originalItem, final ItemStack createdMap) {
        super(player);
        this.originalItem = originalItem;
        this.createdMap = createdMap;
    }

    /**
     * Returns a copy of the empty map before it was consumed.
     *
     * @return cloned original item
     */
    public ItemStack getOriginalItem() {
        return this.originalItem.clone();
    }

    /**
     * Returns a copy of the filled map which was created.
     *
     * @return cloned created map item
     */
    public ItemStack getCreatedMap() {
        return this.createdMap.clone();
    }

    /**
     * Sets the filled map that will be created.
     *
     * @param createdMap map item
     */
    public void setCreatedMap(final ItemStack createdMap) {
        this.createdMap = createdMap.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
