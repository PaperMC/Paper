package io.papermc.paper.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player tries to draw a bow or load a crossbow without having a suitable projectile item in their inventory
 */
@NullMarked
public class PlayerUseBowWithoutProjectileEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack weapon;
    private ItemStack projectileItem;

    @ApiStatus.Internal
    public PlayerUseBowWithoutProjectileEvent(final Player player, final ItemStack weapon) {
        super(player);
        this.weapon = weapon;
        this.projectileItem = ItemStack.empty();
    }

    /**
     * @return The weapon wich the player tries to use
     */
    public ItemStack getWeapon() {
        return weapon.clone();
    }

    /**
     * @return The projectile that should be used
     */
    public ItemStack getProjectileItem() {
        return projectileItem.clone();
    }

    /**
     * Sets the projectile that should be used
     * <p>
     * Note: setting this to {@link ItemStack#empty()} will prevent the player from using the bow/crossbow
     * 
     * @param projectile the projectile
     */
    public void setProjectileItem(ItemStack projectile) {
        this.projectileItem = projectile.clone();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
