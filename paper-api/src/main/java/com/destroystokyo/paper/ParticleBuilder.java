package com.destroystokyo.paper;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * A builder class for creating and spawning particles in the Minecraft world.
 * This class provides a fluent API for configuring particle effects and various
 * utility methods for creating common particle patterns.
 * <p>
 * Key features:
 * <ul>
 *   <li>Fluent builder pattern for easy configuration</li>
 *   <li>Caching system for reusable particle configurations</li>
 *   <li>Type-safe particle data handling</li>
 *   <li>Performance optimizations for particle spawning</li>
 *   <li>Utility methods for common particle effects</li>
 * </ul>
 * <p>
 * Example usage:
 * <pre>
 * // Create a reusable particle configuration
 * ParticleBuilder config = ParticleBuilder.of(Particle.FLAME)
 *     .count(5)
 *     .offset(0.1, 0.1, 0.1)
 *     .extra(0.1);
 * 
 * // Cache it for later use
 * config.cache("flame_effect");
 * 
 * // Later, retrieve and use the cached configuration
 * ParticleBuilder.fromCache("flame_effect")
 *     .location(player.getLocation())
 *     .spawn();
 * 
 * // Create a helix effect
 * ParticleBuilder.of(Particle.REDSTONE)
 *     .color(Color.RED)
 *     .helix(player.getLocation(), player.getLocation().add(0, 3, 0), 1.0, 3, 20)
 *     .spawn();
 * </pre>
 * 
 * @see Particle
 * @see World#spawnParticle(Particle, Location, int, double, double, double, double, Object)
 */
@NullMarked
public class ParticleBuilder implements Cloneable {

    private Particle particle;
    private @Nullable List<Player> receivers;
    private @Nullable Player source;
    private @Nullable Location location;
    private int count = 1;
    private double offsetX = 0, offsetY = 0, offsetZ = 0;
    private double extra = 1;
    private @Nullable Object data;
    private boolean force = true;

    // Cache for commonly used particle configurations with size limit and expiration
    private static final int MAX_CACHE_SIZE = 100;
    private static final Map<String, CacheEntry> PARTICLE_CACHE = new ConcurrentHashMap<>();

    private static class CacheEntry {
        final ParticleBuilder builder;
        final long timestamp;

        CacheEntry(ParticleBuilder builder) {
            this.builder = builder;
            this.timestamp = System.currentTimeMillis();
        }

        boolean isExpired() {
            return System.currentTimeMillis() - timestamp > 3600000; // 1 hour expiration
        }
    }

    // Cache for default particle data and types
    private static final Map<Particle, Object> DEFAULT_PARTICLE_DATA = new HashMap<>();
    private static final Map<Particle, Class<?>> PARTICLE_DATA_TYPES = new HashMap<>();

    // Reusable lists for performance optimization
    private static final ThreadLocal<List<Player>> PLAYER_BUFFER = ThreadLocal.withInitial(ObjectArrayList::new);

    static {
        // Initialize default particle data and expected types
        DEFAULT_PARTICLE_DATA.put(Particle.DUST, new Particle.DustOptions(Color.RED, 1.0f));
        PARTICLE_DATA_TYPES.put(Particle.DUST, Particle.DustOptions.class);
        // Add more particle type mappings as needed
    }

    /**
     * Validates a double value to ensure it is a valid number and within reasonable bounds.
     * 
     * @param value The value to validate
     * @param name The name of the parameter for error messages
     * @return The validated value
     * @throws IllegalArgumentException if the value is NaN, infinite, or out of bounds
     */
    private static double validateDouble(double value, String name) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException(name + " cannot be NaN or infinite");
        }
        // Reasonable bounds for particle effects
        if (Math.abs(value) > 1_000_000) {
            throw new IllegalArgumentException(name + " cannot be greater than 1,000,000 or less than -1,000,000");
        }
        return value;
    }

    /**
     * Creates a new ParticleBuilder for the specified particle type.
     *
     * @param particle The particle type to use
     * @throws IllegalArgumentException if particle is null
     */
    public ParticleBuilder(final Particle particle) {
        Preconditions.checkArgument(particle != null, "Particle cannot be null");
        this.particle = particle;
    }

    /**
     * Creates a new ParticleBuilder for the specified particle type.
     * This is a convenience method equivalent to new ParticleBuilder(particle).
     *
     * @param particle The particle type
     * @return A new ParticleBuilder instance
     */
    public static ParticleBuilder of(Particle particle) {
        return new ParticleBuilder(particle);
    }

    /**
     * Retrieves a cached particle configuration.
     *
     * @param key The cache key
     * @return A clone of the cached ParticleBuilder, or null if not found or expired
     */
    public static @Nullable ParticleBuilder fromCache(String key) {
        CacheEntry entry = PARTICLE_CACHE.get(key);
        if (entry == null || entry.isExpired()) {
            PARTICLE_CACHE.remove(key);
            return null;
        }
        return entry.builder.clone();
    }

    /**
     * Caches this particle configuration for later use.
     * The cached configuration can be retrieved using {@link #fromCache(String)}.
     * If the cache is full, the oldest entries will be removed.
     *
     * @param key The cache key
     * @return this builder for chaining
     */
    public ParticleBuilder cache(String key) {
        if (PARTICLE_CACHE.size() >= MAX_CACHE_SIZE) {
            // Remove expired entries
            PARTICLE_CACHE.entrySet().removeIf(e -> e.getValue().isExpired());
            
            // If still too large, remove oldest entries
            if (PARTICLE_CACHE.size() >= MAX_CACHE_SIZE) {
                List<Map.Entry<String, CacheEntry>> entries = new ArrayList<>(PARTICLE_CACHE.entrySet());
                entries.sort((a, b) -> Long.compare(a.getValue().timestamp, b.getValue().timestamp));
                for (int i = 0; i < entries.size() / 2; i++) {
                    PARTICLE_CACHE.remove(entries.get(i).getKey());
                }
            }
        }
        PARTICLE_CACHE.put(key, new CacheEntry(this.clone()));
        return this;
    }

    /**
     * Removes a cached particle configuration.
     *
     * @param key The cache key
     * @return true if the configuration was removed, false if it didn't exist
     */
    public static boolean removeFromCache(String key) {
        return PARTICLE_CACHE.remove(key) != null;
    }

    /**
     * Clears all cached particle configurations.
     */
    public static void clearCache() {
        PARTICLE_CACHE.clear();
    }

    /**
     * Checks if a particle configuration exists in the cache and is still valid.
     *
     * @param key The cache key to check
     * @return true if the configuration exists and is not expired, false otherwise
     * @throws IllegalArgumentException if the key is null or empty
     */
    public static boolean isCached(String key) {
        Preconditions.checkArgument(key != null && !key.isEmpty(), "Cache key cannot be null or empty");
        CacheEntry entry = PARTICLE_CACHE.get(key);
        return entry != null && !entry.isExpired();
    }

    /**
     * Returns statistics about the current state of the particle cache.
     *
     * @return A map containing cache statistics with the following keys:
     *         - "currentSize": Current number of entries in the cache
     *         - "expiredEntries": Number of expired entries
     *         - "maxCapacity": Maximum cache capacity
     */
    public static Map<String, Integer> getCacheStats() {
        Map<String, Integer> stats = new HashMap<>();
        int expiredCount = (int) PARTICLE_CACHE.values().stream()
            .filter(CacheEntry::isExpired)
            .count();
        
        stats.put("currentSize", PARTICLE_CACHE.size());
        stats.put("expiredEntries", expiredCount);
        stats.put("maxCapacity", MAX_CACHE_SIZE);
        
        return stats;
    }

    /**
     * Removes all expired entries from the cache.
     *
     * @return The number of entries that were removed
     */
    public static int cleanExpiredCache() {
        int initialSize = PARTICLE_CACHE.size();
        PARTICLE_CACHE.entrySet().removeIf(e -> e.getValue().isExpired());
        return initialSize - PARTICLE_CACHE.size();
    }

    /**
     * Spawns multiple particle effects in sequence.
     * This method is optimized for performance by reusing location objects
     * and minimizing object creation.
     *
     * @param effects The particle effects to spawn
     * @return this builder for chaining
     */
    @SafeVarargs
    public final ParticleBuilder sequence(Consumer<ParticleBuilder>... effects) {
        for (Consumer<ParticleBuilder> effect : effects) {
            effect.accept(this);
        }
        return this;
    }

    /**
     * Spawns the particle to all receiving players (or all).
     * This method is optimized for performance and is safe to use asynchronously.
     *
     * @return this builder for chaining
     * @throws IllegalStateException if location is not set
     */
    public ParticleBuilder spawn() {
        if (this.location == null) {
            throw new IllegalStateException("Please specify location for this particle");
        }

        // Get the world once to avoid multiple method calls
        World world = this.location.getWorld();
        double x = this.location.getX();
        double y = this.location.getY();
        double z = this.location.getZ();

        // Use cached list if available
        List<Player> effectiveReceivers = this.receivers;
        if (effectiveReceivers == null) {
            effectiveReceivers = PLAYER_BUFFER.get();
            effectiveReceivers.clear();
            effectiveReceivers.addAll(world.getPlayers());
        }

        // Spawn the particle
        world.spawnParticle(
            this.particle,
            effectiveReceivers,
            this.source,
            x, y, z,
            this.count,
            this.offsetX,
            this.offsetY,
            this.offsetZ,
            this.extra,
            this.data,
            this.force
        );

        return this;
    }

    /**
     * @return The particle going to be sent
     */
    public Particle particle() {
        return this.particle;
    }

    /**
     * Changes what particle will be sent
     *
     * @param particle The particle
     * @return a reference to this object.
     */
    public ParticleBuilder particle(final Particle particle) {
        this.particle = particle;
        return this;
    }

    /**
     * @return List of players who will receive the particle, or null for all in world
     */
    public @Nullable List<Player> receivers() {
        return this.receivers;
    }

    /**
     * Example use:
     * <p>
     * builder.receivers(16); if (builder.hasReceivers()) { sendParticleAsync(builder); }
     *
     * @return If this particle is going to be sent to someone
     */
    public boolean hasReceivers() {
        return (this.receivers == null && this.location != null && !this.location.getWorld().getPlayers().isEmpty()) || (
            this.receivers != null && !this.receivers.isEmpty());
    }

    /**
     * Sends this particle to all players in the world. This is rather silly, and you should likely not
     * be doing this.
     * <p>
     * Just be a logical person and use receivers by radius or collection.
     *
     * @return a reference to this object.
     */
    public ParticleBuilder allPlayers() {
        this.receivers = null;
        return this;
    }

    /**
     * @param receivers List of players to receive this particle, or null for all players in the
     *                  world
     * @return a reference to this object.
     * @deprecated Use {@link #receivers(Collection)} instead
     */
    @Deprecated
    public ParticleBuilder receivers(final @Nullable List<Player> receivers) {
        // Had to keep this as we first made API List<> and not Collection, but removing this may break plugins compiled on older jars
        this.receivers = receivers != null ? Lists.newArrayList(receivers) : null;
        return this;
    }

    /**
     * @param receivers List of players to receive this particle, or null for all players in the
     *                  world
     * @return a reference to this object.
     */
    public ParticleBuilder receivers(final @Nullable Collection<Player> receivers) {
        this.receivers = receivers != null ? Lists.newArrayList(receivers) : null;
        return this;
    }

    /**
     * @param receivers List of players to receive this particle, or null for all players in the
     *                  world
     * @return a reference to this object.
     */
    public ParticleBuilder receivers(final Player @Nullable... receivers) {
        this.receivers = receivers != null ? Lists.newArrayList(receivers) : null;
        return this;
    }

    /**
     * Selects all players within a cuboid selection around the particle location, within the
     * specified bounding box. If you want a more spherical check, see {@link #receivers(int,
     * boolean)}
     *
     * @param radius amount to add on all axis
     * @return a reference to this object.
     */
    public ParticleBuilder receivers(final int radius) {
        return this.receivers(radius, radius);
    }

    /**
     * Selects all players within the specified radius around the particle location. If byDistance is
     * false, behavior uses cuboid selection the same as {@link #receivers(int, int)} If byDistance is
     * true, radius is tested by distance in a spherical shape
     *
     * @param radius     amount to add on each axis
     * @param byDistance true to use a spherical radius, false to use a cuboid
     * @return a reference to this object.
     */
    public ParticleBuilder receivers(final int radius, final boolean byDistance) {
        if (!byDistance) {
            return this.receivers(radius, radius, radius);
        } else {
            if (this.location == null) {
                throw new IllegalStateException("Please set location first");
            }
            this.receivers = Lists.newArrayList();
            for (final Player nearbyPlayer : this.location.getWorld()
                .getNearbyPlayers(this.location, radius, radius, radius)) {
                final Location loc = nearbyPlayer.getLocation();
                final double x = NumberConversions.square(this.location.getX() - loc.getX());
                final double y = NumberConversions.square(this.location.getY() - loc.getY());
                final double z = NumberConversions.square(this.location.getZ() - loc.getZ());
                if (Math.sqrt(x + y + z) > radius) {
                    continue;
                }
                this.receivers.add(nearbyPlayer);
            }
            return this;
        }
    }

    /**
     * Selects all players within a cuboid selection around the particle location, within the
     * specified bounding box. Allows specifying a different Y size than X and Z If you want a more
     * cylinder check, see {@link #receivers(int, int, boolean)} If you want a more spherical check,
     * see {@link #receivers(int, boolean)}
     *
     * @param xzRadius amount to add on the x/z axis
     * @param yRadius  amount to add on the y axis
     * @return a reference to this object.
     */
    public ParticleBuilder receivers(final int xzRadius, final int yRadius) {
        return this.receivers(xzRadius, yRadius, xzRadius);
    }

    /**
     * Selects all players within the specified radius around the particle location. If byDistance is
     * false, behavior uses cuboid selection the same as {@link #receivers(int, int)} If byDistance is
     * true, radius is tested by distance on the y plane and on the x/z plane, in a cylinder shape.
     *
     * @param xzRadius   amount to add on the x/z axis
     * @param yRadius    amount to add on the y axis
     * @param byDistance true to use a cylinder shape, false to use cuboid
     * @return a reference to this object.
     * @throws IllegalStateException if a location hasn't been specified yet
     */
    public ParticleBuilder receivers(final int xzRadius, final int yRadius, final boolean byDistance) {
        if (!byDistance) {
            return this.receivers(xzRadius, yRadius, xzRadius);
        } else {
            if (this.location == null) {
                throw new IllegalStateException("Please set location first");
            }
            this.receivers = Lists.newArrayList();
            for (final Player nearbyPlayer : this.location.getWorld()
                .getNearbyPlayers(this.location, xzRadius, yRadius, xzRadius)) {
                final Location loc = nearbyPlayer.getLocation();
                if (Math.abs(loc.getY() - this.location.getY()) > yRadius) {
                    continue;
                }
                final double x = NumberConversions.square(this.location.getX() - loc.getX());
                final double z = NumberConversions.square(this.location.getZ() - loc.getZ());
                if (x + z > NumberConversions.square(xzRadius)) {
                    continue;
                }
                this.receivers.add(nearbyPlayer);
            }
            return this;
        }
    }

    /**
     * Selects all players within a cuboid selection around the particle location, within the
     * specified bounding box. If you want a more cylinder check, see {@link #receivers(int, int,
     * boolean)} If you want a more spherical check, see {@link #receivers(int, boolean)}
     *
     * @param xRadius amount to add on the x axis
     * @param yRadius amount to add on the y axis
     * @param zRadius amount to add on the z axis
     * @return a reference to this object.
     */
    public ParticleBuilder receivers(final int xRadius, final int yRadius, final int zRadius) {
        if (this.location == null) {
            throw new IllegalStateException("Please set location first");
        }
        return this.receivers(this.location.getWorld().getNearbyPlayers(this.location, xRadius, yRadius, zRadius));
    }

    /**
     * @return The player considered the source of this particle (for Visibility concerns), or null
     */
    public @Nullable Player source() {
        return this.source;
    }

    /**
     * Sets the source of this particle for visibility concerns (Vanish API)
     *
     * @param source The player who is considered the source
     * @return a reference to this object.
     */
    public ParticleBuilder source(final @Nullable Player source) {
        this.source = source;
        return this;
    }

    /**
     * @return Location of where the particle will spawn
     */
    public @Nullable Location location() {
        return this.location;
    }

    /**
     * Sets the location of where to spawn the particle
     *
     * @param location The location of the particle
     * @return a reference to this object.
     */
    public ParticleBuilder location(final Location location) {
        this.location = location.clone();
        return this;
    }

    /**
     * Sets the location of where to spawn the particle
     *
     * @param world World to spawn particle in
     * @param x     X location
     * @param y     Y location
     * @param z     Z location
     * @return a reference to this object.
     */
    public ParticleBuilder location(final World world, final double x, final double y, final double z) {
        this.location = new Location(world, x, y, z);
        return this;
    }

    /**
     * @return Number of particles to spawn
     */
    public int count() {
        return this.count;
    }

    /**
     * Sets the number of particles to spawn
     *
     * @param count Number of particles
     * @return a reference to this object.
     */
    public ParticleBuilder count(final int count) {
        this.count = count;
        return this;
    }

    /**
     * Particle offset X. Varies by particle on how this is used
     *
     * @return Particle offset X.
     */
    public double offsetX() {
        return this.offsetX;
    }

    /**
     * Particle offset Y. Varies by particle on how this is used
     *
     * @return Particle offset Y.
     */
    public double offsetY() {
        return this.offsetY;
    }

    /**
     * Particle offset Z. Varies by particle on how this is used
     *
     * @return Particle offset Z.
     */
    public double offsetZ() {
        return this.offsetZ;
    }

    /**
     * Sets the particle offset. Varies by particle on how this is used
     *
     * @param offsetX Particle offset X
     * @param offsetY Particle offset Y
     * @param offsetZ Particle offset Z
     * @return a reference to this object.
     */
    public ParticleBuilder offset(final double offsetX, final double offsetY, final double offsetZ) {
        this.offsetX = validateDouble(offsetX, "offsetX");
        this.offsetY = validateDouble(offsetY, "offsetY");
        this.offsetZ = validateDouble(offsetZ, "offsetZ");
        return this;
    }

    /**
     * Gets the Particle extra data. Varies by particle on how this is used
     *
     * @return the extra particle data
     */
    public double extra() {
        return this.extra;
    }

    /**
     * Sets the particle extra data. Varies by particle on how this is used
     *
     * @param extra the extra particle data
     * @return a reference to this object.
     */
    public ParticleBuilder extra(final double extra) {
        this.extra = validateDouble(extra, "extra");
        return this;
    }

    /**
     * Gets the particle data with type safety.
     * <p>
     * This method returns the particle data cast to the requested type.
     * If the particle has a specific data type requirement, it will be validated.
     * If the data is null or the particle does not require data, null will be returned.
     * </p>
     *
     * @param <T> The type to cast the data to
     * @return The particle data cast to the requested type, or null if no data is set
     * @throws ClassCastException If the stored data cannot be cast to the requested type
     */
    @SuppressWarnings("unchecked")
    public @Nullable <T> T data() {
        if (this.data == null) {
            return null;
        }

        Class<?> expectedType = PARTICLE_DATA_TYPES.get(this.particle);
        if (expectedType != null) {
            if (!expectedType.isInstance(this.data)) {
                throw new ClassCastException(String.format(
                    "Invalid data type for particle %s. Expected %s but got %s",
                    this.particle,
                    expectedType.getName(),
                    this.data.getClass().getName()
                ));
            }
        }

        try {
            return (T) this.data;
        } catch (ClassCastException e) {
            throw new ClassCastException(String.format(
                "Cannot cast particle data of type %s to the requested type. " +
                "Particle: %s, Data type: %s",
                this.data.getClass().getName(),
                this.particle,
                this.data.getClass().getName()
            ));
        }
    }

    /**
     * Sets the particle data for this builder.
     * <p>
     * The data type must match the expected type for the current particle.
     * If the particle requires specific data (e.g., {@link Particle#DUST} requires {@link Particle.DustOptions}),
     * this method will validate that the provided data is of the correct type.
     * </p>
     * <p>
     * If the particle does not require data, any type can be provided, but it will be validated
     * when retrieved using {@link #data()}.
     * </p>
     *
     * @param data The particle data to set, or null to clear the data
     * @param <T> The type of the particle data
     * @return This particle builder
     * @throws IllegalArgumentException If the provided data is not of the expected type for the current particle
     * @see #data()
     * @see #DEFAULT_PARTICLE_DATA
     * @see #PARTICLE_DATA_TYPES
     */
    public <T> ParticleBuilder data(final @Nullable T data) {
        if (data == null) {
            this.data = null;
            return this;
        }

        Class<?> dataType = this.particle.getDataType();
        if (!dataType.isInstance(data)) {
            throw new IllegalArgumentException(
                String.format("Invalid data type %s for particle %s (expected %s)",
                    data.getClass().getName(),
                    this.particle.name(),
                    dataType.getName()
                )
            );
        }

        if (data instanceof Particle.DustOptions) {
            validateDustOptions((Particle.DustOptions) data);
        }

        this.data = data;
        return this;
    }

    private void validateDustOptions(Particle.DustOptions options) {
        Preconditions.checkArgument(options.getColor() != null, "Dust color cannot be null");
        Preconditions.checkArgument(options.getSize() > 0, "Dust size must be positive");
        Preconditions.checkArgument(options.getSize() <= 4, "Dust size cannot be greater than 4");
    }

    /**
     * @return whether the particle is forcefully shown to players.
     */
    public boolean force() {
        return this.force;
    }

    /**
     * Sets whether the particle is forcefully shown to the player. If forced, the particle will show
     * faraway, as far as the player's view distance allows. If false, the particle will show
     * according to the client's particle settings.
     *
     * @param force true to force, false for normal
     * @return a reference to this object.
     */
    public ParticleBuilder force(final boolean force) {
        this.force = force;
        return this;
    }

    /**
     * Sets the particle Color.
     * Only valid for particles with a data type of {@link Color} or {@link Particle.DustOptions}.
     *
     * @param color the new particle color
     * @return a reference to this object.
     */
    public ParticleBuilder color(final @Nullable Color color) {
        if (this.particle.getDataType() == Color.class) {
            return this.data(color);
        }
        return this.color(color, 1);
    }

    /**
     * Sets the particle Color and size.
     * Only valid for particles with a data type of {@link Particle.DustOptions}.
     *
     * @param color the new particle color
     * @param size  the size of the particle
     * @return a reference to this object.
     */
    public ParticleBuilder color(final @Nullable Color color, final float size) {
        if (this.particle.getDataType() != Particle.DustOptions.class && color != null) {
            throw new IllegalStateException("The combination of Color and size cannot be set on this particle type.");
        }

        // We don't officially support reusing these objects, but here we go
        if (color == null) {
            if (this.data instanceof Particle.DustOptions) {
                return this.data(null);
            } else {
                return this;
            }
        }

        return this.data(new Particle.DustOptions(color, size));
    }

    /**
     * Sets the particle Color.
     * Only valid for particles with a data type of {@link Color} or {@link Particle.DustOptions}.
     *
     * @param r red color component
     * @param g green color component
     * @param b blue color component
     * @return a reference to this object.
     */
    public ParticleBuilder color(final int r, final int g, final int b) {
        return this.color(Color.fromRGB(r, g, b));
    }

    /**
     * Sets the particle Color.
     * Only valid for particles with a data type of {@link Color} or {@link Particle.DustOptions}.
     * <p>
     * This method detects if the provided color integer is in RGB or ARGB format.
     * If the alpha channel is zero, it treats the color as RGB. Otherwise, it treats it as ARGB.
     *
     * @param color an integer representing the color components. If the highest byte (alpha channel) is zero,
     *              the color is treated as RGB. Otherwise, it is treated as ARGB.
     * @return a reference to this object.
     */
    public ParticleBuilder color(final int color) {
        final int alpha = (color >> 24) & 0xFF;
        if (alpha == 0) {
            return this.color(Color.fromRGB(color));
        }
        return this.color(Color.fromARGB(color));
    }

    /**
     * Sets the particle Color.
     * Only valid for particles with a data type of {@link Color} or {@link Particle.DustOptions}.
     *
     * @param a alpha color component
     * @param r red color component
     * @param g green color component
     * @param b blue color component
     * @return a reference to this object.
     */
    public ParticleBuilder color(final int a, final int r, final int g, final int b) {
        return this.color(Color.fromARGB(a, r, g, b));
    }

    /**
     * Sets the particle Color Transition.
     * Only valid for {@link Particle#DUST_COLOR_TRANSITION}.
     *
     * @param fromColor the new particle from color
     * @param toColor   the new particle to color
     * @return a reference to this object.
     * @throws IllegalArgumentException if the particle builder's {@link #particle()} isn't {@link Particle#DUST_COLOR_TRANSITION}.
     */
    public ParticleBuilder colorTransition(final Color fromColor, final Color toColor) {
        return this.colorTransition(fromColor, toColor, 1);
    }

    /**
     * Sets the particle Color Transition.
     * Only valid for {@link Particle#DUST_COLOR_TRANSITION}.
     *
     * @param fromRed   red color component for the "from" color
     * @param fromGreen green color component for the "from" color
     * @param fromBlue  blue color component for the "from" color
     * @param toRed     red color component for the to color
     * @param toGreen   green color component for the to color
     * @param toBlue    blue color component for the to color
     * @return a reference to this object.
     * @throws IllegalArgumentException if the particle builder's {@link #particle()} isn't {@link Particle#DUST_COLOR_TRANSITION}.
     */
    public ParticleBuilder colorTransition(
        final int fromRed, final int fromGreen, final int fromBlue,
        final int toRed, final int toGreen, final int toBlue
    ) {
        return this.colorTransition(Color.fromRGB(fromRed, fromGreen, fromBlue), Color.fromRGB(toRed, toGreen, toBlue));
    }

    /**
     * Sets the particle Color Transition.
     * Only valid for {@link Particle#DUST_COLOR_TRANSITION}.
     *
     * @param fromRgb an integer representing the red, green, and blue color components for the "from" color
     * @param toRgb   an integer representing the red, green, and blue color components for the "to" color
     * @return a reference to this object.
     * @throws IllegalArgumentException if the particle builder's {@link #particle()} isn't {@link Particle#DUST_COLOR_TRANSITION}.
     */
    public ParticleBuilder colorTransition(final int fromRgb, final int toRgb) {
        return this.colorTransition(Color.fromRGB(fromRgb), Color.fromRGB(toRgb));
    }

    /**
     * Sets the particle Color Transition and size.
     * Only valid for {@link Particle#DUST_COLOR_TRANSITION}.
     *
     * @param fromColor the new particle color for the "from" color.
     * @param toColor   the new particle color for the "to" color.
     * @param size      the size of the particle
     * @return a reference to this object.
     * @throws IllegalArgumentException if the particle builder's {@link #particle()} isn't {@link Particle#DUST_COLOR_TRANSITION}.
     */
    public ParticleBuilder colorTransition(final Color fromColor, final Color toColor, final float size) {
        Preconditions.checkArgument(fromColor != null, "Cannot define color transition with null fromColor.");
        Preconditions.checkArgument(toColor != null, "Cannot define color transition with null toColor.");
        Preconditions.checkArgument(this.particle() == Particle.DUST_COLOR_TRANSITION, "Can only define a color transition on particle DUST_COLOR_TRANSITION.");
        return this.data(new Particle.DustTransition(fromColor, toColor, size));
    }

    /**
     * Creates a particle effect that follows a straight line between two points.
     * The particles will be evenly distributed along the line.
     *
     * @param start The starting location
     * @param end The ending location
     * @param particlesPerBlock Number of particles per block of distance
     * @return A reference to this object
     */
    public ParticleBuilder line(@NonNull Location start, @NonNull Location end, double particlesPerBlock) {
        Preconditions.checkArgument(start != null, "Start location cannot be null");
        Preconditions.checkArgument(end != null, "End location cannot be null");
        Preconditions.checkArgument(particlesPerBlock > 0, "Particles per block must be positive");

        double distance = start.distance(end);
        int particleCount = (int) (distance * particlesPerBlock);
        
        double stepX = (end.getX() - start.getX()) / particleCount;
        double stepY = (end.getY() - start.getY()) / particleCount;
        double stepZ = (end.getZ() - start.getZ()) / particleCount;

        for (int i = 0; i < particleCount; i++) {
            double x = start.getX() + (stepX * i);
            double y = start.getY() + (stepY * i);
            double z = start.getZ() + (stepZ * i);
            this.location(start.getWorld(), x, y, z).spawn();
        }
        return this;
    }

    /**
     * Creates a particle effect in a circle around a center point.
     *
     * @param center The center location of the circle
     * @param radius The radius of the circle
     * @param particlesPerBlock Number of particles per block of circumference
     * @return A reference to this object
     */
    public ParticleBuilder circle(@NonNull Location center, double radius, double particlesPerBlock) {
        Preconditions.checkArgument(center != null, "Center location cannot be null");
        Preconditions.checkArgument(radius > 0, "Radius must be positive");
        Preconditions.checkArgument(particlesPerBlock > 0, "Particles per block must be positive");

        double circumference = 2 * Math.PI * radius;
        int particleCount = (int) (circumference * particlesPerBlock);
        
        for (int i = 0; i < particleCount; i++) {
            double angle = (2 * Math.PI * i) / particleCount;
            double x = center.getX() + (radius * Math.cos(angle));
            double z = center.getZ() + (radius * Math.sin(angle));
            this.location(center.getWorld(), x, center.getY(), z).spawn();
        }
        return this;
    }

    /**
     * Creates a particle effect in a sphere around a center point.
     *
     * @param center The center location of the sphere
     * @param radius The radius of the sphere
     * @param particlesPerBlock Number of particles per block of surface area
     * @return A reference to this object
     */
    public ParticleBuilder sphere(@NonNull Location center, double radius, double particlesPerBlock) {
        Preconditions.checkArgument(center != null, "Center location cannot be null");
        Preconditions.checkArgument(radius > 0, "Radius must be positive");
        Preconditions.checkArgument(particlesPerBlock > 0, "Particles per block must be positive");

        double surfaceArea = 4 * Math.PI * radius * radius;
        int particleCount = (int) (surfaceArea * particlesPerBlock);
        
        for (int i = 0; i < particleCount; i++) {
            double theta = Math.acos(2 * Math.random() - 1);
            double phi = 2 * Math.PI * Math.random();
            
            double x = center.getX() + (radius * Math.sin(theta) * Math.cos(phi));
            double y = center.getY() + (radius * Math.sin(theta) * Math.sin(phi));
            double z = center.getZ() + (radius * Math.cos(theta));
            
            this.location(center.getWorld(), x, y, z).spawn();
        }
        return this;
    }

    /**
     * Creates a particle effect that follows a spiral pattern.
     *
     * @param center The center location of the spiral
     * @param radius The maximum radius of the spiral
     * @param height The height of the spiral
     * @param rotations Number of complete rotations
     * @param particlesPerBlock Number of particles per block of spiral length
     * @return A reference to this object
     */
    public ParticleBuilder spiral(@NonNull Location center, double radius, double height, int rotations, double particlesPerBlock) {
        Preconditions.checkArgument(center != null, "Center location cannot be null");
        Preconditions.checkArgument(radius > 0, "Radius must be positive");
        Preconditions.checkArgument(height > 0, "Height must be positive");
        Preconditions.checkArgument(rotations > 0, "Rotations must be positive");
        Preconditions.checkArgument(particlesPerBlock > 0, "Particles per block must be positive");

        double spiralLength = Math.sqrt(Math.pow(2 * Math.PI * radius * rotations, 2) + Math.pow(height, 2));
        int particleCount = (int) (spiralLength * particlesPerBlock);
        
        for (int i = 0; i < particleCount; i++) {
            double t = (double) i / particleCount;
            double angle = 2 * Math.PI * rotations * t;
            double r = radius * t;
            
            double x = center.getX() + (r * Math.cos(angle));
            double y = center.getY() + (height * t);
            double z = center.getZ() + (r * Math.sin(angle));
            
            this.location(center.getWorld(), x, y, z).spawn();
        }
        return this;
    }

    /**
     * Creates a particle effect that follows a path defined by a list of locations.
     *
     * @param path The list of locations defining the path
     * @param particlesPerBlock Number of particles per block of path length
     * @return A reference to this object
     */
    public ParticleBuilder path(@NonNull List<Location> path, double particlesPerBlock) {
        Preconditions.checkArgument(path != null && !path.isEmpty(), "Path cannot be null or empty");
        Preconditions.checkArgument(particlesPerBlock > 0, "Particles per block must be positive");

        double totalDistance = 0;
        for (int i = 1; i < path.size(); i++) {
            totalDistance += path.get(i-1).distance(path.get(i));
        }
        
        int particleCount = (int) (totalDistance * particlesPerBlock);
        double step = 1.0 / particleCount;
        
        for (int i = 0; i < particleCount; i++) {
            double t = i * step;
            Location interpolated = interpolatePath(path, t);
            this.location(interpolated).spawn();
        }
        return this;
    }

    private Location interpolatePath(List<Location> path, double t) {
        double totalDistance = 0;
        double targetDistance = t * getPathLength(path);
        
        for (int i = 1; i < path.size(); i++) {
            Location start = path.get(i-1);
            Location end = path.get(i);
            double segmentDistance = start.distance(end);
            
            if (totalDistance + segmentDistance >= targetDistance) {
                double segmentT = (targetDistance - totalDistance) / segmentDistance;
                return new Location(
                    start.getWorld(),
                    start.getX() + (end.getX() - start.getX()) * segmentT,
                    start.getY() + (end.getY() - start.getY()) * segmentT,
                    start.getZ() + (end.getZ() - start.getZ()) * segmentT
                );
            }
            totalDistance += segmentDistance;
        }
        return path.get(path.size() - 1);
    }

    private double getPathLength(List<Location> path) {
        double length = 0;
        for (int i = 1; i < path.size(); i++) {
            length += path.get(i-1).distance(path.get(i));
        }
        return length;
    }

    /**
     * Creates a helix effect around a line between two points.
     *
     * @param start The start location
     * @param end The end location
     * @param radius The radius of the helix
     * @param rotations The number of rotations
     * @param particlesPerRotation The number of particles per rotation
     * @return this builder for chaining
     */
    public ParticleBuilder helix(@NonNull Location start, @NonNull Location end, double radius, int rotations, double particlesPerRotation) {
        Preconditions.checkArgument(start != null, "Start location cannot be null");
        Preconditions.checkArgument(end != null, "End location cannot be null");
        Preconditions.checkArgument(start.getWorld().equals(end.getWorld()), "Start and end locations must be in the same world");
        Preconditions.checkArgument(rotations > 0, "Rotations must be positive");
        Preconditions.checkArgument(particlesPerRotation > 0, "Particles per rotation must be positive");

        double length = start.distance(end);
        double heightStep = length / (rotations * particlesPerRotation);
        
        Location current = start.clone();
        double dx = (end.getX() - start.getX()) / (rotations * particlesPerRotation);
        double dy = heightStep; // Explicitly use heightStep for vertical movement
        double dz = (end.getZ() - start.getZ()) / (rotations * particlesPerRotation);

        for (int i = 0; i < rotations * particlesPerRotation; i++) {
            double angle = (2 * Math.PI * i) / particlesPerRotation;
            double x = current.getX() + radius * Math.cos(angle);
            double y = current.getY();
            double z = current.getZ() + radius * Math.sin(angle);
            
            this.location(current.getWorld(), x, y, z).spawn();
            
            current.add(dx, dy, dz);
        }

        return this;
    }

    /**
     * Creates a vortex effect that spirals inward or outward.
     *
     * @param center The center location
     * @param startRadius The starting radius
     * @param endRadius The ending radius
     * @param height The height of the vortex
     * @param rotations The number of rotations
     * @param particlesPerRotation The number of particles per rotation
     * @return this builder for chaining
     */
    public ParticleBuilder vortex(@NonNull Location center, double startRadius, double endRadius, double height, int rotations, double particlesPerRotation) {
        Preconditions.checkArgument(center != null, "Center location cannot be null");
        Preconditions.checkArgument(rotations > 0, "Rotations must be positive");
        Preconditions.checkArgument(particlesPerRotation > 0, "Particles per rotation must be positive");

        double totalParticles = rotations * particlesPerRotation;
        double heightStep = height / totalParticles;
        double radiusStep = (endRadius - startRadius) / totalParticles;

        Location current = center.clone();
        double currentRadius = startRadius;

        for (int i = 0; i < totalParticles; i++) {
            double angle = (2 * Math.PI * i) / particlesPerRotation;
            double x = current.getX() + currentRadius * Math.cos(angle);
            double z = current.getZ() + currentRadius * Math.sin(angle);
            
            this.location(current.getWorld(), x, current.getY(), z).spawn();
            
            current.add(0, heightStep, 0);
            currentRadius += radiusStep;
        }

        return this;
    }

    /**
     * Creates a wave effect along a line.
     *
     * @param start The start location
     * @param end The end location
     * @param amplitude The amplitude of the wave
     * @param frequency The frequency of the wave
     * @param particlesPerBlock The number of particles per block
     * @return this builder for chaining
     */
    public ParticleBuilder wave(@NonNull Location start, @NonNull Location end, double amplitude, double frequency, double particlesPerBlock) {
        Preconditions.checkArgument(start != null, "Start location cannot be null");
        Preconditions.checkArgument(end != null, "End location cannot be null");
        Preconditions.checkArgument(start.getWorld().equals(end.getWorld()), "Start and end locations must be in the same world");
        Preconditions.checkArgument(particlesPerBlock > 0, "Particles per block must be positive");

        double length = start.distance(end);
        int particles = (int) (length * particlesPerBlock);
        
        double dx = (end.getX() - start.getX()) / particles;
        double dy = (end.getY() - start.getY()) / particles;
        double dz = (end.getZ() - start.getZ()) / particles;

        Location current = start.clone();

        for (int i = 0; i < particles; i++) {
            double progress = i / (double) particles;
            double wave = amplitude * Math.sin(progress * 2 * Math.PI * frequency);
            
            double x = current.getX() + wave * Math.cos(Math.atan2(dz, dx) + Math.PI/2);
            double z = current.getZ() + wave * Math.sin(Math.atan2(dz, dx) + Math.PI/2);
            
            this.location(current.getWorld(), x, current.getY(), z).spawn();
            
            current.add(dx, dy, dz);
        }

        return this;
    }

    /**
     * Creates a burst effect at a location.
     *
     * @param center The center location
     * @param radius The radius of the burst
     * @param particleCount The number of particles
     * @return this builder for chaining
     */
    public ParticleBuilder burst(@NonNull Location center, double radius, int particleCount) {
        Preconditions.checkArgument(center != null, "Center location cannot be null");
        Preconditions.checkArgument(radius > 0, "Radius must be positive");
        Preconditions.checkArgument(particleCount > 0, "Particle count must be positive");

        for (int i = 0; i < particleCount; i++) {
            double phi = Math.acos(2 * Math.random() - 1);
            double theta = 2 * Math.PI * Math.random();
            
            double x = center.getX() + radius * Math.sin(phi) * Math.cos(theta);
            double y = center.getY() + radius * Math.sin(phi) * Math.sin(theta);
            double z = center.getZ() + radius * Math.cos(phi);
            
            this.location(center.getWorld(), x, y, z).spawn();
        }

        return this;
    }

    /**
     * Creates a polygon shape with the specified number of sides.
     *
     * @param center The center location
     * @param radius The radius of the polygon
     * @param sides The number of sides
     * @param particlesPerSide The number of particles per side
     * @return this builder for chaining
     */
    public ParticleBuilder polygon(@NonNull Location center, double radius, int sides, double particlesPerSide) {
        Preconditions.checkArgument(center != null, "Center location cannot be null");
        Preconditions.checkArgument(radius > 0, "Radius must be positive");
        Preconditions.checkArgument(sides >= 3, "Polygon must have at least 3 sides");
        Preconditions.checkArgument(particlesPerSide > 0, "Particles per side must be positive");

        double angleStep = 2 * Math.PI / sides;
        
        for (int i = 0; i < sides; i++) {
            double angle1 = i * angleStep;
            double angle2 = ((i + 1) % sides) * angleStep;
            
            double x1 = center.getX() + radius * Math.cos(angle1);
            double z1 = center.getZ() + radius * Math.sin(angle1);
            double x2 = center.getX() + radius * Math.cos(angle2);
            double z2 = center.getZ() + radius * Math.sin(angle2);
            
            Location start = new Location(center.getWorld(), x1, center.getY(), z1);
            Location end = new Location(center.getWorld(), x2, center.getY(), z2);
            
            line(start, end, particlesPerSide);
        }

        return this;
    }

    /**
     * Creates a fountain effect at a location.
     *
     * @param base The base location
     * @param height The height of the fountain
     * @param radius The radius at the top
     * @param particleCount The number of particles
     * @return this builder for chaining
     */
    public ParticleBuilder fountain(@NonNull Location base, double height, double radius, int particleCount) {
        Preconditions.checkArgument(base != null, "Base location cannot be null");
        Preconditions.checkArgument(height > 0, "Height must be positive");
        Preconditions.checkArgument(radius > 0, "Radius must be positive");
        Preconditions.checkArgument(particleCount > 0, "Particle count must be positive");

        for (int i = 0; i < particleCount; i++) {
            double t = Math.random();
            double angle = 2 * Math.PI * Math.random();
            
            double x = base.getX() + radius * t * Math.cos(angle);
            double y = base.getY() + height * (1 - t * t);
            double z = base.getZ() + radius * t * Math.sin(angle);
            
            this.location(base.getWorld(), x, y, z).spawn();
        }

        return this;
    }

    /**
     * Creates a tornado effect at a location.
     *
     * @param base The base location
     * @param height The height of the tornado
     * @param startRadius The radius at the base
     * @param endRadius The radius at the top
     * @param rotations The number of rotations
     * @param particlesPerRotation The number of particles per rotation
     * @return this builder for chaining
     */
    public ParticleBuilder tornado(@NonNull Location base, double height, double startRadius, double endRadius, int rotations, double particlesPerRotation) {
        Preconditions.checkArgument(base != null, "Base location cannot be null");
        Preconditions.checkArgument(height > 0, "Height must be positive");
        Preconditions.checkArgument(startRadius > 0, "Start radius must be positive");
        Preconditions.checkArgument(endRadius > 0, "End radius must be positive");
        Preconditions.checkArgument(rotations > 0, "Rotations must be positive");
        Preconditions.checkArgument(particlesPerRotation > 0, "Particles per rotation must be positive");

        double totalParticles = rotations * particlesPerRotation;
        double heightStep = height / totalParticles;
        double radiusStep = (endRadius - startRadius) / totalParticles;
        double angleStep = 2 * Math.PI / particlesPerRotation;
        double speedFactor = 1.0 - (1.0 / totalParticles);

        Location current = base.clone();
        double currentRadius = startRadius;

        for (int i = 0; i < totalParticles; i++) {
            double angle = i * angleStep;
            double speed = extra() * speedFactor;
            
            double x = current.getX() + currentRadius * Math.cos(angle);
            double z = current.getZ() + currentRadius * Math.sin(angle);
            
            this.location(current.getWorld(), x, current.getY(), z)
                .extra(speed)
                .spawn();
            
            current.add(0, heightStep, 0);
            currentRadius = startRadius + (i * radiusStep);
        }

        return this;
    }

    @Override
    public ParticleBuilder clone() {
        try {
            final ParticleBuilder builder = (ParticleBuilder) super.clone();
            if (this.location != null) builder.location = this.location.clone();
            if (this.receivers != null) builder.receivers = new ObjectArrayList<>(this.receivers);
            return builder;
        } catch (final CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
