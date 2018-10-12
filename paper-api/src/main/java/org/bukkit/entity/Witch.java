package org.bukkit.entity;

import com.destroystokyo.paper.entity.RangedEntity;

// Paper start
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
// Paper end

/**
 * Represents a Witch
 */
public interface Witch extends Raider, RangedEntity { // Paper

    /**
     * Gets whether the witch is drinking a potion
     *
     * @return whether the witch is drinking a potion
     */
    boolean isDrinkingPotion();

    // Paper start
    /**
     * Get time remaining (in ticks) the Witch is drinking a potion
     *
     * @return Time remaining (in ticks)
     */
    int getPotionUseTimeLeft();

    /**
     * Set time remaining (in ticks) that the Witch is drinking a potion.
     * <p>
     * This only has an effect while the Witch is drinking a potion.
     *
     * @param ticks Time in ticks remaining
     * @see #isDrinkingPotion
     */
    void setPotionUseTimeLeft(int ticks);

    /**
     * Get the potion the Witch is drinking
     *
     * @return The potion the witch is drinking
     */
    @org.jetbrains.annotations.NotNull
    ItemStack getDrinkingPotion();

    /**
     * Set the potion the Witch should drink
     *
     * @param potion Potion to drink
     */
    void setDrinkingPotion(@Nullable ItemStack potion);
    // Paper end
}
