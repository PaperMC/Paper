package org.bukkit.entity;

import java.util.Collection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a thrown potion bottle
 */
public interface ThrownPotion extends ThrowableProjectile {

    /**
     * Returns the effects that are applied by this potion.
     *
     * @return The potion effects
     */
    @NotNull
    public Collection<PotionEffect> getEffects();

    /**
     * Returns a copy of the ItemStack for this thrown potion.
     * <p>
     * Altering this copy will not alter the thrown potion directly. If you want
     * to alter the thrown potion, you must use the {@link
     * #setItem(ItemStack) setItemStack} method.
     *
     * @return A copy of the ItemStack for this thrown potion.
     */
    @NotNull
    public ItemStack getItem();

    /**
     * Set the ItemStack for this thrown potion.
     *
     * @param item New ItemStack
     */
    public void setItem(@NotNull ItemStack item);

    // Paper start - Projectile API
    /**
     * Gets a copy of the PotionMeta for this thrown potion.
     * This includes what effects will be applied by this potion.
     *
     * @return potion meta
     */
    @NotNull
    org.bukkit.inventory.meta.PotionMeta getPotionMeta();

    /**
     * Sets the PotionMeta of this thrown potion.
     * This will modify the effects applied by this potion.
     * <p>
     * Note that the type of {@link #getItem()} is irrelevant
     *
     * @param meta potion meta
     */
    void setPotionMeta(@NotNull org.bukkit.inventory.meta.PotionMeta meta);

    /**
     * Splashes the potion at its current location.
     */
    void splash();
    // Paper end
}
