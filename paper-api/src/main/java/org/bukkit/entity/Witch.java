package org.bukkit.entity;

import com.destroystokyo.paper.entity.RangedEntity;

// Paper start
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
// Paper end

/**
 * Represents a Witch
 *
 * @since 1.4.5 R1.0
 */
public interface Witch extends Raider, RangedEntity { // Paper

    /**
     * Gets whether the witch is drinking a potion
     *
     * @return whether the witch is drinking a potion
     * @since 1.13.1
     */
    boolean isDrinkingPotion();

    // Paper start
    /**
     * Get time remaining (in ticks) the Witch is drinking a potion
     *
     * @return Time remaining (in ticks)
     * @since 1.13.1
     */
    int getPotionUseTimeLeft();

    /**
     * Set time remaining (in ticks) that the Witch is drinking a potion.
     * <p>
     * This only has an effect while the Witch is drinking a potion.
     *
     * @param ticks Time in ticks remaining
     * @see #isDrinkingPotion
     * @since 1.16.5
     */
    void setPotionUseTimeLeft(int ticks);

    /**
     * Get the potion the Witch is drinking
     *
     * @return The potion the witch is drinking
     * @since 1.13.1
     */
    @org.jetbrains.annotations.NotNull
    ItemStack getDrinkingPotion();

    /**
     * Set the potion the Witch should drink
     *
     * @param potion Potion to drink
     * @since 1.13.1
     */
    void setDrinkingPotion(@Nullable ItemStack potion);
    // Paper end
}
