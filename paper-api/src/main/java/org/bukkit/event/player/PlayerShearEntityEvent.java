package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player shears an entity
 */
public class PlayerShearEntityEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel;
    private final Entity what;
    private final ItemStack item;
    private final EquipmentSlot hand;

    public PlayerShearEntityEvent(@NotNull Player who, @NotNull Entity what, @NotNull ItemStack item, @NotNull EquipmentSlot hand) {
        super(who);
        this.what = what;
        this.item = item;
        this.hand = hand;
    }

    @Deprecated
    public PlayerShearEntityEvent(@NotNull final Player who, @NotNull final Entity what) {
        this(who, what, new ItemStack(Material.SHEARS), EquipmentSlot.HAND);
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets the entity the player is shearing
     *
     * @return the entity the player is shearing
     */
    @NotNull
    public Entity getEntity() {
        return what;
    }

    /**
     * Gets the item used to shear the entity.
     *
     * @return the shears
     */
    @NotNull
    public ItemStack getItem() {
        return item.clone();
    }

    /**
     * Gets the hand used to shear the entity.
     *
     * @return the hand
     */
    @NotNull
    public EquipmentSlot getHand() {
        return hand;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
