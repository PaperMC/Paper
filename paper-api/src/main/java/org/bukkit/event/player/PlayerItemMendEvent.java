package org.bukkit.event.player;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents when a player has an item repaired via the Mending enchantment.
 * <br>
 * This event is fired directly before the {@link PlayerExpChangeEvent}, and the
 * results of this event directly affect the {@link PlayerExpChangeEvent}.
 */
public class PlayerItemMendEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    //
    private final ItemStack item;
    private final EquipmentSlot slot;
    private final ExperienceOrb experienceOrb;
    private int repairAmount;
    private boolean cancelled;

    public PlayerItemMendEvent(@NotNull Player who, @NotNull ItemStack item, @NotNull EquipmentSlot slot, @NotNull ExperienceOrb experienceOrb, int repairAmount) {
        super(who);
        this.item = item;
        this.slot = slot;
        this.experienceOrb = experienceOrb;
        this.repairAmount = repairAmount;
    }

    @Deprecated
    public PlayerItemMendEvent(@NotNull Player who, @NotNull ItemStack item, @NotNull ExperienceOrb experienceOrb, int repairAmount) {
        this(who, item, null, experienceOrb, repairAmount);
    }

    /**
     * Get the {@link ItemStack} to be repaired.
     *
     * This is not necessarily the item the player is holding.
     *
     * @return the item to be repaired
     */
    @NotNull
    public ItemStack getItem() {
        return item;
    }

    /**
     * Get the {@link EquipmentSlot} in which the repaired {@link ItemStack}
     * may be found.
     *
     * @return the repaired slot
     */
    @NotNull
    public EquipmentSlot getSlot() {
        return slot;
    }

    /**
     * Get the experience orb triggering the event.
     *
     * @return the experience orb
     */
    @NotNull
    public ExperienceOrb getExperienceOrb() {
        return experienceOrb;
    }

    /**
     * Get the amount the item is to be repaired.
     *
     * The default value is twice the value of the consumed experience orb
     * or the remaining damage left on the item, whichever is smaller.
     *
     * @return how much damage will be repaired by the experience orb
     */
    public int getRepairAmount() {
        return repairAmount;
    }

    /**
     * Set the amount the item will be repaired.
     *
     * Half of this value will be subtracted from the experience orb which initiated this event.
     *
     * @param amount how much damage will be repaired on the item
     */
    public void setRepairAmount(int amount) {
        this.repairAmount = amount;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
