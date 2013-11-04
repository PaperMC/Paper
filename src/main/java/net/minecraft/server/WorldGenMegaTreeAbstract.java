package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.CraftBlockChangeDelegate; // CraftBukkit

public abstract class WorldGenMegaTreeAbstract extends WorldGenTreeAbstract {

    protected final int a;
    protected final int b;
    protected final int c;
    protected int d;

    public WorldGenMegaTreeAbstract(boolean flag, int i, int j, int k, int l) {
        super(flag);
        this.a = i;
        this.d = j;
        this.b = k;
        this.c = l;
    }

    protected int a(Random random) {
        int i = random.nextInt(3) + this.a;

        if (this.d > 1) {
            i += random.nextInt(this.d);
        }

        return i;
    }

    // CraftBukkit - Changed world to CraftBlockChangeDelegate
    private boolean b(CraftBlockChangeDelegate world, Random random, int i, int j, int k, int l) {
        boolean flag = true;

        if (j >= 1 && j + l + 1 <= 256) {
            for (int i1 = j; i1 <= j + 1 + l; ++i1) {
                byte b0 = 2;

                if (i1 == j) {
                    b0 = 1;
                }

                if (i1 >= j + 1 + l - 2) {
                    b0 = 2;
                }

                for (int j1 = i - b0; j1 <= i + b0 && flag; ++j1) {
                    for (int k1 = k - b0; k1 <= k + b0 && flag; ++k1) {
                        if (i1 >= 0 && i1 < 256) {
                            Block block = world.getType(j1, i1, k1);

                            // CraftBukkit - ignore our own saplings
                            if (block != Blocks.SAPLING && !this.a(block)) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            return flag;
        } else {
            return false;
        }
    }

    // CraftBukkit - Change world to CraftBlockChangeDelegate
    private boolean c(CraftBlockChangeDelegate world, Random random, int i, int j, int k) {
        Block block = world.getType(i, j - 1, k);

        if ((block == Blocks.GRASS || block == Blocks.DIRT) && j >= 2) {
            world.setTypeAndData(i, j - 1, k, Blocks.DIRT, 0, 2);
            world.setTypeAndData(i + 1, j - 1, k, Blocks.DIRT, 0, 2);
            world.setTypeAndData(i, j - 1, k + 1, Blocks.DIRT, 0, 2);
            world.setTypeAndData(i + 1, j - 1, k + 1, Blocks.DIRT, 0, 2);
            return true;
        } else {
            return false;
        }
    }

    // CraftBukkit - Change world to CraftBlockChangeDelegate
    protected boolean a(CraftBlockChangeDelegate world, Random random, int i, int j, int k, int l) {
        return this.b(world, random, i, j, k, l) && this.c(world, random, i, j, k);
    }

    // CraftBukkit - Change world to CraftBlockChangeDelegate
    protected void a(CraftBlockChangeDelegate world, int i, int j, int k, int l, Random random) {
        int i1 = l * l;

        for (int j1 = i - l; j1 <= i + l + 1; ++j1) {
            int k1 = j1 - i;

            for (int l1 = k - l; l1 <= k + l + 1; ++l1) {
                int i2 = l1 - k;
                int j2 = k1 - 1;
                int k2 = i2 - 1;

                if (k1 * k1 + i2 * i2 <= i1 || j2 * j2 + k2 * k2 <= i1 || k1 * k1 + k2 * k2 <= i1 || j2 * j2 + i2 * i2 <= i1) {
                    Block block = world.getType(j1, j, l1);

                    if (block.getMaterial() == Material.AIR || block.getMaterial() == Material.LEAVES) {
                        this.setTypeAndData(world, j1, j, l1, Blocks.LEAVES, this.c);
                    }
                }
            }
        }
    }

    // CraftBukkit - Change world to CraftBlockChangeDelegate
    protected void b(CraftBlockChangeDelegate world, int i, int j, int k, int l, Random random) {
        int i1 = l * l;

        for (int j1 = i - l; j1 <= i + l; ++j1) {
            int k1 = j1 - i;

            for (int l1 = k - l; l1 <= k + l; ++l1) {
                int i2 = l1 - k;

                if (k1 * k1 + i2 * i2 <= i1) {
                    Block block = world.getType(j1, j, l1);

                    if (block.getMaterial() == Material.AIR || block.getMaterial() == Material.LEAVES) {
                        this.setTypeAndData(world, j1, j, l1, Blocks.LEAVES, this.c);
                    }
                }
            }
        }
    }
}
