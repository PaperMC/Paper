package org.bukkit.entity;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a Primed TNT.
 *
 * @since 1.0.0
 */
public interface TNTPrimed extends Explosive {

    /**
     * Set the number of ticks until the TNT blows up after being primed.
     *
     * @param fuseTicks The fuse ticks
     */
    public void setFuseTicks(int fuseTicks);

    /**
     * Retrieve the number of ticks until the explosion of this TNTPrimed
     * entity
     *
     * @return the number of ticks until this TNTPrimed explodes
     */
    public int getFuseTicks();

    /**
     * Gets the source of this primed TNT. The source is the entity
     * responsible for the creation of this primed TNT. (I.E. player ignites
     * TNT with flint and steel.) Take note that this can be null if there is
     * no suitable source. (created by the {@link
     * org.bukkit.World#spawn(Location, Class)} method, for example.)
     * <p>
     * The source will become null if the chunk this primed TNT is in is
     * unloaded then reloaded. The source entity may be invalid if for example
     * it has since died or been unloaded. Callers should check
     * {@link Entity#isValid()}.
     *
     * @return the source of this primed TNT
     * @since 1.5.1
     */
    @Nullable
    public Entity getSource();

    /**
     * Sets the source of this primed TNT.
     *
     * The source is the entity responsible for the creation of this primed TNT.
     * <p>
     * Must be instance of {@link org.bukkit.entity.LivingEntity} otherwise will
     * be set to null. The parameter is typed {@link
     * org.bukkit.entity.Entity} to be consistent with {@link
     * org.bukkit.entity.TNTPrimed#getSource()} method.
     *
     * @param source the source of this primed TNT
     * @since 1.16.3
     */
    public void setSource(@Nullable Entity source);

    /**
     * Gets the source block location of the TNTPrimed
     *
     * @return the source block location the TNTPrimed was spawned from
     * @deprecated replaced by {@link Entity#getOrigin()}
     * @since 1.9.4
     */
    @Deprecated
    default org.bukkit.Location getSourceLoc() {
        return this.getOrigin();
    }

    // Paper start
    /**
     * Sets the visual block data of this
     * primed tnt.
     * <br>
     * The explosion of the tnt stays the
     * same and is not affected by this change.
     *
     * @param data the visual block data
     * @since 1.20.4
     */
    void setBlockData(@org.jetbrains.annotations.NotNull org.bukkit.block.data.BlockData data);

    /**
     * Gets the visual block data of this
     * primed tnt.
     *
     * @return the visual block data
     * @since 1.20.4
     */
    @org.jetbrains.annotations.NotNull
    org.bukkit.block.data.BlockData getBlockData();
    // Paper end
}
