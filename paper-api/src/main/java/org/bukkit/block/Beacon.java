package org.bukkit.block;

import java.util.Collection;
import org.bukkit.Nameable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a captured state of a beacon.
 *
 * @since 1.5.1 R0.2
 */
public interface Beacon extends io.papermc.paper.block.LockableTileState { // Paper

    /**
     * Returns the list of players within the beacon's range of effect.
     * <p>
     * This will return an empty list if the block represented by this state is
     * no longer a beacon.
     *
     * @return the players in range
     * @throws IllegalStateException if this block state is not placed
     * @since 1.10.2
     */
    @NotNull
    Collection<LivingEntity> getEntitiesInRange();

    /**
     * Returns the tier of the beacon pyramid (0-4). The tier refers to the
     * beacon's power level, based on how many layers of blocks are in the
     * pyramid. Tier 1 refers to a beacon with one layer of 9 blocks under it.
     *
     * @return the beacon tier
     * @since 1.10.2
     */
    int getTier();

    /**
     * Returns the primary effect set on the beacon
     *
     * @return the primary effect or null if not set
     * @since 1.10.2
     */
    @Nullable
    PotionEffect getPrimaryEffect();

    /**
     * Set the primary effect on this beacon, or null to clear.
     *
     * @param effect new primary effect
     * @since 1.10.2
     */
    void setPrimaryEffect(@Nullable PotionEffectType effect);

    /**
     * Returns the secondary effect set on the beacon.
     *
     * @return the secondary effect or null if no secondary effect
     * @since 1.10.2
     */
    @Nullable
    PotionEffect getSecondaryEffect();

    /**
     * Set the secondary effect on this beacon, or null to clear. Note that tier
     * must be &gt;= 4 for this effect to be active.
     *
     * @param effect desired secondary effect
     * @since 1.10.2
     */
    void setSecondaryEffect(@Nullable PotionEffectType effect);

    // Paper start - Custom effect ranges
    /**
     * Gets the effect range of this beacon.
     * A negative range value means the beacon is using its default range based on tier.
     * @return Either the custom range set with {@link #setEffectRange(double)} or the range based on the beacon tier.
     * @since 1.16.4
     */
    double getEffectRange();

    /**
     * Sets the effect range of the beacon
     * A negative range value means the beacon is using its default range based on tier.
     * @param range Radius of effect range.
     * @since 1.16.4
     */
    void setEffectRange(double range);

    /**
     * Resets the custom range from this beacon and falls back to the range based on the beacon tier.
     * Shortcut for setting the effect range to a negative number.
     *
     * @since 1.16.4
     */
    void resetEffectRange();
    // Paper end
}
