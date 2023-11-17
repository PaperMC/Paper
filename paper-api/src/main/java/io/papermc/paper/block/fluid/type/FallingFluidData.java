package io.papermc.paper.block.fluid.type;

import io.papermc.paper.block.fluid.FluidData;

/**
 * A specific subtype of {@link FluidData} that is returned by the API for fluid data of potentially falling fluids.
 */
public interface FallingFluidData extends FluidData {

    /**
     * Get if this liquid is falling.
     *
     * @return true if falling
     */
    boolean isFalling();
}
