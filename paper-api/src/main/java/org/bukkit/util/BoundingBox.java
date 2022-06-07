package org.bukkit.util;

import com.google.common.base.Preconditions;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A mutable axis aligned bounding box (AABB).
 * <p>
 * This basically represents a rectangular box (specified by minimum and maximum
 * corners) that can for example be used to describe the position and extents of
 * an object (such as an entity, block, or rectangular region) in 3D space. Its
 * edges and faces are parallel to the axes of the cartesian coordinate system.
 * <p>
 * The bounding box may be degenerate (one or more sides having the length 0).
 * <p>
 * Because bounding boxes are mutable, storing them long term may be dangerous
 * if they get modified later. If you want to keep around a bounding box, it may
 * be wise to call {@link #clone()} in order to get a copy.
 */
@SerializableAs("BoundingBox")
public class BoundingBox implements Cloneable, ConfigurationSerializable {

    /**
     * Creates a new bounding box using the coordinates of the given vectors as
     * corners.
     *
     * @param corner1 the first corner
     * @param corner2 the second corner
     * @return the bounding box
     */
    @NotNull
    public static BoundingBox of(@NotNull Vector corner1, @NotNull Vector corner2) {
        Preconditions.checkArgument(corner1 != null, "Corner1 is null!");
        Preconditions.checkArgument(corner2 != null, "Corner2 is null!");
        return new BoundingBox(corner1.getX(), corner1.getY(), corner1.getZ(), corner2.getX(), corner2.getY(), corner2.getZ());
    }

    /**
     * Creates a new bounding box using the coordinates of the given locations
     * as corners.
     *
     * @param corner1 the first corner
     * @param corner2 the second corner
     * @return the bounding box
     */
    @NotNull
    public static BoundingBox of(@NotNull Location corner1, @NotNull Location corner2) {
        Preconditions.checkArgument(corner1 != null, "Corner1 is null!");
        Preconditions.checkArgument(corner2 != null, "Corner2 is null!");
        Preconditions.checkArgument(Objects.equals(corner1.getWorld(), corner2.getWorld()), "Locations from different worlds!");
        return new BoundingBox(corner1.getX(), corner1.getY(), corner1.getZ(), corner2.getX(), corner2.getY(), corner2.getZ());
    }

    /**
     * Creates a new bounding box using the coordinates of the given blocks as
     * corners.
     * <p>
     * The bounding box will be sized to fully contain both blocks.
     *
     * @param corner1 the first corner block
     * @param corner2 the second corner block
     * @return the bounding box
     */
    @NotNull
    public static BoundingBox of(@NotNull Block corner1, @NotNull Block corner2) {
        Preconditions.checkArgument(corner1 != null, "Corner1 is null!");
        Preconditions.checkArgument(corner2 != null, "Corner2 is null!");
        Preconditions.checkArgument(Objects.equals(corner1.getWorld(), corner2.getWorld()), "Blocks from different worlds!");

        int x1 = corner1.getX();
        int y1 = corner1.getY();
        int z1 = corner1.getZ();
        int x2 = corner2.getX();
        int y2 = corner2.getY();
        int z2 = corner2.getZ();

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2) + 1;
        int maxY = Math.max(y1, y2) + 1;
        int maxZ = Math.max(z1, z2) + 1;

        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Creates a new 1x1x1 sized bounding box containing the given block.
     *
     * @param block the block
     * @return the bounding box
     */
    @NotNull
    public static BoundingBox of(@NotNull Block block) {
        Preconditions.checkArgument(block != null, "Block is null!");
        return new BoundingBox(block.getX(), block.getY(), block.getZ(), block.getX() + 1, block.getY() + 1, block.getZ() + 1);
    }

    /**
     * Creates a new bounding box using the given center and extents.
     *
     * @param center the center
     * @param x 1/2 the size of the bounding box along the x axis
     * @param y 1/2 the size of the bounding box along the y axis
     * @param z 1/2 the size of the bounding box along the z axis
     * @return the bounding box
     */
    @NotNull
    public static BoundingBox of(@NotNull Vector center, double x, double y, double z) {
        Preconditions.checkArgument(center != null, "Center is null!");
        return new BoundingBox(center.getX() - x, center.getY() - y, center.getZ() - z, center.getX() + x, center.getY() + y, center.getZ() + z);
    }

    /**
     * Creates a new bounding box using the given center and extents.
     *
     * @param center the center
     * @param x 1/2 the size of the bounding box along the x axis
     * @param y 1/2 the size of the bounding box along the y axis
     * @param z 1/2 the size of the bounding box along the z axis
     * @return the bounding box
     */
    @NotNull
    public static BoundingBox of(@NotNull Location center, double x, double y, double z) {
        Preconditions.checkArgument(center != null, "Center is null!");
        return new BoundingBox(center.getX() - x, center.getY() - y, center.getZ() - z, center.getX() + x, center.getY() + y, center.getZ() + z);
    }

    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;

    /**
     * Creates a new (degenerate) bounding box with all corner coordinates at
     * <code>0</code>.
     */
    public BoundingBox() {
        this.resize(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    /**
     * Creates a new bounding box from the given corner coordinates.
     *
     * @param x1 the first corner's x value
     * @param y1 the first corner's y value
     * @param z1 the first corner's z value
     * @param x2 the second corner's x value
     * @param y2 the second corner's y value
     * @param z2 the second corner's z value
     */
    public BoundingBox(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.resize(x1, y1, z1, x2, y2, z2);
    }

    /**
     * Resizes this bounding box.
     *
     * @param x1 the first corner's x value
     * @param y1 the first corner's y value
     * @param z1 the first corner's z value
     * @param x2 the second corner's x value
     * @param y2 the second corner's y value
     * @param z2 the second corner's z value
     * @return this bounding box (resized)
     */
    @NotNull
    public BoundingBox resize(double x1, double y1, double z1, double x2, double y2, double z2) {
        NumberConversions.checkFinite(x1, "x1 not finite");
        NumberConversions.checkFinite(y1, "y1 not finite");
        NumberConversions.checkFinite(z1, "z1 not finite");
        NumberConversions.checkFinite(x2, "x2 not finite");
        NumberConversions.checkFinite(y2, "y2 not finite");
        NumberConversions.checkFinite(z2, "z2 not finite");

        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
        return this;
    }

    /**
     * Gets the minimum x value.
     *
     * @return the minimum x value
     */
    public double getMinX() {
        return minX;
    }

    /**
     * Gets the minimum y value.
     *
     * @return the minimum y value
     */
    public double getMinY() {
        return minY;
    }

    /**
     * Gets the minimum z value.
     *
     * @return the minimum z value
     */
    public double getMinZ() {
        return minZ;
    }

    /**
     * Gets the minimum corner as vector.
     *
     * @return the minimum corner as vector
     */
    @NotNull
    public Vector getMin() {
        return new Vector(minX, minY, minZ);
    }

    /**
     * Gets the maximum x value.
     *
     * @return the maximum x value
     */
    public double getMaxX() {
        return maxX;
    }

    /**
     * Gets the maximum y value.
     *
     * @return the maximum y value
     */
    public double getMaxY() {
        return maxY;
    }

    /**
     * Gets the maximum z value.
     *
     * @return the maximum z value
     */
    public double getMaxZ() {
        return maxZ;
    }

    /**
     * Gets the maximum corner as vector.
     *
     * @return the maximum corner vector
     */
    @NotNull
    public Vector getMax() {
        return new Vector(maxX, maxY, maxZ);
    }

    /**
     * Gets the width of the bounding box in the x direction.
     *
     * @return the width in the x direction
     */
    public double getWidthX() {
        return (this.maxX - this.minX);
    }

    /**
     * Gets the width of the bounding box in the z direction.
     *
     * @return the width in the z direction
     */
    public double getWidthZ() {
        return (this.maxZ - this.minZ);
    }

    /**
     * Gets the height of the bounding box.
     *
     * @return the height
     */
    public double getHeight() {
        return (this.maxY - this.minY);
    }

    /**
     * Gets the volume of the bounding box.
     *
     * @return the volume
     */
    public double getVolume() {
        return (this.getHeight() * this.getWidthX() * this.getWidthZ());
    }

    /**
     * Gets the x coordinate of the center of the bounding box.
     *
     * @return the center's x coordinate
     */
    public double getCenterX() {
        return (this.minX + this.getWidthX() * 0.5D);
    }

    /**
     * Gets the y coordinate of the center of the bounding box.
     *
     * @return the center's y coordinate
     */
    public double getCenterY() {
        return (this.minY + this.getHeight() * 0.5D);
    }

    /**
     * Gets the z coordinate of the center of the bounding box.
     *
     * @return the center's z coordinate
     */
    public double getCenterZ() {
        return (this.minZ + this.getWidthZ() * 0.5D);
    }

    /**
     * Gets the center of the bounding box.
     *
     * @return the center
     */
    @NotNull
    public Vector getCenter() {
        return new Vector(this.getCenterX(), this.getCenterY(), this.getCenterZ());
    }

    /**
     * Copies another bounding box.
     *
     * @param other the other bounding box
     * @return this bounding box
     */
    @NotNull
    public BoundingBox copy(@NotNull BoundingBox other) {
        Preconditions.checkArgument(other != null, "Other bounding box is null!");
        return this.resize(other.getMinX(), other.getMinY(), other.getMinZ(), other.getMaxX(), other.getMaxY(), other.getMaxZ());
    }

    /**
     * Expands this bounding box by the given values in the corresponding
     * directions.
     * <p>
     * Negative values will shrink the bounding box in the corresponding
     * direction. Shrinking will be limited to the point where the affected
     * opposite faces would meet if the they shrank at uniform speeds.
     *
     * @param negativeX the amount of expansion in the negative x direction
     * @param negativeY the amount of expansion in the negative y direction
     * @param negativeZ the amount of expansion in the negative z direction
     * @param positiveX the amount of expansion in the positive x direction
     * @param positiveY the amount of expansion in the positive y direction
     * @param positiveZ the amount of expansion in the positive z direction
     * @return this bounding box (now expanded)
     */
    @NotNull
    public BoundingBox expand(double negativeX, double negativeY, double negativeZ, double positiveX, double positiveY, double positiveZ) {
        if (negativeX == 0.0D && negativeY == 0.0D && negativeZ == 0.0D && positiveX == 0.0D && positiveY == 0.0D && positiveZ == 0.0D) {
            return this;
        }
        double newMinX = this.minX - negativeX;
        double newMinY = this.minY - negativeY;
        double newMinZ = this.minZ - negativeZ;
        double newMaxX = this.maxX + positiveX;
        double newMaxY = this.maxY + positiveY;
        double newMaxZ = this.maxZ + positiveZ;

        // limit shrinking:
        if (newMinX > newMaxX) {
            double centerX = this.getCenterX();
            if (newMaxX >= centerX) {
                newMinX = newMaxX;
            } else if (newMinX <= centerX) {
                newMaxX = newMinX;
            } else {
                newMinX = centerX;
                newMaxX = centerX;
            }
        }
        if (newMinY > newMaxY) {
            double centerY = this.getCenterY();
            if (newMaxY >= centerY) {
                newMinY = newMaxY;
            } else if (newMinY <= centerY) {
                newMaxY = newMinY;
            } else {
                newMinY = centerY;
                newMaxY = centerY;
            }
        }
        if (newMinZ > newMaxZ) {
            double centerZ = this.getCenterZ();
            if (newMaxZ >= centerZ) {
                newMinZ = newMaxZ;
            } else if (newMinZ <= centerZ) {
                newMaxZ = newMinZ;
            } else {
                newMinZ = centerZ;
                newMaxZ = centerZ;
            }
        }
        return this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    /**
     * Expands this bounding box uniformly by the given values in both positive
     * and negative directions.
     * <p>
     * Negative values will shrink the bounding box. Shrinking will be limited
     * to the bounding box's current size.
     *
     * @param x the amount of expansion in both positive and negative x
     * direction
     * @param y the amount of expansion in both positive and negative y
     * direction
     * @param z the amount of expansion in both positive and negative z
     * direction
     * @return this bounding box (now expanded)
     */
    @NotNull
    public BoundingBox expand(double x, double y, double z) {
        return this.expand(x, y, z, x, y, z);
    }

    /**
     * Expands this bounding box uniformly by the given values in both positive
     * and negative directions.
     * <p>
     * Negative values will shrink the bounding box. Shrinking will be limited
     * to the bounding box's current size.
     *
     * @param expansion the expansion values
     * @return this bounding box (now expanded)
     */
    @NotNull
    public BoundingBox expand(@NotNull Vector expansion) {
        Preconditions.checkArgument(expansion != null, "Expansion is null!");
        double x = expansion.getX();
        double y = expansion.getY();
        double z = expansion.getZ();
        return this.expand(x, y, z, x, y, z);
    }

    /**
     * Expands this bounding box uniformly by the given value in all directions.
     * <p>
     * A negative value will shrink the bounding box. Shrinking will be limited
     * to the bounding box's current size.
     *
     * @param expansion the amount of expansion
     * @return this bounding box (now expanded)
     */
    @NotNull
    public BoundingBox expand(double expansion) {
        return this.expand(expansion, expansion, expansion, expansion, expansion, expansion);
    }

    /**
     * Expands this bounding box in the specified direction.
     * <p>
     * The magnitude of the direction will scale the expansion. A negative
     * expansion value will shrink the bounding box in this direction. Shrinking
     * will be limited to the bounding box's current size.
     *
     * @param dirX the x direction component
     * @param dirY the y direction component
     * @param dirZ the z direction component
     * @param expansion the amount of expansion
     * @return this bounding box (now expanded)
     */
    @NotNull
    public BoundingBox expand(double dirX, double dirY, double dirZ, double expansion) {
        if (expansion == 0.0D) return this;
        if (dirX == 0.0D && dirY == 0.0D && dirZ == 0.0D) return this;

        double negativeX = (dirX < 0.0D ? (-dirX * expansion) : 0.0D);
        double negativeY = (dirY < 0.0D ? (-dirY * expansion) : 0.0D);
        double negativeZ = (dirZ < 0.0D ? (-dirZ * expansion) : 0.0D);
        double positiveX = (dirX > 0.0D ? (dirX * expansion) : 0.0D);
        double positiveY = (dirY > 0.0D ? (dirY * expansion) : 0.0D);
        double positiveZ = (dirZ > 0.0D ? (dirZ * expansion) : 0.0D);
        return this.expand(negativeX, negativeY, negativeZ, positiveX, positiveY, positiveZ);
    }

    /**
     * Expands this bounding box in the specified direction.
     * <p>
     * The magnitude of the direction will scale the expansion. A negative
     * expansion value will shrink the bounding box in this direction. Shrinking
     * will be limited to the bounding box's current size.
     *
     * @param direction the direction
     * @param expansion the amount of expansion
     * @return this bounding box (now expanded)
     */
    @NotNull
    public BoundingBox expand(@NotNull Vector direction, double expansion) {
        Preconditions.checkArgument(direction != null, "Direction is null!");
        return this.expand(direction.getX(), direction.getY(), direction.getZ(), expansion);
    }

    /**
     * Expands this bounding box in the direction specified by the given block
     * face.
     * <p>
     * A negative expansion value will shrink the bounding box in this
     * direction. Shrinking will be limited to the bounding box's current size.
     *
     * @param blockFace the block face
     * @param expansion the amount of expansion
     * @return this bounding box (now expanded)
     */
    @NotNull
    public BoundingBox expand(@NotNull BlockFace blockFace, double expansion) {
        Preconditions.checkArgument(blockFace != null, "Block face is null!");
        if (blockFace == BlockFace.SELF) return this;

        return this.expand(blockFace.getDirection(), expansion);
    }

    /**
     * Expands this bounding box in the specified direction.
     * <p>
     * Negative values will expand the bounding box in the negative direction,
     * positive values will expand it in the positive direction. The magnitudes
     * of the direction components determine the corresponding amounts of
     * expansion.
     *
     * @param dirX the x direction component
     * @param dirY the y direction component
     * @param dirZ the z direction component
     * @return this bounding box (now expanded)
     */
    @NotNull
    public BoundingBox expandDirectional(double dirX, double dirY, double dirZ) {
        return this.expand(dirX, dirY, dirZ, 1.0D);
    }

    /**
     * Expands this bounding box in the specified direction.
     * <p>
     * Negative values will expand the bounding box in the negative direction,
     * positive values will expand it in the positive direction. The magnitude
     * of the direction vector determines the amount of expansion.
     *
     * @param direction the direction and magnitude of the expansion
     * @return this bounding box (now expanded)
     */
    @NotNull
    public BoundingBox expandDirectional(@NotNull Vector direction) {
        Preconditions.checkArgument(direction != null, "Expansion is null!");
        return this.expand(direction.getX(), direction.getY(), direction.getZ(), 1.0D);
    }

    /**
     * Expands this bounding box to contain (or border) the specified position.
     *
     * @param posX the x position value
     * @param posY the y position value
     * @param posZ the z position value
     * @return this bounding box (now expanded)
     * @see #contains(double, double, double)
     */
    @NotNull
    public BoundingBox union(double posX, double posY, double posZ) {
        double newMinX = Math.min(this.minX, posX);
        double newMinY = Math.min(this.minY, posY);
        double newMinZ = Math.min(this.minZ, posZ);
        double newMaxX = Math.max(this.maxX, posX);
        double newMaxY = Math.max(this.maxY, posY);
        double newMaxZ = Math.max(this.maxZ, posZ);
        if (newMinX == this.minX && newMinY == this.minY && newMinZ == this.minZ && newMaxX == this.maxX && newMaxY == this.maxY && newMaxZ == this.maxZ) {
            return this;
        }
        return this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    /**
     * Expands this bounding box to contain (or border) the specified position.
     *
     * @param position the position
     * @return this bounding box (now expanded)
     * @see #contains(double, double, double)
     */
    @NotNull
    public BoundingBox union(@NotNull Vector position) {
        Preconditions.checkArgument(position != null, "Position is null!");
        return this.union(position.getX(), position.getY(), position.getZ());
    }

    /**
     * Expands this bounding box to contain (or border) the specified position.
     *
     * @param position the position
     * @return this bounding box (now expanded)
     * @see #contains(double, double, double)
     */
    @NotNull
    public BoundingBox union(@NotNull Location position) {
        Preconditions.checkArgument(position != null, "Position is null!");
        return this.union(position.getX(), position.getY(), position.getZ());
    }

    /**
     * Expands this bounding box to contain both this and the given bounding
     * box.
     *
     * @param other the other bounding box
     * @return this bounding box (now expanded)
     */
    @NotNull
    public BoundingBox union(@NotNull BoundingBox other) {
        Preconditions.checkArgument(other != null, "Other bounding box is null!");
        if (this.contains(other)) return this;
        double newMinX = Math.min(this.minX, other.minX);
        double newMinY = Math.min(this.minY, other.minY);
        double newMinZ = Math.min(this.minZ, other.minZ);
        double newMaxX = Math.max(this.maxX, other.maxX);
        double newMaxY = Math.max(this.maxY, other.maxY);
        double newMaxZ = Math.max(this.maxZ, other.maxZ);
        return this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    /**
     * Resizes this bounding box to represent the intersection of this and the
     * given bounding box.
     *
     * @param other the other bounding box
     * @return this bounding box (now representing the intersection)
     * @throws IllegalArgumentException if the bounding boxes don't overlap
     */
    @NotNull
    public BoundingBox intersection(@NotNull BoundingBox other) {
        Preconditions.checkArgument(other != null, "Other bounding box is null!");
        Preconditions.checkArgument(this.overlaps(other), "The bounding boxes do not overlap!");
        double newMinX = Math.max(this.minX, other.minX);
        double newMinY = Math.max(this.minY, other.minY);
        double newMinZ = Math.max(this.minZ, other.minZ);
        double newMaxX = Math.min(this.maxX, other.maxX);
        double newMaxY = Math.min(this.maxY, other.maxY);
        double newMaxZ = Math.min(this.maxZ, other.maxZ);
        return this.resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    /**
     * Shifts this bounding box by the given amounts.
     *
     * @param shiftX the shift in x direction
     * @param shiftY the shift in y direction
     * @param shiftZ the shift in z direction
     * @return this bounding box (now shifted)
     */
    @NotNull
    public BoundingBox shift(double shiftX, double shiftY, double shiftZ) {
        if (shiftX == 0.0D && shiftY == 0.0D && shiftZ == 0.0D) return this;
        return this.resize(this.minX + shiftX, this.minY + shiftY, this.minZ + shiftZ,
                this.maxX + shiftX, this.maxY + shiftY, this.maxZ + shiftZ);
    }

    /**
     * Shifts this bounding box by the given amounts.
     *
     * @param shift the shift
     * @return this bounding box (now shifted)
     */
    @NotNull
    public BoundingBox shift(@NotNull Vector shift) {
        Preconditions.checkArgument(shift != null, "Shift is null!");
        return this.shift(shift.getX(), shift.getY(), shift.getZ());
    }

    /**
     * Shifts this bounding box by the given amounts.
     *
     * @param shift the shift
     * @return this bounding box (now shifted)
     */
    @NotNull
    public BoundingBox shift(@NotNull Location shift) {
        Preconditions.checkArgument(shift != null, "Shift is null!");
        return this.shift(shift.getX(), shift.getY(), shift.getZ());
    }

    private boolean overlaps(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.minX < maxX && this.maxX > minX
                && this.minY < maxY && this.maxY > minY
                && this.minZ < maxZ && this.maxZ > minZ;
    }

    /**
     * Checks if this bounding box overlaps with the given bounding box.
     * <p>
     * Bounding boxes that are only intersecting at the borders are not
     * considered overlapping.
     *
     * @param other the other bounding box
     * @return <code>true</code> if overlapping
     */
    public boolean overlaps(@NotNull BoundingBox other) {
        Preconditions.checkArgument(other != null, "Other bounding box is null!");
        return this.overlaps(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }

    /**
     * Checks if this bounding box overlaps with the bounding box that is
     * defined by the given corners.
     * <p>
     * Bounding boxes that are only intersecting at the borders are not
     * considered overlapping.
     *
     * @param min the first corner
     * @param max the second corner
     * @return <code>true</code> if overlapping
     */
    public boolean overlaps(@NotNull Vector min, @NotNull Vector max) {
        Preconditions.checkArgument(min != null, "Min is null!");
        Preconditions.checkArgument(max != null, "Max is null!");
        double x1 = min.getX();
        double y1 = min.getY();
        double z1 = min.getZ();
        double x2 = max.getX();
        double y2 = max.getY();
        double z2 = max.getZ();
        return this.overlaps(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2),
                Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
    }

    /**
     * Checks if this bounding box contains the specified position.
     * <p>
     * Positions exactly on the minimum borders of the bounding box are
     * considered to be inside the bounding box, while positions exactly on the
     * maximum borders are considered to be outside. This allows bounding boxes
     * to reside directly next to each other with positions always only residing
     * in exactly one of them.
     *
     * @param x the position's x coordinates
     * @param y the position's y coordinates
     * @param z the position's z coordinates
     * @return <code>true</code> if the bounding box contains the position
     */
    public boolean contains(double x, double y, double z) {
        return x >= this.minX && x < this.maxX
                && y >= this.minY && y < this.maxY
                && z >= this.minZ && z < this.maxZ;
    }

    /**
     * Checks if this bounding box contains the specified position.
     * <p>
     * Positions exactly on the minimum borders of the bounding box are
     * considered to be inside the bounding box, while positions exactly on the
     * maximum borders are considered to be outside. This allows bounding boxes
     * to reside directly next to each other with positions always only residing
     * in exactly one of them.
     *
     * @param position the position
     * @return <code>true</code> if the bounding box contains the position
     */
    public boolean contains(@NotNull Vector position) {
        Preconditions.checkArgument(position != null, "Position is null!");
        return this.contains(position.getX(), position.getY(), position.getZ());
    }

    private boolean contains(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.minX <= minX && this.maxX >= maxX
                && this.minY <= minY && this.maxY >= maxY
                && this.minZ <= minZ && this.maxZ >= maxZ;
    }

    /**
     * Checks if this bounding box fully contains the given bounding box.
     *
     * @param other the other bounding box
     * @return <code>true</code> if the bounding box contains the given bounding
     * box
     */
    public boolean contains(@NotNull BoundingBox other) {
        Preconditions.checkArgument(other != null, "Other bounding box is null!");
        return this.contains(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }

    /**
     * Checks if this bounding box fully contains the bounding box that is
     * defined by the given corners.
     *
     * @param min the first corner
     * @param max the second corner
     * @return <code>true</code> if the bounding box contains the specified
     *     bounding box
     */
    public boolean contains(@NotNull Vector min, @NotNull Vector max) {
        Preconditions.checkArgument(min != null, "Min is null!");
        Preconditions.checkArgument(max != null, "Max is null!");
        double x1 = min.getX();
        double y1 = min.getY();
        double z1 = min.getZ();
        double x2 = max.getX();
        double y2 = max.getY();
        double z2 = max.getZ();
        return this.contains(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2),
                Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
    }

    /**
     * Calculates the intersection of this bounding box with the specified line
     * segment.
     * <p>
     * Intersections at edges and corners yield one of the affected block faces
     * as hit result, but it is not defined which of them.
     *
     * @param start the start position
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @return the ray trace hit result, or <code>null</code> if there is no hit
     */
    @Nullable
    public RayTraceResult rayTrace(@NotNull Vector start, @NotNull Vector direction, double maxDistance) {
        Preconditions.checkArgument(start != null, "Start is null!");
        start.checkFinite();
        Preconditions.checkArgument(direction != null, "Direction is null!");
        direction.checkFinite();
        Preconditions.checkArgument(direction.lengthSquared() > 0, "Direction's magnitude is 0!");
        if (maxDistance < 0.0D) return null;

        // ray start:
        double startX = start.getX();
        double startY = start.getY();
        double startZ = start.getZ();

        // ray direction:
        Vector dir = direction.clone().normalizeZeros().normalize();
        double dirX = dir.getX();
        double dirY = dir.getY();
        double dirZ = dir.getZ();

        // saving a few divisions below:
        // Note: If one of the direction vector components is 0.0, these
        // divisions result in infinity. But this is not a problem.
        double divX = 1.0D / dirX;
        double divY = 1.0D / dirY;
        double divZ = 1.0D / dirZ;

        double tMin;
        double tMax;
        BlockFace hitBlockFaceMin;
        BlockFace hitBlockFaceMax;

        // intersections with x planes:
        if (dirX >= 0.0D) {
            tMin = (this.minX - startX) * divX;
            tMax = (this.maxX - startX) * divX;
            hitBlockFaceMin = BlockFace.WEST;
            hitBlockFaceMax = BlockFace.EAST;
        } else {
            tMin = (this.maxX - startX) * divX;
            tMax = (this.minX - startX) * divX;
            hitBlockFaceMin = BlockFace.EAST;
            hitBlockFaceMax = BlockFace.WEST;
        }

        // intersections with y planes:
        double tyMin;
        double tyMax;
        BlockFace hitBlockFaceYMin;
        BlockFace hitBlockFaceYMax;
        if (dirY >= 0.0D) {
            tyMin = (this.minY - startY) * divY;
            tyMax = (this.maxY - startY) * divY;
            hitBlockFaceYMin = BlockFace.DOWN;
            hitBlockFaceYMax = BlockFace.UP;
        } else {
            tyMin = (this.maxY - startY) * divY;
            tyMax = (this.minY - startY) * divY;
            hitBlockFaceYMin = BlockFace.UP;
            hitBlockFaceYMax = BlockFace.DOWN;
        }
        if ((tMin > tyMax) || (tMax < tyMin)) {
            return null;
        }
        if (tyMin > tMin) {
            tMin = tyMin;
            hitBlockFaceMin = hitBlockFaceYMin;
        }
        if (tyMax < tMax) {
            tMax = tyMax;
            hitBlockFaceMax = hitBlockFaceYMax;
        }

        // intersections with z planes:
        double tzMin;
        double tzMax;
        BlockFace hitBlockFaceZMin;
        BlockFace hitBlockFaceZMax;
        if (dirZ >= 0.0D) {
            tzMin = (this.minZ - startZ) * divZ;
            tzMax = (this.maxZ - startZ) * divZ;
            hitBlockFaceZMin = BlockFace.NORTH;
            hitBlockFaceZMax = BlockFace.SOUTH;
        } else {
            tzMin = (this.maxZ - startZ) * divZ;
            tzMax = (this.minZ - startZ) * divZ;
            hitBlockFaceZMin = BlockFace.SOUTH;
            hitBlockFaceZMax = BlockFace.NORTH;
        }
        if ((tMin > tzMax) || (tMax < tzMin)) {
            return null;
        }
        if (tzMin > tMin) {
            tMin = tzMin;
            hitBlockFaceMin = hitBlockFaceZMin;
        }
        if (tzMax < tMax) {
            tMax = tzMax;
            hitBlockFaceMax = hitBlockFaceZMax;
        }

        // intersections are behind the start:
        if (tMax < 0.0D) return null;
        // intersections are to far away:
        if (tMin > maxDistance) {
            return null;
        }

        // find the closest intersection:
        double t;
        BlockFace hitBlockFace;
        if (tMin < 0.0D) {
            t = tMax;
            hitBlockFace = hitBlockFaceMax;
        } else {
            t = tMin;
            hitBlockFace = hitBlockFaceMin;
        }
        // reusing the newly created direction vector for the hit position:
        Vector hitPosition = dir.multiply(t).add(start);
        return new RayTraceResult(hitPosition, hitBlockFace);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(maxX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(maxZ);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minX);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minY);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(minZ);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BoundingBox)) return false;
        BoundingBox other = (BoundingBox) obj;
        if (Double.doubleToLongBits(maxX) != Double.doubleToLongBits(other.maxX)) return false;
        if (Double.doubleToLongBits(maxY) != Double.doubleToLongBits(other.maxY)) return false;
        if (Double.doubleToLongBits(maxZ) != Double.doubleToLongBits(other.maxZ)) return false;
        if (Double.doubleToLongBits(minX) != Double.doubleToLongBits(other.minX)) return false;
        if (Double.doubleToLongBits(minY) != Double.doubleToLongBits(other.minY)) return false;
        if (Double.doubleToLongBits(minZ) != Double.doubleToLongBits(other.minZ)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BoundingBox [minX=");
        builder.append(minX);
        builder.append(", minY=");
        builder.append(minY);
        builder.append(", minZ=");
        builder.append(minZ);
        builder.append(", maxX=");
        builder.append(maxX);
        builder.append(", maxY=");
        builder.append(maxY);
        builder.append(", maxZ=");
        builder.append(maxZ);
        builder.append("]");
        return builder.toString();
    }

    /**
     * Creates a copy of this bounding box.
     *
     * @return the cloned bounding box
     */
    @NotNull
    @Override
    public BoundingBox clone() {
        try {
            return (BoundingBox) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("minX", minX);
        result.put("minY", minY);
        result.put("minZ", minZ);
        result.put("maxX", maxX);
        result.put("maxY", maxY);
        result.put("maxZ", maxZ);
        return result;
    }

    @NotNull
    public static BoundingBox deserialize(@NotNull Map<String, Object> args) {
        double minX = 0.0D;
        double minY = 0.0D;
        double minZ = 0.0D;
        double maxX = 0.0D;
        double maxY = 0.0D;
        double maxZ = 0.0D;

        if (args.containsKey("minX")) {
            minX = ((Number) args.get("minX")).doubleValue();
        }
        if (args.containsKey("minY")) {
            minY = ((Number) args.get("minY")).doubleValue();
        }
        if (args.containsKey("minZ")) {
            minZ = ((Number) args.get("minZ")).doubleValue();
        }
        if (args.containsKey("maxX")) {
            maxX = ((Number) args.get("maxX")).doubleValue();
        }
        if (args.containsKey("maxY")) {
            maxY = ((Number) args.get("maxY")).doubleValue();
        }
        if (args.containsKey("maxZ")) {
            maxZ = ((Number) args.get("maxZ")).doubleValue();
        }

        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
}
