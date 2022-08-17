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
     * <p>
     * The ItemStack must be of type {@link org.bukkit.Material#SPLASH_POTION}
     * or {@link org.bukkit.Material#LINGERING_POTION}, otherwise an exception
     * is thrown.
     *
     * @param item New ItemStack
     */
    public void setItem(@NotNull ItemStack item);
}
