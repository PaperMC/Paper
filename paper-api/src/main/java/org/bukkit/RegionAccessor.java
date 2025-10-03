package org.bukkit;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A RegionAccessor gives access to getting, modifying and spawning {@link Biome}, {@link BlockState} and {@link Entity},
 * as well as generating some basic structures.
 */
public interface RegionAccessor extends Keyed, io.papermc.paper.world.flag.FeatureFlagSetHolder { // Paper - feature flag API

    /**
     * Gets the {@link Biome} at the given {@link Location}.
     *
     * @param location the location of the biome
     * @return Biome at the given location
     * @see #getComputedBiome(int, int, int)
     */
    @NotNull
    default Biome getBiome(@NotNull Location location) {
        return this.getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Gets the {@link Biome} at the given coordinates.
     *
     * @param x X-coordinate of the block
     * @param y Y-coordinate of the block
     * @param z Z-coordinate of the block
     * @return Biome at the given coordinates
     * @see #getComputedBiome(int, int, int)
     */
    @NotNull
    Biome getBiome(int x, int y, int z);

    // Paper start
    /**
     * Gets the computed {@link Biome} at the given coordinates.
     *
     * <p>The computed Biome is the Biome as seen by clients for rendering
     * purposes and in the "F3" debug menu. This is computed by looking at the noise biome
     * at this and surrounding quarts and applying complex math operations.</p>
     *
     * <p>Most other Biome-related methods named getBiome, setBiome, and similar
     * operate on the "noise biome", which is stored per-quart, or in other words,
     * 1 Biome per 4x4x4 block region. This is how Biomes are currently generated and
     * stored on disk.</p>
     *
     * @param x X-coordinate of the block
     * @param y Y-coordinate of the block
     * @param z Z-coordinate of the block
     * @return Biome at the given coordinates
     */
    @NotNull
    Biome getComputedBiome(int x, int y, int z);
    // Paper end

    /**
     * Sets the {@link Biome} at the given {@link Location}.
     *
     * @param location the location of the biome
     * @param biome New Biome type for this block
     */
    default void setBiome(@NotNull Location location, @NotNull Biome biome) {
        this.setBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ(), biome);
    }

    /**
     * Sets the {@link Biome} for the given block coordinates
     *
     * @param x X-coordinate of the block
     * @param y Y-coordinate of the block
     * @param z Z-coordinate of the block
     * @param biome New Biome type for this block
     */
    void setBiome(int x, int y, int z, @NotNull Biome biome);

    /**
     * Gets the {@link BlockState} at the given {@link Location}.
     *
     * @param location The location of the block state
     * @return Block state at the given location
     */
    @NotNull
    default BlockState getBlockState(@NotNull Location location) {
        return this.getBlockState(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Gets the {@link BlockState} at the given coordinates.
     *
     * @param x X-coordinate of the block state
     * @param y Y-coordinate of the block state
     * @param z Z-coordinate of the block state
     * @return Block state at the given coordinates
     */
    @NotNull
    BlockState getBlockState(int x, int y, int z);

    // Paper start - FluidState API
    /**
     * Gets the {@link io.papermc.paper.block.fluid.FluidData} at the specified position.
     *
     * @param x The x-coordinate of the position
     * @param y The y-coordinate of the position
     * @param z The z-coordinate of the position
     * @return The {@link io.papermc.paper.block.fluid.FluidData} at the specified position
     */
    @NotNull
    io.papermc.paper.block.fluid.FluidData getFluidData(int x, int y, int z);

    /**
     * Gets the {@link io.papermc.paper.block.fluid.FluidData} at the given position
     *
     * @param position The position of the fluid
     * @return The fluid data at the given position
     */
    @NotNull
    default io.papermc.paper.block.fluid.FluidData getFluidData(@NotNull io.papermc.paper.math.Position position) {
        return getFluidData(position.blockX(), position.blockY(), position.blockZ());
    }

    /**
     * Gets the {@link io.papermc.paper.block.fluid.FluidData} at the given position
     *
     * @param location The location of the fluid
     * @return The fluid data at the given position
     */
    @NotNull
    default io.papermc.paper.block.fluid.FluidData getFluidData(@NotNull Location location) {
        return getFluidData(location.blockX(), location.blockY(), location.blockZ());
    }
    // Paper end

    /**
     * Gets the {@link BlockData} at the given {@link Location}.
     *
     * @param location The location of the block data
     * @return Block data at the given location
     */
    @NotNull
    default BlockData getBlockData(@NotNull Location location) {
        return this.getBlockData(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Gets the {@link BlockData} at the given coordinates.
     *
     * @param x X-coordinate of the block data
     * @param y Y-coordinate of the block data
     * @param z Z-coordinate of the block data
     * @return Block data at the given coordinates
     */
    @NotNull
    BlockData getBlockData(int x, int y, int z);

    /**
     * Gets the type of the block at the given {@link Location}.
     *
     * @param location The location of the block
     * @return Material at the given coordinates
     */
    @NotNull
    default Material getType(@NotNull Location location) {
        return this.getType(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    /**
     * Gets the type of the block at the given coordinates.
     *
     * @param x X-coordinate of the block
     * @param y Y-coordinate of the block
     * @param z Z-coordinate of the block
     * @return Material at the given coordinates
     */
    @NotNull
    Material getType(int x, int y, int z);

    /**
     * Sets the {@link BlockData} at the given {@link Location}.
     *
     * @param location The location of the block
     * @param blockData The block data to set the block to
     */
    default void setBlockData(@NotNull Location location, @NotNull BlockData blockData) {
        this.setBlockData(location.getBlockX(), location.getBlockY(), location.getBlockZ(), blockData);
    }

    /**
     * Sets the {@link BlockData} at the given coordinates.
     *
     * @param x X-coordinate of the block
     * @param y Y-coordinate of the block
     * @param z Z-coordinate of the block
     * @param blockData The block data to set the block to
     */
    void setBlockData(int x, int y, int z, @NotNull BlockData blockData);

    /**
     * Sets the {@link Material} at the given {@link Location}.
     *
     * @param location The location of the block
     * @param material The type to set the block to
     */
    default void setType(@NotNull Location location, @NotNull Material material) {
        this.setType(location.getBlockX(), location.getBlockY(), location.getBlockZ(), material);
    }

    /**
     * Sets the {@link Material} at the given coordinates.
     *
     * @param x X-coordinate of the block
     * @param y Y-coordinate of the block
     * @param z Z-coordinate of the block
     * @param material The type to set the block to
     */
    default void setType(int x, int y, int z, @NotNull Material material) {
        this.setBlockData(x, y, z, material.createBlockData());
    }

    /**
     * Creates a tree at the given {@link Location}
     *
     * @param location Location to spawn the tree
     * @param random Random to use to generate the tree
     * @param type Type of the tree to create
     * @return true if the tree was created successfully, otherwise false
     */
    boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type);

    /**
     * Creates a tree at the given {@link Location}
     * <p>
     * The provided consumer gets called for every block which gets changed
     * as a result of the tree generation. When the consumer gets called no
     * modifications to the world are done yet. Which means, that calling
     * {@link #getBlockState(Location)} in the consumer will return the state
     * of the block before the generation.
     * <p>
     * Modifications done to the {@link BlockState} in the consumer are respected,
     * which means that it is not necessary to call {@link BlockState#update()}
     *
     * @param location Location to spawn the tree
     * @param random Random to use to generate the tree
     * @param type Type of the tree to create
     * @param stateConsumer The consumer which should get called for every block which gets changed
     * @return true if the tree was created successfully, otherwise false
     */
    boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type, @Nullable Consumer<? super BlockState> stateConsumer);

    /**
     * Creates a tree at the given {@link Location}
     * <p>
     * The provided predicate gets called for every block which gets changed
     * as a result of the tree generation. When the predicate gets called no
     * modifications to the world are done yet. Which means, that calling
     * {@link #getBlockState(Location)} in the predicate will return the state
     * of the block before the generation.
     * <p>
     * If the predicate returns {@code true} the block gets set in the world.
     * If it returns {@code false} the block won't get set in the world.
     *
     * @param location Location to spawn the tree
     * @param random Random to use to generate the tree
     * @param type Type of the tree to create
     * @param statePredicate The predicate which should get used to test if a block should be set or not.
     * @return true if the tree was created successfully, otherwise false
     */
    boolean generateTree(@NotNull Location location, @NotNull Random random, @NotNull TreeType type, @Nullable Predicate<? super BlockState> statePredicate);

    /**
     * Creates an entity at the given {@link Location}
     *
     * @param location The location to spawn the entity
     * @param type The entity to spawn
     * @return Resulting Entity of this method
     */
    @NotNull
    default Entity spawnEntity(@NotNull Location location, @NotNull EntityType type) {
        return this.spawn(location, type.getEntityClass());
    }

    /**
     * Creates a new entity at the given {@link Location}.
     *
     * @param loc the location at which the entity will be spawned.
     * @param type the entity type that determines the entity to spawn.
     * @param randomizeData whether or not the entity's data should be randomised
     *                      before spawning. By default entities are randomised
     *                      before spawning in regards to their equipment, age,
     *                      attributes, etc.
     *                      An example of this randomization would be the color of
     *                      a sheep, random enchantments on the equipment of mobs
     *                      or even a zombie becoming a chicken jockey.
     *                      If set to false, the entity will not be randomised
     *                      before spawning, meaning all their data will remain
     *                      in their default state and not further modifications
     *                      to the entity will be made.
     *                      Notably only entities that extend the
     *                      {@link org.bukkit.entity.Mob} interface provide
     *                      randomization logic for their spawn.
     *                      This parameter is hence useless for any other type
     *                      of entity.
     * @return the spawned entity instance.
     */
    @NotNull
    public Entity spawnEntity(@NotNull Location loc, @NotNull EntityType type, boolean randomizeData);

    /**
     * Get a list of all entities in this RegionAccessor
     *
     * @return A List of all Entities currently residing in this world accessor
     */
    @NotNull
    List<Entity> getEntities();

    /**
     * Get a list of all living entities in this RegionAccessor
     *
     * @return A List of all LivingEntities currently residing in this world accessor
     */
    @NotNull
    List<LivingEntity> getLivingEntities();

    /**
     * Get a collection of all entities in this RegionAccessor matching the given
     * class/interface
     *
     * @param <T> an entity subclass
     * @param cls The class representing the type of entity to match
     * @return A List of all Entities currently residing in this world accessor
     *     that match the given class/interface
     */
    @NotNull
    <T extends Entity> Collection<T> getEntitiesByClass(@NotNull Class<T> cls);

    /**
     * Get a collection of all entities in this RegionAccessor matching any of the
     * given classes/interfaces
     *
     * @param classes The classes representing the types of entity to match
     * @return A List of all Entities currently residing in this world accessor
     *     that match one or more of the given classes/interfaces
     */
    @NotNull
    Collection<Entity> getEntitiesByClasses(@NotNull Class<?>... classes);

    /**
     * Creates an entity of a specific class at the given {@link Location} but
     * does not spawn it in the world.
     * <p>
     * <b>Note:</b> The created entity keeps a reference to the world it was
     * created in, care should be taken that the entity does not outlive the
     * world instance as this will lead to memory leaks.
     *
     * @param <T> the class of the {@link Entity} to create
     * @param location the {@link Location} to create the entity at
     * @param clazz the class of the {@link Entity} to spawn
     * @return an instance of the created {@link Entity}
     * @see #addEntity(Entity)
     * @see Entity#createSnapshot()
     */
    @NotNull
    <T extends Entity> T createEntity(@NotNull Location location, @NotNull Class<T> clazz);

    /**
     * Spawn an entity of a specific class at the given {@link Location}
     *
     * @param location the {@link Location} to spawn the entity at
     * @param clazz the class of the {@link Entity} to spawn
     * @param <T> the class of the {@link Entity} to spawn
     * @return an instance of the spawned {@link Entity}
     * @throws IllegalArgumentException if either parameter is null or the
     *     {@link Entity} requested cannot be spawned
     */
    @NotNull
    default <T extends Entity> T spawn(@NotNull Location location, @NotNull Class<T> clazz) throws IllegalArgumentException {
        return this.spawn(location, clazz, null, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    /**
     * Spawn an entity of a specific class at the given {@link Location}, with
     * the supplied function run before the entity is added to the world.
     * <br>
     * Note that when the function is run, the entity will not be actually in
     * the world. Any operation involving such as teleporting the entity is undefined
     * until after this function returns.
     *
     * @param location the {@link Location} to spawn the entity at
     * @param clazz the class of the {@link Entity} to spawn
     * @param function the function to be run before the entity is spawned.
     * @param <T> the class of the {@link Entity} to spawn
     * @return an instance of the spawned {@link Entity}
     * @throws IllegalArgumentException if either parameter is null or the
     *     {@link Entity} requested cannot be spawned
     */
    // Paper start
    default <T extends Entity> @NotNull T spawn(final @NotNull Location location, final @NotNull Class<T> clazz, final @Nullable Consumer<? super T> function) throws IllegalArgumentException {
        return this.spawn(location, clazz, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CUSTOM, function);
    }

    default @NotNull <T extends Entity> T spawn(final @NotNull Location location, final @NotNull Class<T> clazz, final org.bukkit.event.entity.CreatureSpawnEvent.@NotNull SpawnReason reason) throws IllegalArgumentException {
        return this.spawn(location, clazz, reason, null);
    }

    default @NotNull <T extends Entity> T spawn(final @NotNull Location location, final @NotNull Class<T> clazz, final org.bukkit.event.entity.CreatureSpawnEvent.@NotNull SpawnReason reason, final @Nullable Consumer<? super T> function) throws IllegalArgumentException {
        return this.spawn(location, clazz, function, reason);
    }

    default @NotNull Entity spawnEntity(final @NotNull Location loc, final @NotNull EntityType type, final org.bukkit.event.entity.CreatureSpawnEvent.@NotNull SpawnReason reason) {
        com.google.common.base.Preconditions.checkArgument(type.getEntityClass() != null, "%s is not a valid EntityType, must have an entity class", type);
        return this.spawn(loc, type.getEntityClass(), reason, null);
    }

    default @NotNull Entity spawnEntity(final @NotNull Location loc, final @NotNull EntityType type, final org.bukkit.event.entity.CreatureSpawnEvent.@NotNull SpawnReason reason, final @Nullable Consumer<? super Entity> function) {
        com.google.common.base.Preconditions.checkArgument(type.getEntityClass() != null, "%s is not a valid EntityType, must have an entity class", type);
        return this.spawn(loc, type.getEntityClass(), reason, function);
    }

    <T extends Entity> @NotNull T spawn(@NotNull Location location, @NotNull Class<T> clazz, @Nullable Consumer<? super T> function, org.bukkit.event.entity.CreatureSpawnEvent.@NotNull SpawnReason reason) throws IllegalArgumentException;
    // Paper end

    /**
     * Creates a new entity at the given {@link Location} with the supplied
     * function run before the entity is added to the world.
     * <br>
     * Note that when the function is run, the entity will not be actually in
     * the world. Any operation involving such as teleporting the entity is undefined
     * until after this function returns.
     * The passed function however is run after the potential entity's spawn
     * randomization and hence already allows access to the values of the mob,
     * whether or not those were randomized, such as attributes or the entity
     * equipment.
     *
     * @param location      the location at which the entity will be spawned.
     * @param clazz         the class of the {@link Entity} that is to be spawned.
     * @param <T>           the generic type of the entity that is being created.
     * @param randomizeData whether or not the entity's data should be randomised
     *                      before spawning. By default entities are randomised
     *                      before spawning in regards to their equipment, age,
     *                      attributes, etc.
     *                      An example of this randomization would be the color of
     *                      a sheep, random enchantments on the equipment of mobs
     *                      or even a zombie becoming a chicken jockey.
     *                      If set to false, the entity will not be randomised
     *                      before spawning, meaning all their data will remain
     *                      in their default state and not further modifications
     *                      to the entity will be made.
     *                      Notably only entities that extend the
     *                      {@link org.bukkit.entity.Mob} interface provide
     *                      randomization logic for their spawn.
     *                      This parameter is hence useless for any other type
     *                      of entity.
     * @param function      the function to be run before the entity is spawned.
     * @return the spawned entity instance.
     * @throws IllegalArgumentException if either the world or clazz parameter are null.
     */
    @NotNull
    public <T extends Entity> T spawn(@NotNull Location location, @NotNull Class<T> clazz, boolean randomizeData, @Nullable Consumer<? super T> function) throws IllegalArgumentException;

    /**
     * Gets the highest non-empty (impassable) coordinate at the given
     * coordinates.
     *
     * @param x X-coordinate of the blocks
     * @param z Z-coordinate of the blocks
     * @return Y-coordinate of the highest non-empty block
     */
    public int getHighestBlockYAt(int x, int z);

    /**
     * Gets the highest non-empty (impassable) coordinate at the given
     * {@link Location}.
     *
     * @param location Location of the blocks
     * @return Y-coordinate of the highest non-empty block
     */
    public int getHighestBlockYAt(@NotNull Location location);

    /**
     * Gets the highest coordinate corresponding to the {@link HeightMap} at the
     * given coordinates.
     *
     * @param x X-coordinate of the blocks
     * @param z Z-coordinate of the blocks
     * @param heightMap the heightMap that is used to determine the highest
     * point
     *
     * @return Y-coordinate of the highest block corresponding to the
     * {@link HeightMap}
     */
    public int getHighestBlockYAt(int x, int z, @NotNull HeightMap heightMap);

    /**
     * Gets the highest coordinate corresponding to the {@link HeightMap} at the
     * given {@link Location}.
     *
     * @param location Location of the blocks
     * @param heightMap the heightMap that is used to determine the highest
     * point
     * @return Y-coordinate of the highest block corresponding to the
     * {@link HeightMap}
     */
    public int getHighestBlockYAt(@NotNull Location location, @NotNull HeightMap heightMap);

    /**
     * Spawns a previously created entity in the world. <br>
     * The provided entity must not have already been spawned in a world.
     *
     * @param <T> the generic type of the entity that is being added.
     * @param entity the entity to add
     * @return the entity now in the world
     */
    @NotNull
    public <T extends Entity> T addEntity(@NotNull T entity);

    // Paper start
    /**
     * @return the current moon phase at the current time in the world
     */
    @NotNull
    io.papermc.paper.world.MoonPhase getMoonPhase();

    /**
     * Get the world's key
     *
     * @return the world's key
     */
    @NotNull
    @Override
    NamespacedKey getKey();

    /**
     * Tell whether a line of sight exists between the given locations
     * @param from Location to start at
     * @param to target Location
     * @return whether a line of sight exists between {@code from} and {@code to}
     */
    public boolean lineOfSightExists(@NotNull Location from, @NotNull Location to);

    /**
     * Checks if the world collides with the given boundingbox.
     * This will check for any colliding hard entities (boats, shulkers) / worldborder / blocks.
     * Does not load chunks that are within the bounding box.
     *
     * @param boundingBox the box to check collisions in
     * @return collides or not
     */
    boolean hasCollisionsIn(@NotNull org.bukkit.util.BoundingBox boundingBox);
    // Paper end
}
