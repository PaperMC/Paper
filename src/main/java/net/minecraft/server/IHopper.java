package net.minecraft.server;

import javax.annotation.Nullable;

public interface IHopper extends IInventory {

    VoxelShape a = Block.a(2.0D, 11.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    VoxelShape b = Block.a(0.0D, 16.0D, 0.0D, 16.0D, 32.0D, 16.0D);
    VoxelShape c = VoxelShapes.a(IHopper.a, IHopper.b);

    default VoxelShape aa_() {
        return IHopper.c;
    }

    //@Nullable // Paper - it's annoying
    World getWorld();
    default BlockPosition getBlockPosition() { return new BlockPosition(getX(), getY(), getZ()); } // Paper

    double x(); default double getX() { return this.x(); } // Paper - OBFHELPER

    double z(); default double getY() { return this.z(); } // Paper - OBFHELPER

    double A(); default double getZ() { return this.A(); } // Paper - OBFHELPER
}
