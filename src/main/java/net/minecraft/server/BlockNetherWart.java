package net.minecraft.server;

import java.util.Random;

public class BlockNetherWart extends BlockPlant {

    protected BlockNetherWart() {
        this.a(true);
        float f = 0.5F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        this.a((CreativeModeTab) null);
    }

    protected boolean a(Block block) {
        return block == Blocks.SOUL_SAND;
    }

    public boolean j(World world, int i, int j, int k) {
        return this.a(world.getType(i, j - 1, k));
    }

    public void a(World world, int i, int j, int k, Random random) {
        int l = world.getData(i, j, k);

        if (l < 3 && random.nextInt(10) == 0) {
            ++l;
            org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, i, j, k, this, l); // CraftBukkit
        }

        super.a(world, i, j, k, random);
    }

    public int b() {
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
                this.a(world, i, j, k, new ItemStack(Items.NETHER_STALK));
            }
        }
    }

    public Item getDropType(int i, Random random, int j) {
        return null;
    }

    public int a(Random random) {
        return 0;
    }
}
