package net.minecraft.server;

import java.util.Random;

public class BlockCrops extends BlockFlower {

    protected BlockCrops(int i, int j) {
        super(i, j);
        this.textureId = j;
        this.b(true);
        float f = 0.5F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        this.a((CreativeModeTab) null);
        this.c(0.0F);
        this.a(g);
        this.D();
        this.r();
    }

    protected boolean d_(int i) {
        return i == Block.SOIL.id;
    }

    public void b(World world, int i, int j, int k, Random random) {
        super.b(world, i, j, k, random);
        if (world.getLightLevel(i, j + 1, k) >= 9) {
            int l = world.getData(i, j, k);

            if (l < 7) {
                float f = this.l(world, i, j, k);

                if (random.nextInt((int) (25.0F / f) + 1) == 0) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, i, j, k, this.id, ++l); // CraftBukkit
                }
            }
        }
    }

    public void c_(World world, int i, int j, int k) {
        world.setData(i, j, k, 7);
    }

    private float l(World world, int i, int j, int k) {
        float f = 1.0F;
        int l = world.getTypeId(i, j, k - 1);
        int i1 = world.getTypeId(i, j, k + 1);
        int j1 = world.getTypeId(i - 1, j, k);
        int k1 = world.getTypeId(i + 1, j, k);
        int l1 = world.getTypeId(i - 1, j, k - 1);
        int i2 = world.getTypeId(i + 1, j, k - 1);
        int j2 = world.getTypeId(i + 1, j, k + 1);
        int k2 = world.getTypeId(i - 1, j, k + 1);
        boolean flag = j1 == this.id || k1 == this.id;
        boolean flag1 = l == this.id || i1 == this.id;
        boolean flag2 = l1 == this.id || i2 == this.id || j2 == this.id || k2 == this.id;

        for (int l2 = i - 1; l2 <= i + 1; ++l2) {
            for (int i3 = k - 1; i3 <= k + 1; ++i3) {
                int j3 = world.getTypeId(l2, j - 1, i3);
                float f1 = 0.0F;

                if (j3 == Block.SOIL.id) {
                    f1 = 1.0F;
                    if (world.getData(l2, j - 1, i3) > 0) {
                        f1 = 3.0F;
                    }
                }

                if (l2 != i || i3 != k) {
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

    public int a(int i, int j) {
        if (j < 0) {
            j = 7;
        }

        return this.textureId + j;
    }

    public int d() {
        return 6;
    }

    protected int h() {
        return Item.SEEDS.id;
    }

    protected int j() {
        return Item.WHEAT.id;
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        super.dropNaturally(world, i, j, k, l, f, 0);
        if (!world.isStatic) {
            if (l >= 7) {
                int j1 = 3 + i1;

                for (int k1 = 0; k1 < j1; ++k1) {
                    if (world.random.nextInt(15) <= l) {
                        this.a(world, i, j, k, new ItemStack(this.h(), 1, 0));
                    }
                }
            }
        }
    }

    public int getDropType(int i, Random random, int j) {
        return i == 7 ? this.j() : this.h();
    }

    public int a(Random random) {
        return 1;
    }
}
