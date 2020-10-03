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

    @Override
    public IBlockData getType(BlockPosition blockposition) {
        return Blocks.AIR.getBlockData();
    }

    @Override
    public Fluid getFluid(BlockPosition blockposition) {
        return FluidTypes.EMPTY.h();
    }
}
