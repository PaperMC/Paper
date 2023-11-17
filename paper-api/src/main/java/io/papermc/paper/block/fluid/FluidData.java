package io.papermc.paper.block.fluid;

import org.bukkit.Fluid;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

/**
 * A representation of a fluid in a specific state of data.
 * This type is not linked to a specific location and hence mostly resembles a {@link org.bukkit.block.data.BlockData}.
 */
@NullMarked
public interface FluidData extends Cloneable {

    /**
     * Gets the fluid type of this fluid data.
     *
     * @return the fluid type
     */
    Fluid getFluidType();

    /**
     * Returns a copy of this FluidData.
     *
     * @return a copy of the fluid data
     */
    FluidData clone();

    /**
     * Computes the direction of the flow of the liquid at the given location as a vector.
     * <p>
     * This method requires the passed location's chunk to be loaded.
     * If said chunk is not loaded when this method is called, the chunk will first be loaded prior to the computation
     * which leads to a potentially slow sync chunk load.
     *
     * @param location - the location to check the liquid flow
     * @return the flow direction vector at the given location
     */
    Vector computeFlowDirection(Location location);

    /**
     * Returns the level of liquid this fluid data holds.
     *
     * @return the amount as an integer, between 0 and 8
     */
    @Range(from = 0, to = 8)
    int getLevel();

    /**
     * Computes the height of the fluid in the world.
     * <p>
     * This method requires the passed location's chunk to be loaded.
     * If said chunk is not loaded when this method is called, the chunk will first be loaded prior to the computation
     * which leads to a potentially slow sync chunk load.
     *
     * @param location the location at which to check the high of this fluid data.
     * @return the height as a float value
     */
    @Range(from = 0, to = 1)
    float computeHeight(Location location);

    /**
     * Returns whether this fluid is a source block
     *
     * @return true if the fluid is a source block, false otherwise
     */
    boolean isSource();
}
