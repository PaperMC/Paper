package net.minecraft.server;

import java.util.Random;

public class BlockCrops extends BlockPlant implements IBlockFragilePlantElement {

    protected BlockCrops() {
        this.a(true);
        float f = 0.5F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        this.a((CreativeModeTab) null);
        this.c(0.0F);
        this.a(h);
        this.H();
    }

    protected boolean a(Block block) {
        return block == Blocks.SOIL;
    }

    public void a(World world, int i, int j, int k, Random random) {
        super.a(world, i, j, k, random);
        if (world.getLightLevel(i, j + 1, k) >= 9) {
            int l = world.getData(i, j, k);

            if (l < 7) {
                float f = this.n(world, i, j, k);

                if (random.nextInt((int) (25.0F / f) + 1) == 0) {
                    ++l;
                    org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, i, j, k, this, l); // CraftBukkit
                }
            }
        }
    }

    public void m(World world, int i, int j, int k) {
        int l = world.getData(i, j, k) + MathHelper.nextInt(world.random, 2, 5);

        if (l > 7) {
            l = 7;
        }

        world.setData(i, j, k, l, 2);
    }

    private float n(World world, int i, int j, int k) {
        float f = 1.0F;
        Block block = world.getType(i, j, k - 1);
        Block block1 = world.getType(i, j, k + 1);
        Block block2 = world.getType(i - 1, j, k);
        Block block3 = world.getType(i + 1, j, k);
        Block block4 = world.getType(i - 1, j, k - 1);
        Block block5 = world.getType(i + 1, j, k - 1);
        Block block6 = world.getType(i + 1, j, k + 1);
        Block block7 = world.getType(i - 1, j, k + 1);
        boolean flag = block2 == this || block3 == this;
        boolean flag1 = block == this || block1 == this;
        boolean flag2 = block4 == this || block5 == this || block6 == this || block7 == this;

        for (int l = i - 1; l <= i + 1; ++l) {
            for (int i1 = k - 1; i1 <= k + 1; ++i1) {
                float f1 = 0.0F;

                if (world.getType(l, j - 1, i1) == Blocks.SOIL) {
                    f1 = 1.0F;
                    if (world.getData(l, j - 1, i1) > 0) {
                        f1 = 3.0F;
                    }
                }

                if (l != i || i1 != k) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (flag2 || flag && flag1) {
            f /= 2.0F;
        }

        return f;
    }

    public int b() {
        return 6;
    }

    protected Item i() {
        return Items.SEEDS;
    }

    protected Item P() {
        return Items.WHEAT;
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        super.dropNaturally(world, i, j, k, l, f, 0);
        if (!world.isStatic) {
            if (l >= 7) {
                int j1 = 3 + i1;

                for (int k1 = 0; k1 < j1; ++k1) {
                    if (world.random.nextInt(15) <= l) {
                        this.a(world, i, j, k, new ItemStack(this.i(), 1, 0));
                    }
                }
            }
        }
    }

    public Item getDropType(int i, Random random, int j) {
        return i == 7 ? this.P() : this.i();
    }

    public int a(Random random) {
        return 1;
    }

    public boolean a(World world, int i, int j, int k, boolean flag) {
        return world.getData(i, j, k) != 7;
    }

    public boolean a(World world, Random random, int i, int j, int k) {
        return true;
    }

    public void b(World world, Random random, int i, int j, int k) {
        this.m(world, i, j, k);
    }
}
