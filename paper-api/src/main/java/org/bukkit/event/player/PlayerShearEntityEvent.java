package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import java.util.List;

/**
 * Called when a player shears an entity
 */
public class PlayerShearEntityEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Entity entity;
    private final ItemStack item;
    private final EquipmentSlot hand;
    private List<ItemStack> drops;

    private boolean cancelled;

    @ApiStatus.Internal
    public PlayerShearEntityEvent(@NotNull Player player, @NotNull Entity entity, @NotNull ItemStack item, @NotNull EquipmentSlot hand, final @NotNull List<ItemStack> drops) {
        super(player);
        this.entity = entity;
        this.item = item;
        this.hand = hand;
        this.drops = drops;
    }

    @ApiStatus.Internal
    @Deprecated(since = "1.15.2", forRemoval = true)
    public PlayerShearEntityEvent(@NotNull final Player player, @NotNull final Entity entity) {
        this(player, entity, new ItemStack(Material.SHEARS), EquipmentSlot.HAND, java.util.Collections.emptyList());
    }

    /**
     * Gets the entity the player is shearing
     *
     * @return the entity the player is shearing
     */
    @NotNull
    public Entity getEntity() {
        return this.entity;
    }

    /**
     * Gets the item used to shear the entity.
     *
     * @return the shears
     */
    @NotNull
    public ItemStack getItem() {
        return this.item.clone();
    }

    /**
     * Gets the hand used to shear the entity.
     *
     * @return the hand
     */
    @NotNull
    public EquipmentSlot getHand() {
        return this.hand;
    }

    /**
     * Get an immutable list of drops for this shearing.
     *
     * @return the shearing drops
     * @see #setDrops(java.util.List)
     */
    public @NotNull @Unmodifiable List<ItemStack> getDrops() {
        return this.drops;
    }

    /**
     * Sets the drops for the shearing.
     *
     * @param drops the shear drops
     */
    public void setDrops(final @NotNull List<ItemStack> drops) {
        this.drops = List.copyOf(drops);
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
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
