
package io.papermc.paper.block.fluid.type;

import io.papermc.paper.block.fluid.PaperFluidData;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;

public class PaperFallingFluidData extends PaperFluidData implements FallingFluidData {

    public PaperFallingFluidData(final FluidState state) {
        super(state);
    }

    @Override
    public boolean isFalling() {
        return this.getState().getValue(FlowingFluid.FALLING);
    }
}
