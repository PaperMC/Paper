package net.minecraft.server;

import java.util.ArrayList;

public class BlockFence extends Block {

    public BlockFence(int i, int j) {
        super(i, j, Material.WOOD);
        this.a(0, 0, 0, 1, (float) 1.5, 1); //Craftbukkit
    }

    //Removed by craftbukkit
//    public void a(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist) {
//        arraylist.add(AxisAlignedBB.b((double) i, (double) j, (double) k, (double) (i + 1), (double) j + 2.0D, (double) (k + 1)));
//    }

    public boolean a(World world, int i, int j, int k) {
        return world.getTypeId(i, j - 1, k) == this.id ? false : (!world.getMaterial(i, j - 1, k).isBuildable() ? false : super.a(world, i, j, k));
    }

    public boolean a() {
        return false;
    }
}
