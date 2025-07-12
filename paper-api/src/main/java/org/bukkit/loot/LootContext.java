package org.bukkit.loot;

import com.google.common.base.Preconditions;
import io.papermc.paper.loot.LootContextKey;
import io.papermc.paper.math.Position;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents additional information a {@link LootTable} can use to modify it's
 * generated loot.
 */
@NullMarked
public final class LootContext {

    public static final int DEFAULT_LOOT_MODIFIER = -1;

    // Paper start - loot context overhaul
    private final org.bukkit.World world;
    private final float luck;
    private final java.util.Random random;
    private final Map<LootContextKey<?>, Object> contextMap;
    // TODO dynamic drops API
    @Deprecated
    private @Nullable Location legacyLocation;
    @Deprecated // has no functionality
    private int lootingModifier;
    private final boolean isLegacy;

    private LootContext(World world, float luck, Random random, Map<LootContextKey<?>, Object> contextMap, boolean isLegacy, @Deprecated int lootingModifier) {
        this.world = world;
        this.luck = luck;
        this.random = random;
        this.contextMap = java.util.Map.copyOf(contextMap);
        this.isLegacy = isLegacy;
        this.lootingModifier = lootingModifier;
    }

    @org.jetbrains.annotations.ApiStatus.Internal
    public boolean isLegacy() {
        return this.isLegacy;
    }

    /**
     * Checks if this context contains a value for the key
     *
     * @param contextKey the key to check
     * @return true if this context has a value for that key
     */
    public boolean hasKey(final LootContextKey<?> contextKey) {
        return this.contextMap.containsKey(contextKey);
    }

    /**
     * Gets the value for a context key
     *
     * @param contextKey the key for the value
     * @return the value or null if this context doesn't have a value for the key
     * @param <T> value type
     * @see #hasKey(LootContextKey)
     * @see #getOrThrow(LootContextKey)
     */
    @SuppressWarnings("unchecked")
    public <T> @Nullable T get(final LootContextKey<T> contextKey) {
        return (T) this.contextMap.get(contextKey);
    }

    /**
     * Gets the value of a context key, throwing an exception
     * if one isn't found
     *
     * @param contextKey the key for the value
     * @return the value
     * @param <T> value type
     * @throws NoSuchElementException if no value is found for that key
     */
    public <T> T getOrThrow(final LootContextKey<T> contextKey) {
        final T value = this.get(contextKey);
        if (value == null) {
            throw new NoSuchElementException("No value found for " + contextKey);
        }
        return value;
    }

    /**
     * Gets the random instance used for this context.
     *
     * @return the random
     */
    public Random getRandom() {
        return this.random;
    }

    /**
     * Gets the world for this context.
     *
     * @return the world
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Gets the context map for this loot context.
     *
     * @return an unmodifiable map
     */

    public @Unmodifiable Map<LootContextKey<?>, Object> getContextMap() {
        return this.contextMap;
    }
    // Paper end

    /**
     * The {@link Location} to store where the loot will be generated.
     *
     * @return the Location of where the loot will be generated
     * @deprecated use {@link #get(LootContextKey)} methods
     */
    @Deprecated // Paper
    public Location getLocation() {
        // Paper start - fallback to legacy location
        if (this.legacyLocation == null) {
            if (this.contextMap.containsKey(LootContextKey.ORIGIN)) {
                Position pos = this.getOrThrow(LootContextKey.ORIGIN);
                this.legacyLocation = new Location(this.world, pos.x(), pos.y(), pos.z());
            } else if (this.contextMap.containsKey(LootContextKey.THIS_ENTITY)) {
                this.legacyLocation = this.getOrThrow(LootContextKey.THIS_ENTITY).getLocation();
            } else {
                throw new IllegalStateException("All known context key sets require \"origin\" or \"this_entity\" and this one doesn't have either");
            }
        }
        return this.legacyLocation.clone();
        // Paper end
    }

    /**
     * Represents the {@link org.bukkit.potion.PotionEffectType#LUCK} that an
     * entity can have. The higher the value the better chance of receiving more
     * loot.
     *
     * @return luck
     */
    public float getLuck() {
        return this.luck;
    }

    /**
     * Represents the
     * {@link org.bukkit.enchantments.Enchantment#LOOTING} the
     * {@link #getKiller()} entity has on their equipped item.
     * <p>
     * This value is only set via
     * {@link LootContext.Builder#lootingModifier(int)}. If not set, the
     * {@link #getKiller()} entity's looting level will be used instead.
     *
     * @return the looting level
     * @deprecated no longer functional
     */
    @Deprecated(since = "1.21", forRemoval = true)
    public int getLootingModifier() {
        return this.lootingModifier;
    }

    /**
     * Get the {@link Entity} that was killed. Can be null.
     *
     * @return the looted entity or null
     * @deprecated use {@link #get(LootContextKey)} methods
     */
    @Deprecated // Paper
    public @Nullable Entity getLootedEntity() {
        return this.get(LootContextKey.THIS_ENTITY); // Paper
    }

    /**
     * Get the {@link HumanEntity} who killed the {@link #getLootedEntity()}.
     * Can be null.
     *
     * @return the killer entity, or null.
     * @deprecated use {@link #get(LootContextKey)} methods
     */
    // Paper
    @Deprecated
    public @Nullable HumanEntity getKiller() {
        return this.get(LootContextKey.ATTACKING_ENTITY) instanceof HumanEntity humanEntity ? humanEntity : null; // Paper
    }

    /**
     * Utility class to make building {@link LootContext} easier. The only
     * required argument is {@link org.bukkit.World}.
     */
    public static class Builder {

        private final org.bukkit.World world; // Paper
        private float luck;
        @Deprecated // Paper - not functional
        private int lootingModifier = LootContext.DEFAULT_LOOT_MODIFIER;
        private Random random = ThreadLocalRandom.current(); // Paper
        private final Map<LootContextKey<?>, Object> contextMap = new IdentityHashMap<>(); // Paper
        private boolean isLegacy = false; // Paper

        /**
         * Creates a new LootContext.Builder instance to facilitate easy
         * creation of {@link LootContext}s.
         *
         * @param location the location the LootContext should use
         * @deprecated not all loot contexts have locations
         */
        @Deprecated // Paper
        public Builder(Location location) {
            // Paper start
            Preconditions.checkArgument(location.getWorld() != null, "location missing world");
            this.world = location.getWorld();
            this.contextMap.put(LootContextKey.ORIGIN, Position.fine(location));
            this.isLegacy = true;
            // Paper end
        }

        // Paper start
        public Builder(World world) {
            this.world = world;
        }

        /**
         * Sets the random instance to use for this context.
         * Defaults to {@link java.util.concurrent.ThreadLocalRandom#current()}.
         *
         * @param random the random to use
         * @return the builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        public Builder withRandom(Random random) {
            this.random = random;
            return this;
        }

        /**
         * Sets or clears context values.
         *
         * @param contextKey the key to set or clear for
         * @param context the value to set, or null to clear
         * @param <T> the value type
         * @return the builder
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        public <T> Builder with(LootContextKey<T> contextKey, @Nullable T context) {
            if (context == null) {
                this.contextMap.remove(contextKey);
            } else {
                this.contextMap.put(contextKey, context);
            }
            return this;
        }
        // Paper end

        /**
         * Set how much luck to have when generating loot.
         *
         * @param luck the luck level
         * @return the Builder
         */
        @Contract(value = "_ -> this", mutates = "this") // Paper
        public Builder luck(float luck) {
            this.luck = luck;
            return this;
        }

        /**
         * Set the {@link org.bukkit.enchantments.Enchantment#LOOTING}
         * level equivalent to use when generating loot. Values less than or
         * equal to 0 will force the {@link LootTable} to only return a single
         * {@link org.bukkit.inventory.ItemStack} per pool.
         *
         * @param modifier the looting level modifier
         * @return the Builder
         * @deprecated no longer functional
         */
        @Deprecated(since = "1.21", forRemoval = true)
        public Builder lootingModifier(int modifier) {
            this.isLegacy = true; // Paper
            this.lootingModifier = modifier;
            return this;
        }

        /**
         * The entity that was killed.
         *
         * @param lootedEntity the looted entity
         * @return the Builder
         * @deprecated use {@link #with(LootContextKey, Object)}
         */
        @org.jetbrains.annotations.Contract(value = "_ -> this", mutates = "this") // Paper
        @Deprecated // Paper
        public Builder lootedEntity(@Nullable Entity lootedEntity) {
            this.isLegacy = true; // Paper
            return this.with(LootContextKey.THIS_ENTITY, lootedEntity); // Paper
        }

        /**
         * Set the {@link org.bukkit.entity.HumanEntity} that killed
         * {@link #getLootedEntity()}. This entity will be used to get the
         * looting level if {@link #lootingModifier(int)} is not set.
         *
         * @param killer the killer entity
         * @return the Builder
         * @deprecated use {@link #with(LootContextKey, Object)}
         */
        @Contract(value = "_ -> this", mutates = "this") // Paper
        @Deprecated // Paper
        public Builder killer(@Nullable HumanEntity killer) {
            this.isLegacy = true; // Paper
            return this.with(LootContextKey.ATTACKING_ENTITY, killer); // Paper
        }

        /**
         * Create a new {@link LootContext} instance using the supplied
         * parameters.
         *
         * @return a new {@link LootContext} instance
         */
        @Contract("-> new") // Paper
        public LootContext build() {
            return new LootContext(this.world, this.luck, this.random, this.contextMap, this.isLegacy, this.lootingModifier); // Paper
        }
    }
}
