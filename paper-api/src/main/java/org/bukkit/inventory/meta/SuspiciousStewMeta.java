package org.bukkit.inventory.meta;

import java.util.List;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a suspicious stew that can have custom effects.
 */
public interface SuspiciousStewMeta extends ItemMeta {

    /**
     * Checks for the presence of custom potion effects.
     *
     * @return true if custom potion effects are applied
     */
    boolean hasCustomEffects();

    /**
     * Gets an immutable list containing all custom potion effects applied to
     * this suspicious stew.
     * <p>
     * Plugins should check that hasCustomEffects() returns true before calling
     * this method.
     *
     * @return the immutable list of custom potion effects
     */
    @NotNull
    List<PotionEffect> getCustomEffects();

    /**
     * Adds a custom potion effect to this suspicious stew.
     *
     * @deprecated use {@link #addCustomEffect(io.papermc.paper.potion.SuspiciousEffectEntry, boolean)} as PotionEffect suggests that all attributes are used. In fact, only the PotionEffectType and the duration are used.
     * @param effect the potion effect to add
     * @param overwrite true if any existing effect of the same type should be
     * overwritten
     * @return true if the suspicious stew meta changed as a result of this call
     */
    @Deprecated // Paper - add overloads to use suspicious effect entry to mushroom cow and suspicious stew meta
    boolean addCustomEffect(@NotNull PotionEffect effect, boolean overwrite);

    // Paper start - add overloads to use suspicious effect entry to mushroom cow and suspicious stew meta
    /**
     * Adds a custom potion effect to this suspicious stew.
     *
     * @param suspiciousEffectEntry the suspicious effect entry to add
     * @param overwrite true if any existing effect of the same type should be
     * overwritten
     * @return true if the suspicious stew meta changed as a result of this call
     * as a result of this call
     */
    boolean addCustomEffect(@NotNull io.papermc.paper.potion.SuspiciousEffectEntry suspiciousEffectEntry, boolean overwrite);
    // Paper end - add overloads to use suspicious effect entry to mushroom cow and suspicious stew meta

    /**
     * Removes a custom potion effect from this suspicious stew.
     *
     * @param type the potion effect type to remove
     * @return true if the suspicious stew meta changed as a result of this call
     */
    boolean removeCustomEffect(@NotNull PotionEffectType type);

    /**
     * Checks for a specific custom potion effect type on this suspicious stew.
     *
     * @param type the potion effect type to check for
     * @return true if the suspicious stew has this effect
     */
    boolean hasCustomEffect(@NotNull PotionEffectType type);

    /**
     * Removes all custom potion effects from this suspicious stew.
     *
     * @return true if the suspicious stew meta changed as a result of this call
     */
    boolean clearCustomEffects();

    @Override
    SuspiciousStewMeta clone();
}
