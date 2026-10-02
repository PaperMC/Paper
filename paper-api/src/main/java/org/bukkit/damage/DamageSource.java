package org.bukkit.damage;

import io.papermc.paper.InternalAPIBridge;
import net.kyori.adventure.pointer.Pointers;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

/// Represents a source of damage.
public interface DamageSource {

    /// Get the [DamageType].
    ///
    /// @return the damage type
    @NotNull
    public DamageType getDamageType();

    /// Get the [Entity] that caused the damage to occur.
    ///
    /// Not to be confused with [#getDirectEntity()], the causing entity is
    /// the entity to which the damage is ultimately attributed if the receiver
    /// is killed. If, for example, the receiver was damaged by a projectile, the
    /// shooter/thrower would be returned.
    ///
    /// @return an Entity or null
    @Nullable
    public Entity getCausingEntity();

    /// Get the [Entity] that directly caused the damage.
    ///
    /// Not to be confused with [#getCausingEntity()], the direct entity is
    /// the entity that actually inflicted the damage. If, for example, the
    /// receiver was damaged by a projectile, the projectile would be returned.
    ///
    /// @return an Entity or null
    @Nullable
    public Entity getDirectEntity();

    /// Get the [Location] from where the damage originated. This will only
    /// be present if an entity did not cause the damage.
    ///
    /// @return the location, or null if none
    /// @apiNote the world of the location might be null for positioned-only damage source
    /// not caused by any entity
    @Nullable
    public Location getDamageLocation();

    /// Get the [Location] from where the damage originated.
    ///
    /// This is a convenience method to get the final location of the damage.
    /// This method will attempt to return
    /// [`the damage location`][#getDamageLocation()]. If this is null, the
    /// [`causing entity location`][#getCausingEntity()] will be returned.
    /// Finally if there is no damage location nor a causing entity, null will be
    /// returned.
    ///
    /// @return the source of the location or null.
    /// @apiNote the world of the location might be null for positioned-only damage source
    /// not caused by any entity
    @Nullable
    public Location getSourceLocation();

    /// Get if this damage is indirect.
    ///
    /// Damage is considered indirect if [#getCausingEntity()] is not equal
    /// to [#getDirectEntity()]. This will be the case, for example, if a
    /// skeleton shot an arrow or a player threw a potion.
    ///
    /// @return `true` if is indirect, `false` otherwise.
    public boolean isIndirect();

    /// Get the amount of hunger exhaustion caused by this damage.
    ///
    /// @return the amount of hunger exhaustion caused.
    public float getFoodExhaustion();

    /// Gets if this source of damage scales with difficulty.
    ///
    /// @return `True` if scales.
    public boolean scalesWithDifficulty();

    /// Gets the [Pointers] used for plugin-provided damage context.
    ///
    /// @return the damage context
    @ApiStatus.Experimental
    @NotNull
    public Pointers getDamageContext();

    /// Create a new [DamageSource.Builder].
    ///
    /// @param damageType the [DamageType] to use
    /// @return a [DamageSource.Builder]
    @NotNull
    public static Builder builder(@NotNull DamageType damageType) {
        return InternalAPIBridge.get().createDamageSourceBuilder(damageType);
    }

    /// Utility class to make building a [DamageSource] easier. Only a
    /// [DamageType] is required.
    public static interface Builder {

        /// Set the [Entity] that caused the damage.
        ///
        /// @param entity the entity
        /// @return this instance. Allows for chained method calls
        /// @see DamageSource#getCausingEntity()
        @NotNull
        public Builder withCausingEntity(@NotNull Entity entity);

        /// Set the [Entity] that directly inflicted the damage.
        ///
        /// @param entity the entity
        /// @return this instance. Allows for chained method calls
        /// @see DamageSource#getDirectEntity()
        @NotNull
        public Builder withDirectEntity(@NotNull Entity entity);

        /// Set the [Location] of the source of damage.
        ///
        /// @param location the location where the damage occurred
        /// @return this instance. Allows for chained method calls
        /// @see DamageSource#getSourceLocation()
        @NotNull
        public Builder withDamageLocation(@NotNull Location location);

        /// Configures a builder for the [net.kyori.adventure.pointer.Pointers] used for plugin-provided damage context.
        ///
        /// @param consumer a consumer
        /// @return this instance. Allows for chained method calls
        /// @see DamageSource#getDamageContext()
        @ApiStatus.Experimental
        @NotNull
        public Builder withDamageContext(@NotNull Consumer<Pointers.Builder> consumer);

        /// Create a new [DamageSource] instance using the supplied
        /// parameters.
        ///
        /// @return the damage source instance
        @NotNull
        public DamageSource build();
    }
}
