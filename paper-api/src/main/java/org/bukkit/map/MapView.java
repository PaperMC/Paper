package org.bukkit.map;

import java.util.List;
import org.bukkit.World;
import org.bukkit.inventory.meta.MapMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a map item.
 */
public interface MapView {

    /**
     * An enum representing all possible scales a map can be set to.
     */
    public static enum Scale {
        CLOSEST(0),
        CLOSE(1),
        NORMAL(2),
        FAR(3),
        FARTHEST(4);

        private byte value;

        private Scale(int value) {
            this.value = (byte) value;
        }

        /**
         * Get the scale given the raw value.
         *
         * @param value The raw scale
         * @return The enum scale, or null for an invalid input
         * @deprecated Magic value
         */
        @Deprecated
        @Nullable
        public static Scale valueOf(byte value) {
            switch (value) {
            case 0: return CLOSEST;
            case 1: return CLOSE;
            case 2: return NORMAL;
            case 3: return FAR;
            case 4: return FARTHEST;
            default: return null;
            }
        }

        /**
         * Get the raw value of this scale level.
         *
         * @return The scale value
         * @deprecated Magic value
         */
        @Deprecated
        public byte getValue() {
            return value;
        }
    }

    /**
     * Get the ID of this map item for use with {@link MapMeta}.
     *
     * @return The ID of the map.
     */
    public int getId();

    /**
     * Check whether this map is virtual. A map is virtual if its lowermost
     * MapRenderer is plugin-provided.
     *
     * @return Whether the map is virtual.
     */
    public boolean isVirtual();

    /**
     * Get the scale of this map.
     *
     * @return The scale of the map.
     */
    @NotNull
    public Scale getScale();

    /**
     * Set the scale of this map.
     *
     * @param scale The scale to set.
     */
    public void setScale(@NotNull Scale scale);

    /**
     * Get the center X position of this map.
     *
     * @return The center X position.
     */
    public int getCenterX();

    /**
     * Get the center Z position of this map.
     *
     * @return The center Z position.
     */
    public int getCenterZ();

    /**
     * Set the center X position of this map.
     *
     * @param x The center X position.
     */
    public void setCenterX(int x);

    /**
     * Set the center Z position of this map.
     *
     * @param z The center Z position.
     */
    public void setCenterZ(int z);

    /**
     * Get the world that this map is associated with. Primarily used by the
     * internal renderer, but may be used by external renderers. May return
     * null if the world the map is associated with is not loaded.
     *
     * @return The World this map is associated with.
     */
    @Nullable
    public World getWorld();

    /**
     * Set the world that this map is associated with. The world is used by
     * the internal renderer, and may also be used by external renderers.
     *
     * @param world The World to associate this map with.
     */
    public void setWorld(@NotNull World world);

    /**
     * Get a list of MapRenderers currently in effect.
     *
     * @return A {@code List<MapRenderer>} containing each map renderer.
     */
    @NotNull
    public List<MapRenderer> getRenderers();

    /**
     * Add a renderer to this map.
     *
     * @param renderer The MapRenderer to add.
     */
    public void addRenderer(@NotNull MapRenderer renderer);

    /**
     * Remove a renderer from this map.
     *
     * @param renderer The MapRenderer to remove.
     * @return True if the renderer was successfully removed.
     */
    public boolean removeRenderer(@Nullable MapRenderer renderer);

    /**
     * Gets whether a position cursor should be shown when the map is near its
     * center.
     *
     * @return tracking status
     */
    boolean isTrackingPosition();

    /**
     * Sets whether a position cursor should be shown when the map is near its
     * center.
     *
     * @param trackingPosition tracking status
     */
    void setTrackingPosition(boolean trackingPosition);

    /**
     * Whether the map will show a smaller position cursor (true), or no
     * position cursor (false) when cursor is outside of map's range.
     *
     * @return unlimited tracking state
     */
    boolean isUnlimitedTracking();

    /**
     * Whether the map will show a smaller position cursor (true), or no
     * position cursor (false) when cursor is outside of map's range.
     *
     * @param unlimited tracking state
     */
    void setUnlimitedTracking(boolean unlimited);

    /**
     * Gets whether the map is locked or not.
     *
     * A locked map may not be explored further.
     *
     * @return lock status
     */
    boolean isLocked();

    /**
     * Gets whether the map is locked or not.
     *
     * A locked map may not be explored further.
     *
     * @param locked status
     */
    void setLocked(boolean locked);
}
