package net.minecraft.server;

import javax.annotation.Nullable;

public enum BlockAccessAir implements IBlockAccess {

    INSTANCE;

    private BlockAccessAir() {}

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPosition blockposition) {
        return null;
    }

    // Paper start - If loaded util
    @Override
    public Fluid getFluidIfLoaded(BlockPosition blockposition) {
        return this.getFluid(blockposition);
    }

    @Override
    public IBlockData getTypeIfLoaded(BlockPosition blockposition) {
        return this.getType(blockposition);
    }
    // Paper end

    @Override
    public IBlockData getType(BlockPosition blockposition) {
        return Blocks.AIR.getBlockData();
    }

    @Override
    public Fluid getFluid(BlockPosition blockposition) {
        return FluidTypes.EMPTY.h();
    }
}
