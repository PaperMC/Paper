package net.minecraft.server;

import java.util.Random;

public class BlockLongGrass extends BlockFlower {

    protected BlockLongGrass(int i, int j) {
        super(i, j, Material.REPLACEABLE_PLANT);
        float f = 0.4F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);
    }

    public int a(int i, int j) {
        return j == 1 ? this.textureId : (j == 2 ? this.textureId + 16 + 1 : (j == 0 ? this.textureId + 16 : this.textureId));
    }

    public int getDropType(int i, Random random, int j) {
        return random.nextInt(8) == 0 ? Item.SEEDS.id : -1;
    }

    public int getDropCount(int i, Random random) {
        return 1 + random.nextInt(i * 2 + 1);
    }

    public void a(World world, EntityHuman entityhuman, int i, int j, int k, int l) {
        if (!world.isStatic && entityhuman.T() != null && entityhuman.T().id == Item.SHEARS.id) {
            entityhuman.a(StatisticList.C[this.id], 1);
            this.a(world, i, j, k, new ItemStack(Block.LONG_GRASS, 1, l));
        } else {
            super.a(world, entityhuman, i, j, k, l);
        }
    }
}
