package org.bukkit.entity;

import java.util.Collection;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * Represents a thrown potion bottle
 */
public interface ThrownPotion extends Projectile {

    /**
     * Returns the effects that are applied by this potion.
     *
     * @return The potion effects
     */
    public Collection<PotionEffect> getEffects();

    /**
     * Returns a copy of the ItemStack for this thrown potion.
     * <p>
     * Altering this copy will not alter the thrown potion directly. If you
     * want to alter the thrown potion, you must use the {@link
     * #setItem(ItemStack) setItemStack} method.
     *
     * @return A copy of the ItemStack for this thrown potion.
     */
    public ItemStack getItem();

    /**
     * Set the ItemStack for this thrown potion.
     * <p>
     * The ItemStack must be a potion, otherwise an exception is thrown.
     *
     * @param item New ItemStack
     */
    public void setItem(ItemStack item);
}
