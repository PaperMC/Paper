package net.minecraft.server;

import java.util.Random;

public class BlockNetherWart extends BlockFlower {

    protected BlockNetherWart(int i) {
        super(i);
        this.b(true);
        float f = 0.5F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        this.a((CreativeModeTab) null);
    }

    protected boolean g_(int i) {
        return i == Block.SOUL_SAND.id;
    }

    public boolean f(World world, int i, int j, int k) {
        return this.g_(world.getTypeId(i, j - 1, k));
    }

    public void a(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k);

        if (l < 3 && random.nextInt(10) == 0) {
            org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, i, j, k, this.id, ++l); // CraftBukkit
        }

        super.a(world, i, j, k, random);
    }

    public int d() {
        return 6;
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        if (!world.isStatic) {
            int j1 = 1;

            if (l >= 3) {
                j1 = 2 + world.random.nextInt(3);
                if (i1 > 0) {
                    j1 += world.random.nextInt(i1 + 1);
                }
            }

            for (int k1 = 0; k1 < j1; ++k1) {
                this.b(world, i, j, k, new ItemStack(Item.NETHER_STALK));
            }
        }
    }

    public int getDropType(int i, Random random, int j) {
        return 0;
    }

    public int a(Random random) {
        return 0;
    }
}
