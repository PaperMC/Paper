package org.bukkit.event.player;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Represents when a player has an item repaired via the Mending enchantment.
 * <br>
 * This event is fired directly before the {@link PlayerExpChangeEvent}, and the
 * results of this event directly affect the {@link PlayerExpChangeEvent}.
 */
public class PlayerItemMendEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack item;
    private final EquipmentSlot slot;
    private final ExperienceOrb experienceOrb;
    private final int consumedExperience;
    private int repairAmount;

    private boolean cancelled;

    @ApiStatus.Internal
    @Deprecated(forRemoval = true)
    public PlayerItemMendEvent(@NotNull Player player, @NotNull ItemStack item, @NotNull EquipmentSlot slot, @NotNull ExperienceOrb experienceOrb, int repairAmount) {
        this(player, item, slot, experienceOrb, repairAmount, repairAmount / 2);
    }

    @ApiStatus.Internal
    public PlayerItemMendEvent(@NotNull Player player, @NotNull ItemStack item, @NotNull EquipmentSlot slot, @NotNull ExperienceOrb experienceOrb, int repairAmount, int consumedExperience) {
        super(player);
        this.item = item;
        this.slot = slot;
        this.experienceOrb = experienceOrb;
        this.repairAmount = repairAmount;
        this.consumedExperience = consumedExperience;
    }

    @Deprecated(since = "1.19.2", forRemoval = true)
    public PlayerItemMendEvent(@NotNull Player player, @NotNull ItemStack item, @NotNull ExperienceOrb experienceOrb, int repairAmount) {
        this(player, item, null, experienceOrb, repairAmount);
    }

    /**
     * Get the {@link ItemStack} to be repaired.
     * <br>
     * This is not necessarily the item the player is holding.
     *
     * @return the item to be repaired
     */
    @NotNull
    public ItemStack getItem() {
        return this.item;
    }

    /**
     * Get the {@link EquipmentSlot} in which the repaired {@link ItemStack}
     * may be found.
     *
     * @return the repaired slot
     */
    @NotNull
    public EquipmentSlot getSlot() {
        return this.slot;
    }

    /**
     * Get the experience orb triggering the event.
     *
     * @return the experience orb
     */
    @NotNull
    public ExperienceOrb getExperienceOrb() {
        return this.experienceOrb;
    }

    /**
     * Get the amount the item is to be repaired.
     * <p>
     * The default value is twice the value of the consumed experience orb
     * or the remaining damage left on the item, whichever is smaller.
     *
     * @return how much damage will be repaired by the experience orb
     */
    public int getRepairAmount() {
        return this.repairAmount;
    }

    /**
     * Set the amount the item will be repaired.
     * <br>
     * Half of this value will be subtracted from the experience orb which initiated this event.
     *
     * @param amount how much damage will be repaired on the item
     */
    public void setRepairAmount(int amount) {
        this.repairAmount = amount;
    }

    /**
     * Helper method to get the amount of experience that will be consumed.
     * This method just returns the result of inputting {@link #getRepairAmount()}
     * into the function {@link #getDurabilityToXpOperation()}.
     *
     * @return the amount of xp that will be consumed
     */
    public int getConsumedExperience() {
        return this.consumedExperience;
    }

    /**
     * Get the operation used to calculate xp used based on
     * the set repair amount. Used to calculate how much of
     * an XP orb will be consumed by this mend operation.
     *
     * @return the durability-to-xp operation
     * @deprecated the mending enchantment uses enchantment effects to compute how much durability is granted per xp.
     * The enchantment effects operation are too complex to reliably offer the inverse function.
     */
    @Contract("-> fail")
    @Deprecated(forRemoval = true, since = "1.21")
    public @NotNull java.util.function.IntUnaryOperator getDurabilityToXpOperation() {
        throw new UnsupportedOperationException("Enchantments use effects to compute xp to durability since 1.21.");
    }

    /**
     * Sets the operation used to calculate xp used based on
     * the set repair amount. Used to calculate how much of
     * an XP orb will be consumed by this mend operation.
     *
     * @param durabilityToXpOp the durability-to-xp operation
     * @deprecated the mending enchantment uses enchantment effects to compute how much durability is granted per xp.
     * The enchantment effects operation are too complex to reliably offer the inverse function.
     */
    @Contract("_ -> fail")
    @Deprecated(forRemoval = true, since = "1.21")
    public void setDurabilityToXpOperation(@NotNull java.util.function.IntUnaryOperator durabilityToXpOp) {
        throw new UnsupportedOperationException("Enchantments use effects to compute xp to durability since 1.21.");
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
