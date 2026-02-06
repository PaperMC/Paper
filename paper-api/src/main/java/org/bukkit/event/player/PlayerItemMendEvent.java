package org.bukkit.event.player;

import java.util.function.IntUnaryOperator;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

/**
 * Represents when a player has an item repaired via the Mending enchantment.
 * <br>
 * This event is fired directly before the {@link PlayerExpChangeEvent}, and the
 * results of this event directly affect the {@link PlayerExpChangeEvent}.
 */
public interface PlayerItemMendEvent extends PlayerEvent, Cancellable {

    /**
     * Get the {@link ItemStack} to be repaired.
     * <br>
     * This is not necessarily the item the player is holding.
     *
     * @return the item to be repaired
     */
    ItemStack getItem();

    /**
     * Get the {@link EquipmentSlot} in which the repaired {@link ItemStack}
     * may be found.
     *
     * @return the repaired slot
     */
    EquipmentSlot getSlot();

    /**
     * Get the experience orb triggering the event.
     *
     * @return the experience orb
     */
    ExperienceOrb getExperienceOrb();

    /**
     * Get the amount the item is to be repaired.
     * <p>
     * The default value is twice the value of the consumed experience orb
     * or the remaining damage left on the item, whichever is smaller.
     *
     * @return how much damage will be repaired by the experience orb
     */
    int getRepairAmount();

    /**
     * Set the amount the item will be repaired.
     * <br>
     * Half of this value will be subtracted from the experience orb which initiated this event.
     *
     * @param amount how much damage will be repaired on the item
     */
    void setRepairAmount(int amount);

    /**
     * Helper method to get the amount of experience that will be consumed.
     * This method just returns the result of inputting {@link #getRepairAmount()}
     * into the function {@link #getDurabilityToXpOperation()}.
     *
     * @return the amount of xp that will be consumed
     */
    int getConsumedExperience();

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
    default IntUnaryOperator getDurabilityToXpOperation() {
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
    default void setDurabilityToXpOperation(IntUnaryOperator durabilityToXpOp) {
        throw new UnsupportedOperationException("Enchantments use effects to compute xp to durability since 1.21.");
    }

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
