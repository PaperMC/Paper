package net.minecraft.server;

import java.util.Random;

public class BlockDeadBush extends BlockFlower {

    protected BlockDeadBush(int i, int j) {
        super(i, j, Material.REPLACEABLE_PLANT);
        float f = 0.4F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
    }

    protected boolean d(int i) {
        return i == Block.SAND.id;
    }

    public int a(int i, int j) {
        return this.textureId;
    }

    public int getDropType(int i, Random random, int j) {
        return -1;
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        if (!world.isStatic && entityhuman.T() != null && entityhuman.T().id == Item.SHEARS.id) {
            entityhuman.a(StatisticList.C[this.id], 1);
            this.a(world, i, j, k, new ItemStack(Block.DEAD_BUSH, 1, l));
        } else {
            super.a(world, entityhuman, i, j, k, l);
        }
    }
}
