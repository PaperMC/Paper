package org.bukkit;

import com.google.common.base.Preconditions;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.Rotation;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a 3-dimensional position in a world.
 * <br>
 * No constraints are placed on any angular values other than that they be
 * specified in degrees. This means that negative angles or angles of greater
 * magnitude than 360 are valid, but may be normalized to any other equivalent
 * representation by the implementation.
 */
public class Location implements Cloneable, ConfigurationSerializable, io.papermc.paper.math.FinePosition {
    private Reference<World> world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    /**
     * Constructs a new Location with the given coordinates
     *
     * @param world The world in which this location resides
     * @param x The x-coordinate of this new location
     * @param y The y-coordinate of this new location
     * @param z The z-coordinate of this new location
     */
    public Location(@UndefinedNullability final World world, final double x, final double y, final double z) {
        this(world, x, y, z, 0, 0);
    }

    /**
     * Constructs a new Location with the given coordinates and direction
     *
     * @param world The world in which this location resides
     * @param x The x-coordinate of this new location
     * @param y The y-coordinate of this new location
     * @param z The z-coordinate of this new location
     * @param yaw The absolute rotation on the x-plane, in degrees
     * @param pitch The absolute rotation on the y-plane, in degrees
     */
    public Location(@UndefinedNullability final World world, final double x, final double y, final double z, final float yaw, final float pitch) {
        if (world != null) {
            this.world = new WeakReference<>(world);
        }

        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    /**
     * Sets the world that this location resides in
     *
     * @param world New world that this location resides in
     */
    public void setWorld(@Nullable World world) {
        this.world = (world == null) ? null : new WeakReference<>(world);
    }

    /**
     * Checks if world in this location is present and loaded.
     *
     * @return true if is loaded, otherwise false
     */
    public boolean isWorldLoaded() {
        if (this.world == null) {
            return false;
        }

        World world = this.world.get();
        return world != null && world.equals(Bukkit.getWorld(world.getUID()));
    }

    /**
     * Gets the world that this location resides in
     *
     * @return World that contains this location, or {@code null} if it is not set
     * @throws IllegalArgumentException when world is unloaded
     * @see #isWorldLoaded()
     */
    @UndefinedNullability
    public World getWorld() {
        if (this.world == null) {
            return null;
        }

        World world = this.world.get();
        Preconditions.checkArgument(world != null, "World unloaded");
        return world;
    }

    /**
     * Gets the chunk at the represented location
     *
     * @return Chunk at the represented location
     */
    @NotNull
    public Chunk getChunk() {
        return getWorld().getChunkAt(this);
    }

    /**
     * Gets the block at the represented location
     *
     * @return Block at the represented location
     */
    @NotNull
    public Block getBlock() {
        return getWorld().getBlockAt(this);
    }

    /**
     * Sets the x-coordinate of this location
     *
     * @param x X-coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the x-coordinate of this location
     *
     * @return x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the floored value of the X component, indicating the block that
     * this location is contained with.
     *
     * @return block X
     */
    public int getBlockX() {
        return locToBlock(x);
    }

    /**
     * Sets the y-coordinate of this location
     *
     * @param y y-coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Gets the y-coordinate of this location
     *
     * @return y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the floored value of the Y component, indicating the block that
     * this location is contained with.
     *
     * @return block y
     */
    public int getBlockY() {
        return locToBlock(y);
    }

    /**
     * Sets the z-coordinate of this location
     *
     * @param z z-coordinate
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Gets the z-coordinate of this location
     *
     * @return z-coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Gets the floored value of the Z component, indicating the block that
     * this location is contained with.
     *
     * @return block z
     */
    public int getBlockZ() {
        return locToBlock(z);
    }

    /**
     * Sets the yaw of this location, measured in degrees.
     * <ul>
     * <li>A yaw of 0 or 360 represents the positive z direction.
     * <li>A yaw of 180 represents the negative z direction.
     * <li>A yaw of 90 represents the negative x direction.
     * <li>A yaw of 270 represents the positive x direction.
     * </ul>
     * Increasing yaw values are the equivalent of turning to your
     * right-facing, increasing the scale of the next respective axis, and
     * decreasing the scale of the previous axis.
     *
     * @param yaw new rotation's yaw
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    /**
     * Gets the yaw of this location, measured in degrees.
     * <ul>
     * <li>A yaw of 0 or 360 represents the positive z direction.
     * <li>A yaw of 180 represents the negative z direction.
     * <li>A yaw of 90 represents the negative x direction.
     * <li>A yaw of 270 represents the positive x direction.
     * </ul>
     * Increasing yaw values are the equivalent of turning to your
     * right-facing, increasing the scale of the next respective axis, and
     * decreasing the scale of the previous axis.
     *
     * @return the rotation's yaw
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Sets the pitch of this location, measured in degrees.
     * <ul>
     * <li>A pitch of 0 represents level forward facing.
     * <li>A pitch of 90 represents downward facing, or negative y
     *     direction.
     * <li>A pitch of -90 represents upward facing, or positive y direction.
     * </ul>
     * Increasing pitch values the equivalent of looking down.
     *
     * @param pitch new incline's pitch
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * Gets the pitch of this location, measured in degrees.
     * <ul>
     * <li>A pitch of 0 represents level forward facing.
     * <li>A pitch of 90 represents downward facing, or negative y
     *     direction.
     * <li>A pitch of -90 represents upward facing, or positive y direction.
     * </ul>
     * Increasing pitch values the equivalent of looking down.
     *
     * @return the incline's pitch
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Gets a unit-vector pointing in the direction that this Location is
     * facing.
     *
     * @return a vector pointing the direction of this location's {@link
     *     #getPitch() pitch} and {@link #getYaw() yaw}
     */
    @NotNull
    public Vector getDirection() {
        Vector vector = new Vector();

        double rotX = this.getYaw();
        double rotY = this.getPitch();

        vector.setY(-Math.sin(Math.toRadians(rotY)));

        double xz = Math.cos(Math.toRadians(rotY));

        vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
        vector.setZ(xz * Math.cos(Math.toRadians(rotX)));

        return vector;
    }

    /**
     * Sets the {@link #getYaw() yaw} and {@link #getPitch() pitch} to point
     * in the direction of the vector.
     *
     * @param vector the direction vector
     * @return the same location
     */
    @NotNull
    public Location setDirection(@NotNull Vector vector) {
        /*
         * Sin = Opp / Hyp
         * Cos = Adj / Hyp
         * Tan = Opp / Adj
         *
         * x = -Opp
         * z = Adj
         */
        final double _2PI = 2 * Math.PI;
        final double x = vector.getX();
        final double z = vector.getZ();

        if (x == 0 && z == 0) {
            pitch = vector.getY() > 0 ? -90 : 90;
            return this;
        }

        double theta = Math.atan2(-x, z);
        yaw = (float) Math.toDegrees((theta + _2PI) % _2PI);

        double x2 = NumberConversions.square(x);
        double z2 = NumberConversions.square(z);
        double xz = Math.sqrt(x2 + z2);
        pitch = (float) Math.toDegrees(Math.atan(-vector.getY() / xz));

        return this;
    }

    /**
     * Adds the location by another.
     *
     * @param vec The other location
     * @return the same location
     * @throws IllegalArgumentException for differing worlds
     * @see Vector
     */
    @NotNull
    public Location add(@NotNull Location vec) {
        if (vec == null || vec.getWorld() != getWorld()) {
            throw new IllegalArgumentException("Cannot add Locations of differing worlds");
        }

        x += vec.x;
        y += vec.y;
        z += vec.z;
        return this;
    }

    /**
     * Adds the location by a vector.
     *
     * @param vec Vector to use
     * @return the same location
     * @see Vector
     */
    @NotNull
    public Location add(@NotNull Vector vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
        return this;
    }

    /**
     * Adds the location by another. Not world-aware.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return the same location
     * @see Vector
     */
    @NotNull
    public Location add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    /**
     * Adds rotation in the form of yaw and patch to this location. Not world-aware.
     *
     * @param yaw   yaw, measured in degrees.
     * @param pitch pitch, measured in degrees.
     * @return the same location
     * @see Vector
     */
    @NotNull
    @Contract(value = "_,_ -> this", mutates = "this")
    public Location addRotation(final float yaw, final float pitch) {
        this.yaw += yaw;
        this.pitch += pitch;
        return this;
    }

    /**
     * Adds rotation to this location. Not world-aware.
     *
     * @param rotation the rotation to add.
     * @return the same location
     * @see Vector
     */
    @NotNull
    @Contract(value = "_ -> this", mutates = "this")
    public Location addRotation(@NotNull Rotation rotation) {
        return addRotation(rotation.yaw(), rotation.pitch());
    }

    /**
     * Retrieves the rotation of this location.
     *
     * @return a new {@code Rotation} object
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public Rotation getRotation() {
        return Rotation.rotation(yaw, pitch);
    }

    /**
     * Subtracts the location by another.
     *
     * @param vec The other location
     * @return the same location
     * @throws IllegalArgumentException for differing worlds
     * @see Vector
     */
    @NotNull
    public Location subtract(@NotNull Location vec) {
        if (vec == null || vec.getWorld() != getWorld()) {
            throw new IllegalArgumentException("Cannot add Locations of differing worlds");
        }

        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        return this;
    }

    /**
     * Subtracts the location by a vector.
     *
     * @param vec The vector to use
     * @return the same location
     * @see Vector
     */
    @NotNull
    public Location subtract(@NotNull Vector vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
        this.z -= vec.getZ();
        return this;
    }

    /**
     * Subtracts the location by another. Not world-aware and
     * orientation independent.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return the same location
     * @see Vector
     */
    @NotNull
    public Location subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    /**
     * Subtracts rotation in the form of yaw and patch from this location.
     *
     * @param yaw   yaw, measured in degrees.
     * @param pitch pitch, measured in degrees.
     * @return the same location
     * @see Vector
     */
    @NotNull
    @Contract(value = "_,_ -> this", mutates = "this")
    public Location subtractRotation(final float yaw, final float pitch) {
        this.yaw -= yaw;
        this.pitch -= pitch;
        return this;
    }

    /**
     * Subtracts rotation from this location.
     *
     * @param rotation the rotation to subtract.
     * @return the same location
     * @see Vector
     */
    @NotNull
    @Contract(value = "_ -> this", mutates = "this")
    public Location subtractRotation(@NotNull Rotation rotation) {
        return subtractRotation(rotation.yaw(), rotation.pitch());
    }

    /**
     * Gets the magnitude of the location, defined as sqrt(x^2+y^2+z^2). The
     * value of this method is not cached and uses a costly square-root
     * function, so do not repeatedly call this method to get the location's
     * magnitude. NaN will be returned if the inner result of the sqrt()
     * function overflows, which will be caused if the length is too long. Not
     * world-aware and orientation independent.
     *
     * @return the magnitude
     * @see Vector
     */
    public double length() {
        return Math.sqrt(NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z));
    }

    /**
     * Gets the magnitude of the location squared. Not world-aware and
     * orientation independent.
     *
     * @return the magnitude
     * @see Vector
     */
    public double lengthSquared() {
        return NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z);
    }

    /**
     * Get the distance between this location and another. The value of this
     * method is not cached and uses a costly square-root function, so do not
     * repeatedly call this method to get the location's magnitude. NaN will
     * be returned if the inner result of the sqrt() function overflows, which
     * will be caused if the distance is too long.
     *
     * @param o The other location
     * @return the distance
     * @throws IllegalArgumentException for differing worlds
     * @see Vector
     */
    public double distance(@NotNull Location o) {
        return Math.sqrt(distanceSquared(o));
    }

    /**
     * Get the squared distance between this location and another.
     *
     * @param o The other location
     * @return the distance
     * @throws IllegalArgumentException for differing worlds
     * @see Vector
     */
    public double distanceSquared(@NotNull Location o) {
        if (o == null) {
            throw new IllegalArgumentException("Cannot measure distance to a null location");
        } else if (o.getWorld() == null || getWorld() == null) {
            throw new IllegalArgumentException("Cannot measure distance to a null world");
        } else if (o.getWorld() != getWorld()) {
            throw new IllegalArgumentException("Cannot measure distance between " + getWorld().getName() + " and " + o.getWorld().getName());
        }

        return NumberConversions.square(x - o.x) + NumberConversions.square(y - o.y) + NumberConversions.square(z - o.z);
    }

    /**
     * Performs scalar multiplication, multiplying all components with a
     * scalar. Not world-aware.
     *
     * @param m The factor
     * @return the same location
     * @see Vector
     */
    @NotNull
    public Location multiply(double m) {
        x *= m;
        y *= m;
        z *= m;
        return this;
    }

    /**
     * Zero this location's components. Not world-aware.
     *
     * @return the same location
     * @see Vector
     */
    @NotNull
    public Location zero() {
        x = 0;
        y = 0;
        z = 0;
        return this;
    }

    public boolean isChunkLoaded() {
        return this.getWorld().isChunkLoaded(locToBlock(x) >> 4, locToBlock(z) >> 4);
    }

    /**
     * Checks if a {@link Chunk} has been generated at this location.
     *
     * @return true if a chunk has been generated at this location
     */
    public boolean isGenerated() {
        World world = this.getWorld();
        Preconditions.checkNotNull(world, "Location has no world!");
        return world.isChunkGenerated(locToBlock(x) >> 4, locToBlock(z) >> 4);
    }

    /**
     * Sets the position of this Location and returns itself
     * <p>
     * This mutates this object, clone first.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return self (not cloned)
     */
    @NotNull
    public Location set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Sets the rotation of this location and returns itself.
     * <p>
     * This mutates this object, clone first.
     *
     * @param yaw   yaw, measured in degrees.
     * @param pitch pitch, measured in degrees.
     * @return self (not cloned)
     */
    @NotNull
    @Contract(value = "_,_ -> this", mutates = "this")
    public Location setRotation(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
        return this;
    }

    /**
     * Sets the rotation of this location and returns itself.
     * <p>
     * This mutates this object, clone first.
     *
     * @param rotation the new rotation.
     * @return self (not cloned)
     */
    @NotNull
    @Contract(value = "_ -> this", mutates = "this")
    public Location setRotation(@NotNull Rotation rotation) {
        return setRotation(rotation.yaw(), rotation.pitch());
    }

    /**
     * Takes the x/y/z from base and adds the specified x/y/z to it and returns self
     * <p>
     * This mutates this object, clone first.
     *
     * @param base The base coordinate to modify
     * @param x X coordinate to add to base
     * @param y Y coordinate to add to base
     * @param z Z coordinate to add to base
     * @return self (not cloned)
     */
    @NotNull
    public Location add(@NotNull Location base, double x, double y, double z) {
        return this.set(base.x + x, base.y + y, base.z + z);
    }

    /**
     * Takes the x/y/z from base and subtracts the specified x/y/z to it and returns self
     * <p>
     * This mutates this object, clone first.
     *
     * @param base The base coordinate to modify
     * @param x X coordinate to subtract from base
     * @param y Y coordinate to subtract from base
     * @param z Z coordinate to subtract from base
     * @return self (not cloned)
     */
    @NotNull
    public Location subtract(@NotNull Location base, double x, double y, double z) {
        return this.set(base.x - x, base.y - y, base.z - z);
    }

    /**
     * @return A new location where X/Y/Z are on the Block location (integer value of X/Y/Z)
     */
    @NotNull
    public Location toBlockLocation() {
        Location blockLoc = clone();
        blockLoc.setX(getBlockX());
        blockLoc.setY(getBlockY());
        blockLoc.setZ(getBlockZ());
        return blockLoc;
    }

    /**
     * @return The block key for this location's block location.
     * @see Block#getBlockKey(int, int, int)
     * @deprecated only encodes y block ranges from -512 to 511 and represents an already changed implementation detail
     */
    @Deprecated(since = "1.18.1")
    public long toBlockKey() {
        return Block.getBlockKey(getBlockX(), getBlockY(), getBlockZ());
    }

    /**
     * @return A new location where X/Y/Z are the center of the block
     */
    @NotNull
    public Location toCenterLocation() {
        Location centerLoc = clone();
        centerLoc.setX(getBlockX() + 0.5);
        centerLoc.setY(getBlockY() + 0.5);
        centerLoc.setZ(getBlockZ() + 0.5);
        return centerLoc;
    }

    /**
     * Returns a copy of this location except with y = getWorld().getHighestBlockYAt(this.getBlockX(), this.getBlockZ())
     * @return A copy of this location except with y = getWorld().getHighestBlockYAt(this.getBlockX(), this.getBlockZ())
     * @throws NullPointerException if {@link #getWorld()} is {@code null}
     */
    @NotNull
    public Location toHighestLocation() {
        return this.toHighestLocation(HeightMap.WORLD_SURFACE);
    }

    /**
     * Returns a copy of this location except with y = getWorld().getHighestBlockYAt(this.getBlockX(), this.getBlockZ(), heightMap)
     * @param heightMap The heightmap to use for finding the highest y location.
     * @return A copy of this location except with y = getWorld().getHighestBlockYAt(this.getBlockX(), this.getBlockZ(), heightMap)
     */
    @NotNull
    public Location toHighestLocation(@NotNull final HeightMap heightMap) {
        final Location ret = this.clone();
        ret.setY(this.getWorld().getHighestBlockYAt(this, heightMap));
        return ret;
    }

    /**
     * Creates explosion at this location with given power
     * <p>
     * Will break blocks and ignite blocks on fire.
     *
     * @param power The power of explosion, where 4F is TNT
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(float power) {
        return this.getWorld().createExplosion(this, power);
    }

    /**
     * Creates explosion at this location with given power and optionally
     * setting blocks on fire.
     * <p>
     * Will break blocks.
     *
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether to set blocks on fire
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(float power, boolean setFire) {
        return this.getWorld().createExplosion(this, power, setFire);
    }

    /**
     * Creates explosion at this location with given power and optionally
     * setting blocks on fire.
     *
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether to set blocks on fire
     * @param breakBlocks Whether to have blocks be destroyed
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(float power, boolean setFire, boolean breakBlocks) {
        return this.getWorld().createExplosion(this, power, setFire, breakBlocks);
    }

    /**
     * Creates explosion at this location with given power, with the specified entity as the source.
     * <p>
     * Will break blocks and ignite blocks on fire.
     *
     * @param source The source entity of the explosion
     * @param power The power of explosion, where 4F is TNT
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(@Nullable Entity source, float power) {
        return this.getWorld().createExplosion(source, this, power, true, true);
    }

    /**
     * Creates explosion at this location with given power and optionally
     * setting blocks on fire, with the specified entity as the source.
     * <p>
     * Will break blocks.
     *
     * @param source The source entity of the explosion
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether to set blocks on fire
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(@Nullable Entity source, float power, boolean setFire) {
        return this.getWorld().createExplosion(source, this, power, setFire, true);
    }

    /**
     * Creates explosion at this location with given power and optionally
     * setting blocks on fire, with the specified entity as the source.
     *
     * @param source The source entity of the explosion
     * @param power The power of explosion, where 4F is TNT
     * @param setFire Whether to set blocks on fire
     * @param breakBlocks Whether to have blocks be destroyed
     * @return false if explosion was canceled, otherwise true
     */
    public boolean createExplosion(@Nullable Entity source, float power, boolean setFire, boolean breakBlocks) {
        return this.getWorld().createExplosion(source, this, power, setFire, breakBlocks);
    }

    /**
     * Returns a list of entities within a bounding box centered around a Location.
     * <p>
     * Some implementations may impose artificial restrictions on the size of the search bounding box.
     *
     * @param x 1/2 the size of the box along the x-axis
     * @param y 1/2 the size of the box along the y-axis
     * @param z 1/2 the size of the box along the z-axis
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    public @NotNull Collection<Entity> getNearbyEntities(final double x, final double y, final double z) {
        final World world = this.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location has no world");
        }
        return world.getNearbyEntities(this, x, y, z);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param radius X Radius
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(final double radius) {
        return this.getNearbyEntitiesByType(LivingEntity.class, radius, radius, radius);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param xzRadius X/Z Radius
     * @param yRadius Y Radius
     * @return the collection of living entities near location. This will always be a non-null collection.
     */
    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(final double xzRadius, final double yRadius) {
        return this.getNearbyEntitiesByType(LivingEntity.class, xzRadius, yRadius, xzRadius);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param xRadius X Radius
     * @param yRadius Y Radius
     * @param zRadius Z radius
     * @return the collection of living entities near location. This will always be a non-null collection.
     */
    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(final double xRadius, final double yRadius, final double zRadius) {
        return this.getNearbyEntitiesByType(LivingEntity.class, xRadius, yRadius, zRadius);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param radius Radius
     * @param predicate a predicate used to filter results
     * @return the collection of living entities near location. This will always be a non-null collection.
     */
    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(final double radius, final @Nullable Predicate<? super LivingEntity> predicate) {
        return this.getNearbyEntitiesByType(LivingEntity.class, radius, radius, radius, predicate);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param xzRadius X/Z Radius
     * @param yRadius Y Radius
     * @param predicate a predicate used to filter results
     * @return the collection of living entities near location. This will always be a non-null collection.
     */
    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(final double xzRadius, final double yRadius, final @Nullable Predicate<? super LivingEntity> predicate) {
        return this.getNearbyEntitiesByType(LivingEntity.class, xzRadius, yRadius, xzRadius, predicate);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param xRadius X Radius
     * @param yRadius Y Radius
     * @param zRadius Z radius
     * @param predicate a predicate used to filter results
     * @return the collection of living entities near location. This will always be a non-null collection.
     */
    public @NotNull Collection<LivingEntity> getNearbyLivingEntities(final double xRadius, final double yRadius, final double zRadius, final @Nullable Predicate<? super LivingEntity> predicate) {
        return this.getNearbyEntitiesByType(LivingEntity.class, xRadius, yRadius, zRadius, predicate);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param radius X/Y/Z Radius
     * @return the collection of players near location. This will always be a non-null collection.
     */
    public @NotNull Collection<Player> getNearbyPlayers(final double radius) {
        return this.getNearbyEntitiesByType(Player.class, radius, radius, radius);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param xzRadius X/Z Radius
     * @param yRadius Y Radius
     * @return the collection of players near location. This will always be a non-null collection.
     */
    public @NotNull Collection<Player> getNearbyPlayers(final double xzRadius, final double yRadius) {
        return this.getNearbyEntitiesByType(Player.class, xzRadius, yRadius, xzRadius);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param xRadius X Radius
     * @param yRadius Y Radius
     * @param zRadius Z Radius
     * @return the collection of players near location. This will always be a non-null collection.
     */
    public @NotNull Collection<Player> getNearbyPlayers(final double xRadius, final double yRadius, final double zRadius) {
        return this.getNearbyEntitiesByType(Player.class, xRadius, yRadius, zRadius);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param radius X/Y/Z Radius
     * @param predicate a predicate used to filter results
     * @return the collection of players near location. This will always be a non-null collection.
     */
    public @NotNull Collection<Player> getNearbyPlayers(final double radius, final @Nullable Predicate<? super Player> predicate) {
        return this.getNearbyEntitiesByType(Player.class, radius, radius, radius, predicate);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param xzRadius X/Z Radius
     * @param yRadius Y Radius
     * @param predicate a predicate used to filter results
     * @return the collection of players near location. This will always be a non-null collection.
     */
    public @NotNull Collection<Player> getNearbyPlayers(final double xzRadius, final double yRadius, final @Nullable Predicate<? super Player> predicate) {
        return this.getNearbyEntitiesByType(Player.class, xzRadius, yRadius, xzRadius, predicate);
    }

    /**
     * Gets nearby players within the specified radius (bounding box)
     *
     * @param xRadius X Radius
     * @param yRadius Y Radius
     * @param zRadius Z Radius
     * @param predicate a predicate used to filter results
     * @return the collection of players near location. This will always be a non-null collection.
     */
    public @NotNull Collection<Player> getNearbyPlayers(final double xRadius, final double yRadius, final double zRadius, final @Nullable Predicate<? super Player> predicate) {
        return this.getNearbyEntitiesByType(Player.class, xRadius, yRadius, zRadius, predicate);
    }

    /**
     * Gets all nearby entities of the specified type, within the specified radius (bounding box)
     *
     * @param clazz Type to filter by
     * @param radius X/Y/Z radius to search within
     * @param <T> the entity type
     * @return the collection of entities of type clazz near location. This will always be a non-null collection.
     */
    public @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(final @Nullable Class<? extends T> clazz, final double radius) {
        return this.getNearbyEntitiesByType(clazz, radius, radius, radius, null);
    }

    /**
     * Gets all nearby entities of the specified type, within the specified radius, with x and x radius matching (bounding box)
     *
     * @param clazz Type to filter by
     * @param xzRadius X/Z radius to search within
     * @param yRadius Y radius to search within
     * @param <T> the entity type
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    public @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(final @Nullable Class<? extends T> clazz, final double xzRadius, final double yRadius) {
        return this.getNearbyEntitiesByType(clazz, xzRadius, yRadius, xzRadius, null);
    }

    /**
     * Gets all nearby entities of the specified type, within the specified radius (bounding box)
     *
     * @param clazz Type to filter by
     * @param xRadius X Radius
     * @param yRadius Y Radius
     * @param zRadius Z Radius
     * @param <T> the entity type
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    public @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(final @Nullable Class<? extends T> clazz, final double xRadius, final double yRadius, final double zRadius) {
        return this.getNearbyEntitiesByType(clazz, xRadius, yRadius, zRadius, null);
    }

    /**
     * Gets all nearby entities of the specified type, within the specified radius (bounding box)
     *
     * @param clazz Type to filter by
     * @param radius X/Y/Z radius to search within
     * @param predicate a predicate used to filter results
     * @param <T> the entity type
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    public @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(final @Nullable Class<? extends T> clazz, final double radius, final @Nullable Predicate<? super T> predicate) {
        return this.getNearbyEntitiesByType(clazz, radius, radius, radius, predicate);
    }

    /**
     * Gets all nearby entities of the specified type, within the specified radius, with x and x radius matching (bounding box)
     *
     * @param clazz Type to filter by
     * @param xzRadius X/Z radius to search within
     * @param yRadius Y radius to search within
     * @param predicate a predicate used to filter results
     * @param <T> the entity type
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    public @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(final @Nullable Class<? extends T> clazz, final double xzRadius, final double yRadius, final @Nullable Predicate<? super T> predicate) {
        return this.getNearbyEntitiesByType(clazz, xzRadius, yRadius, xzRadius, predicate);
    }

    /**
     * Gets all nearby entities of the specified type, within the specified radius (bounding box)
     *
     * @param clazz Type to filter by
     * @param xRadius X Radius
     * @param yRadius Y Radius
     * @param zRadius Z Radius
     * @param predicate a predicate used to filter results
     * @param <T> the entity type
     * @return the collection of entities near location. This will always be a non-null collection.
     */
    public @NotNull <T extends Entity> Collection<T> getNearbyEntitiesByType(final @Nullable Class<? extends T> clazz, final double xRadius, final double yRadius, final double zRadius, final @Nullable Predicate<? super T> predicate) {
        final World world = this.getWorld();
        if (world == null) {
            throw new IllegalArgumentException("Location has no world");
        }
        return world.getNearbyEntitiesByType(clazz, this, xRadius, yRadius, zRadius, predicate);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;

        World world = (this.world == null) ? null : this.world.get();
        World otherWorld = (other.world == null) ? null : other.world.get();
        if (world != otherWorld && (world == null || !world.equals(otherWorld))) {
            return false;
        }
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
            return false;
        }
        if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch)) {
            return false;
        }
        if (Float.floatToIntBits(this.yaw) != Float.floatToIntBits(other.yaw)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;

        World world = (this.world == null) ? null : this.world.get();
        hash = 19 * hash + (world != null ? world.hashCode() : 0);
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 19 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        hash = 19 * hash + Float.floatToIntBits(this.pitch);
        hash = 19 * hash + Float.floatToIntBits(this.yaw);
        return hash;
    }

    @Override
    public String toString() {
        World world = (this.world == null) ? null : this.world.get();
        return "Location{" + "world=" + world + ",x=" + x + ",y=" + y + ",z=" + z + ",pitch=" + pitch + ",yaw=" + yaw + '}';
    }

    /**
     * Constructs a new {@link Vector} based on this Location
     *
     * @return New Vector containing the coordinates represented by this
     *     Location
     */
    @NotNull
    public Vector toVector() {
        return new Vector(x, y, z);
    }

    @Override
    @NotNull
    public Location clone() {
        try {
            return (Location) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    /**
     * Check if each component of this Location is finite.
     *
     * @throws IllegalArgumentException if any component is not finite
     */
    public void checkFinite() throws IllegalArgumentException {
        NumberConversions.checkFinite(x, "x not finite");
        NumberConversions.checkFinite(y, "y not finite");
        NumberConversions.checkFinite(z, "z not finite");
        NumberConversions.checkFinite(pitch, "pitch not finite");
        NumberConversions.checkFinite(yaw, "yaw not finite");
    }

    /**
     * Safely converts a double (location coordinate) to an int (block
     * coordinate)
     *
     * @param loc Precise coordinate
     * @return Block coordinate
     */
    public static int locToBlock(double loc) {
        return NumberConversions.floor(loc);
    }

    @Override
    @Utility
    @NotNull
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();

        if (this.world != null) {
            data.put("world", getWorld().getName());
        }

        data.put("x", this.x);
        data.put("y", this.y);
        data.put("z", this.z);

        data.put("yaw", this.yaw);
        data.put("pitch", this.pitch);

        return data;
    }

    /**
     * Required method for deserialization
     *
     * @param args map to deserialize
     * @return deserialized location
     * @throws IllegalArgumentException if the world don't exists
     * @see ConfigurationSerializable
     */
    @NotNull
    public static Location deserialize(@NotNull Map<String, Object> args) {
        World world = null;
        if (args.containsKey("world")) {
            world = Bukkit.getWorld((String) args.get("world"));
            if (world == null) {
                throw new IllegalArgumentException("unknown world");
            }
        }

        return new Location(world, NumberConversions.toDouble(args.get("x")), NumberConversions.toDouble(args.get("y")), NumberConversions.toDouble(args.get("z")), NumberConversions.toFloat(args.get("yaw")), NumberConversions.toFloat(args.get("pitch")));
    }

    /**
     * Normalizes the given yaw angle to a value between <code>+/-180</code>
     * degrees.
     *
     * @param yaw the yaw in degrees
     * @return the normalized yaw in degrees
     * @see Location#getYaw()
     */
    public static float normalizeYaw(float yaw) {
        yaw %= 360.0f;
        if (yaw >= 180.0f) {
            yaw -= 360.0f;
        } else if (yaw < -180.0f) {
            yaw += 360.0f;
        }
        return yaw;
    }

    /**
     * Normalizes the given pitch angle to a value between <code>+/-90</code>
     * degrees.
     *
     * @param pitch the pitch in degrees
     * @return the normalized pitch in degrees
     * @see Location#getPitch()
     */
    public static float normalizePitch(float pitch) {
        if (pitch > 90.0f) {
            pitch = 90.0f;
        } else if (pitch < -90.0f) {
            pitch = -90.0f;
        }
        return pitch;
    }

    @Override
    public double x() {
        return this.getX();
    }

    @Override
    public double y() {
        return this.getY();
    }

    @Override
    public double z() {
        return this.getZ();
    }

    @Override
    public boolean isFinite() {
        return FinePosition.super.isFinite() && Float.isFinite(this.getYaw()) && Float.isFinite(this.getPitch());
    }

    @Override
    public @NotNull Location toLocation(@NotNull World world) {
        return new Location(world, this.x(), this.y(), this.z(), this.getYaw(), this.getPitch());
    }
}
