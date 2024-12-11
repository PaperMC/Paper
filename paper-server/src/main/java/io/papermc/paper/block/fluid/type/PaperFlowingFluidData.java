package io.papermc.paper.block.fluid.type;

import net.minecraft.world.level.material.FluidState;

public class PaperFlowingFluidData extends PaperFallingFluidData implements FlowingFluidData {

    public PaperFlowingFluidData(final FluidState state) {
        super(state);
    }

}
