package org.bukkit.loot;

import com.google.common.base.Preconditions;
import io.papermc.paper.loot.LootContextKey;
import io.papermc.paper.loot.LootContextKeys;
import io.papermc.paper.math.Position;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents additional information a {@link LootTable} can use to modify its
 * generated loot.
 */
@NullMarked
public final class LootContext {

    /**
     * @deprecated no longer functional
     */
    @Deprecated
    public static final int DEFAULT_LOOT_MODIFIER = -1;

    private final World world;
    private final float luck;
    private final @Nullable Random random;
    private final Map<LootContextKey, Optional<?>> contextMap;
    // TODO dynamic drops API when loot table is finally a registry

    private LootContext(final World world, final float luck, final @Nullable Random random, final Map<LootContextKey, Optional<?>> contextMap) {
        this.world = world;
        this.luck = luck;
        this.random = random;
        this.contextMap = contextMap;
    }

    /**
     * Gets keys populated in this loot context
     *
     * @return the keys explicitly set
     */
    public Set<LootContextKey> keySet() {
        return this.contextMap.keySet();
    }

    /**
     * Checks if this context contains this key (often with a linked value)
     *
     * @param key the key to check
     * @return true if this context contains this key
     * @see #get(LootContextKey.Valued)
     * @see #getOrThrow(LootContextKey.Valued)
     */
    public boolean has(final LootContextKey key) {
        return this.contextMap.containsKey(key);
    }

    /**
     * Gets the value for a context key
     *
     * @param <T> value type
     * @param key the key for the value
     * @return the value or {@code null} if this context doesn't have a value for the key
     * @see #has(LootContextKey)
     * @see #getOrThrow(LootContextKey.Valued)
     */
    @SuppressWarnings("unchecked")
    public <T> @Nullable T get(final LootContextKey.Valued<T> key) {
        final Optional<?> value = this.contextMap.get(key);
        return value == null ? null : (T) value.orElseThrow();
    }

    /**
     * Gets the value of a context key, throwing an exception
     * if one isn't found
     *
     * @param <T> value type
     * @param key the key for the value
     * @return the value
     * @throws NoSuchElementException if no value is found for that key
     * @see #has(LootContextKey)
     * @see #get(LootContextKey.Valued)
     */
    public <T> T getOrThrow(final LootContextKey.Valued<T> key) {
        final T value = this.get(key);
        if (value == null) {
            throw new NoSuchElementException("No value found for " + key.key().asString());
        }
        return value;
    }

    /**
     * Gets the random instance used for this context.
     *
     * @return the random
     */
    public @Nullable Random getRandom() {
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
     * The {@link Location} to store where the loot will be generated.
     *
     * @return the Location of where the loot will be generated
     * @deprecated use {@link #get(LootContextKey.Valued)} methods
     */
    @Deprecated(since = "26.1")
    public Location getLocation() {
        if (this.has(LootContextKeys.ORIGIN)) {
            final Position pos = this.getOrThrow(LootContextKeys.ORIGIN);
            return pos.toLocation(this.world);
        } else if (this.has(LootContextKeys.THIS_ENTITY)) {
            return this.getOrThrow(LootContextKeys.THIS_ENTITY).getLocation();
        } else {
            throw new IllegalStateException("All known context key sets require \"origin\" or \"this_entity\" and this one doesn't have either");
        }
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
     * Represents the {@link org.bukkit.enchantments.Enchantment#LOOTING}
     * the {@link #getKiller()} entity has on their equipped item.
     * <p>
     * This value is only set via {@link LootContext.Builder#lootingModifier(int)}.
     * If not set, the {@link #getKiller()} entity's looting level will
     * be used instead.
     *
     * @return the looting level
     * @deprecated no longer functional, will always be {@link #DEFAULT_LOOT_MODIFIER}
     */
    @Deprecated(since = "1.21", forRemoval = true)
    public int getLootingModifier() {
        return DEFAULT_LOOT_MODIFIER;
    }

    /**
     * Get the {@link Entity} that was killed.
     *
     * @return the looted entity or {@code null}
     * @deprecated use {@link #get(LootContextKey.Valued)} methods
     */
    @Deprecated(since = "26.1")
    public @Nullable Entity getLootedEntity() {
        return this.get(LootContextKeys.THIS_ENTITY);
    }

    /**
     * Get the {@link HumanEntity} who killed the {@link #getLootedEntity()}.
     *
     * @return the killer entity, or {@code null}.
     * @deprecated use {@link #get(LootContextKey.Valued)} methods
     */
    @Deprecated(since = "26.1")
    public @Nullable HumanEntity getKiller() {
        return this.get(LootContextKeys.ATTACKING_ENTITY) instanceof final HumanEntity humanEntity ? humanEntity : null;
    }

    /**
     * Creates a new Builder instance to facilitate easy
     * creation of {@linkplain LootContext}s.
     *
     * @param world the world the LootContext should use
     */
    public static Builder builder(final World world) {
        return new Builder(world);
    }

    /**
     * Utility class to make building {@link LootContext} easier. The only
     * required argument is {@link org.bukkit.World}.
     */
    public static class Builder {

        private final World world;
        private float luck;
        private @Nullable Random random = null;
        private final Map<LootContextKey, Optional<?>> contextMap = new IdentityHashMap<>();

        /**
         * Creates a new Builder instance to facilitate easy
         * creation of {@linkplain LootContext}s.
         *
         * @param location the location the LootContext should use
         * @deprecated not all loot contexts have locations, use {@link LootContext#builder(World)}
         */
        @Deprecated(since = "26.1")
        public Builder(final Location location) {
            this(location.getWorld());
            this.with(LootContextKeys.ORIGIN, location);
        }

        private Builder(final World world) {
            Preconditions.checkArgument(world != null, "world cannot be null");
            this.world = world;
        }

        /**
         * Sets the random instance to use for this context.
         * Can be {@code null} when sourced from the random sequence defined in
         * the loot table.
         *
         * @param random the random to use
         * @return the builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        public Builder withRandom(final @Nullable Random random) {
            this.random = random;
            return this;
        }

        /**
         * Sets a context key with an associated value
         *
         * @param <T> the value type
         * @param key the key to set
         * @param value the value to set
         * @return the builder
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        public <T> Builder with(final LootContextKey.Valued<T> key, final T value) {
            this.contextMap.put(key, Optional.of(value));
            return this;
        }

        /**
         * Sets a context key without an associated value.
         *
         * @param key the key to set
         * @return the builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        public Builder with(final LootContextKey.NonValued key) {
            this.contextMap.put(key, Optional.empty());
            return this;
        }

        /**
         * Removes a context key with its associated value from this builder.
         *
         * @param key the key to clear for
         * @return the builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        public Builder without(final LootContextKey key) {
            this.contextMap.remove(key);
            return this;
        }

        /**
         * Set how much luck to have when generating loot.
         *
         * @param luck the luck level
         * @return the builder
         */
        @Contract(value = "_ -> this", mutates = "this")
        public Builder luck(final float luck) {
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
         * @return the builder
         * @deprecated no longer functional
         */
        @Deprecated(since = "1.21", forRemoval = true)
        public Builder lootingModifier(final int modifier) {
            return this;
        }

        /**
         * The entity that was killed.
         *
         * @param lootedEntity the looted entity
         * @return the builder
         * @deprecated use {@link #with(LootContextKey.Valued, Object)}
         */
        @Deprecated(since = "26.1")
        @Contract(value = "_ -> this", mutates = "this")
        public Builder lootedEntity(final @Nullable Entity lootedEntity) {
            if (lootedEntity == null) {
                this.without(LootContextKeys.THIS_ENTITY)
                    .without(LootContextKeys.DAMAGE_SOURCE)
                    .without(LootContextKeys.ORIGIN);
            } else {
                this.with(LootContextKeys.THIS_ENTITY, lootedEntity)
                    .with(LootContextKeys.DAMAGE_SOURCE, DamageSource.builder(DamageType.GENERIC).build())
                    .with(LootContextKeys.ORIGIN, lootedEntity.getLocation());
            }
            return this;
        }

        /**
         * Set the {@link org.bukkit.entity.HumanEntity} that killed
         * {@link #getLootedEntity()}. This entity will be used to get the
         * looting level if {@link #lootingModifier(int)} is not set.
         *
         * @param killer the killer entity
         * @return the builder
         * @deprecated use {@link #with(LootContextKey.Valued, Object)}
         */
        @Deprecated(since = "26.1")
        @Contract(value = "_ -> this", mutates = "this")
        public Builder killer(final @Nullable HumanEntity killer) {
            if (killer == null) {
                this.without(LootContextKeys.ATTACKING_ENTITY)
                    .without(LootContextKeys.DAMAGE_SOURCE)
                    .without(LootContextKeys.LAST_DAMAGE_PLAYER)
                    .without(LootContextKeys.TOOL);
            } else {
                this.with(LootContextKeys.ATTACKING_ENTITY, killer)
                    .with(LootContextKeys.DAMAGE_SOURCE, DamageSource.builder(DamageType.PLAYER_ATTACK)
                        .withCausingEntity(killer)
                        .withDirectEntity(killer) // damage source api is outdated...
                        .build())
                    .with(LootContextKeys.LAST_DAMAGE_PLAYER, killer)
                    .with(LootContextKeys.TOOL, killer.getActiveItem());
            }
            return this;
        }

        /**
         * Create a new {@link LootContext} instance using the supplied
         * parameters.
         *
         * @return a new {@link LootContext} instance
         */
        @Contract("-> new")
        public LootContext build() {
            return new LootContext(
                this.world,
                this.luck,
                this.random,
                Map.copyOf(this.contextMap)
            );
        }
    }
}
