package org.bukkit.damage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a source of damage.
 */
@ApiStatus.Experimental
public interface DamageSource {

    /**
     * Get the {@link DamageType}.
     *
     * @return the damage type
     */
    @NotNull
    public DamageType getDamageType();

    /**
     * Get the {@link Entity} that caused the damage to occur.
     * <p>
     * Not to be confused with {@link #getDirectEntity()}, the causing entity is
     * the entity to which the damage is ultimately attributed if the receiver
     * is killed. If, for example, the receiver was damaged by a projectile, the
     * shooter/thrower would be returned.
     *
     * @return an Entity or null
     */
    @Nullable
    public Entity getCausingEntity();

    /**
     * Get the {@link Entity} that directly caused the damage.
     * <p>
     * Not to be confused with {@link #getCausingEntity()}, the direct entity is
     * the entity that actually inflicted the damage. If, for example, the
     * receiver was damaged by a projectile, the projectile would be returned.
     *
     * @return an Entity or null
     */
    @Nullable
    public Entity getDirectEntity();

    /**
     * Get the {@link Location} from where the damage originated. This will only
     * be present if an entity did not cause the damage.
     *
     * @return the location, or null if none
     */
    @Nullable
    public Location getDamageLocation();

    /**
     * Get the {@link Location} from where the damage originated.
     * <p>
     * This is a convenience method to get the final location of the damage.
     * This method will attempt to return
     * {@link #getDamageLocation() the damage location}. If this is null, the
     * {@link #getCausingEntity() causing entity location} will be returned.
     * Finally if there is no damage location nor a causing entity, null will be
     * returned.
     *
     * @return the source of the location or null.
     */
    @Nullable
    public Location getSourceLocation();

    /**
     * Get if this damage is indirect.
     * <p>
     * Damage is considered indirect if {@link #getCausingEntity()} is not equal
     * to {@link #getDirectEntity()}. This will be the case, for example, if a
     * skeleton shot an arrow or a player threw a potion.
     *
     * @return {@code true} if is indirect, {@code false} otherwise.
     */
    public boolean isIndirect();

    /**
     * Get the amount of hunger exhaustion caused by this damage.
     *
     * @return the amount of hunger exhaustion caused.
     */
    public float getFoodExhaustion();

    /**
     * Gets if this source of damage scales with difficulty.
     *
     * @return {@code True} if scales.
     */
    public boolean scalesWithDifficulty();

    /**
     * Create a new {@link DamageSource.Builder}.
     *
     * @param damageType the {@link DamageType} to use
     * @return a {@link DamageSource.Builder}
     */
    @NotNull
    @SuppressWarnings("deprecation")
    public static Builder builder(@NotNull DamageType damageType) {
        return Bukkit.getUnsafe().createDamageSourceBuilder(damageType);
    }

    /**
     * Utility class to make building a {@link DamageSource} easier. Only a
     * {@link DamageType} is required.
     */
    public static interface Builder {

        /**
         * Set the {@link Entity} that caused the damage.
         *
         * @param entity the entity
         * @return this instance. Allows for chained method calls
         * @see DamageSource#getCausingEntity()
         */
        @NotNull
        public Builder withCausingEntity(@NotNull Entity entity);

        /**
         * Set the {@link Entity} that directly inflicted the damage.
         *
         * @param entity the entity
         * @return this instance. Allows for chained method calls
         * @see DamageSource#getDirectEntity()
         */
        @NotNull
        public Builder withDirectEntity(@NotNull Entity entity);

        /**
         * Set the {@link Location} of the source of damage.
         *
         * @param location the location where the damage occurred
         * @return this instance. Allows for chained method calls
         * @see DamageSource#getSourceLocation()
         */
        @NotNull
        public Builder withDamageLocation(@NotNull Location location);

        /**
         * Create a new {@link DamageSource} instance using the supplied
         * parameters.
         *
         * @return the damage source instance
         */
        @NotNull
        public DamageSource build();
    }
}
