package io.papermc.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player tries to draw a bow or load a crossbow without having a suitable projectile in their inventory
 */
@NullMarked
public class PlayerUseBowWithoutProjectileEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack item;
    private ItemStack projectile;

    @ApiStatus.Internal
    public PlayerUseBowWithoutProjectileEvent(final Player player, final ItemStack item) {
        super(player);
        this.item = item;
        this.projectile = ItemStack.empty();
    }

    /**
     *  Gets the item which the player tries to use
     *
     * @return the item
     */
    public ItemStack getItem() {
        return item.clone();
    }

    /**
     * Gets the projectile that should be used
     * 
     * @return the projectile
     */
    public ItemStack getProjectile() {
        return projectile.clone();
    }

    /**
     * Sets the projectile that should be used
     * <p>
     * Note: setting this to {@link ItemStack#empty()} will prevent the player from using the bow/crossbow
     * 
     * @param projectile the projectile
     */
    public void setProjectile(ItemStack projectile) {
        this.projectile = projectile.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
