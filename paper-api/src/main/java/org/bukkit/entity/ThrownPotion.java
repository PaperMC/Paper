package org.bukkit.entity;

import io.papermc.paper.datacomponent.DataComponentType;
import java.util.Collection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a thrown potion bottle
 */
@NullMarked
public interface ThrownPotion extends ThrowableProjectile {

    /**
     * Returns the effects that are applied by this potion.
     *
     * @return The potion effects
     */
    Collection<PotionEffect> getEffects();

    /**
     * Returns a copy of the ItemStack for this thrown potion.
     * <p>
     * Altering this copy will not alter the thrown potion directly. If you want
     * to alter the thrown potion, you must use the {@link
     * #setItem(ItemStack) setItemStack} method.
     *
     * @return A copy of the ItemStack for this thrown potion.
     */
    ItemStack getItem();

    /**
     * Set the ItemStack for this thrown potion.
     *
     * @param item New ItemStack
     */
     void setItem(ItemStack item);

    /**
     * Gets a copy of the PotionMeta for this thrown potion.
     * This includes what effects will be applied by this potion.
     *
     * @return potion meta
     * @apiNote obsolete in favor of {@link #getItem()} with {@link ItemStack#getData(DataComponentType.Valued)} like {@link io.papermc.paper.datacomponent.DataComponentTypes#POTION_CONTENTS}
     */
    @ApiStatus.Obsolete
    PotionMeta getPotionMeta();

    /**
     * Sets the PotionMeta of this thrown potion.
     * This will modify the effects applied by this potion.
     * <p>
     * Note that the type of {@link #getItem()} is irrelevant
     *
     * @param meta potion meta
     * @apiNote obsolete in favor of {@link #setItem(ItemStack)} with {@link io.papermc.paper.datacomponent.DataComponentTypes#POTION_CONTENTS}
     */
    @ApiStatus.Obsolete
    void setPotionMeta(PotionMeta meta);

    /**
     * Splashes the potion at its current location.
     */
    void splash();
}
