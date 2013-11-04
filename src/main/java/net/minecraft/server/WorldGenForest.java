package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.CraftBlockChangeDelegate; // CraftBukkit

public class WorldGenForest extends WorldGenTreeAbstract implements BlockSapling.TreeGenerator { // CraftBukkit - add interface

    private boolean a;

    public WorldGenForest(boolean flag, boolean flag1) {
        super(flag);
        this.a = flag1;
    }

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start - Moved to generate
        return this.generate(new CraftBlockChangeDelegate((org.bukkit.BlockChangeDelegate) world), random, i, j, k);
    }

    public boolean generate(CraftBlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l = random.nextInt(3) + 5;

        if (this.a) {
            l += random.nextInt(7);
        }

        boolean flag = true;

        if (j >= 1 && j + l + 1 <= 256) {
            int i1;
            int j1;

            for (int k1 = j; k1 <= j + 1 + l; ++k1) {
                byte b0 = 1;

                if (k1 == j) {
                    b0 = 0;
                }

                if (k1 >= j + 1 + l - 2) {
                    b0 = 2;
                }

                for (i1 = i - b0; i1 <= i + b0 && flag; ++i1) {
                    for (j1 = k - b0; j1 <= k + b0 && flag; ++j1) {
                        if (k1 >= 0 && k1 < 256) {
                            Block block = world.getType(i1, k1, j1);

                            if (!this.a(block)) {
                                flag = false;
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                Block block1 = world.getType(i, j - 1, k);

                if ((block1 == Blocks.GRASS || block1 == Blocks.DIRT || block1 == Blocks.SOIL) && j < 256 - l - 1) {
                    this.setType(world, i, j - 1, k, Blocks.DIRT);

                    int l1;

                    for (l1 = j - 3 + l; l1 <= j + l; ++l1) {
                        i1 = l1 - (j + l);
                        j1 = 1 - i1 / 2;

                        for (int i2 = i - j1; i2 <= i + j1; ++i2) {
                            int j2 = i2 - i;

                            for (int k2 = k - j1; k2 <= k + j1; ++k2) {
                                int l2 = k2 - k;

                                if (Math.abs(j2) != j1 || Math.abs(l2) != j1 || random.nextInt(2) != 0 && i1 != 0) {
                                    Block block2 = world.getType(i2, l1, k2);

                                    if (block2.getMaterial() == Material.AIR || block2.getMaterial() == Material.LEAVES) {
                                        this.setTypeAndData(world, i2, l1, k2, Blocks.LEAVES, 2);
                                    }
                                }
                            }
                        }
                    }

                    for (l1 = 0; l1 < l; ++l1) {
                        Block block3 = world.getType(i, j + l1, k);

                        if (block3.getMaterial() == Material.AIR || block3.getMaterial() == Material.LEAVES) {
                            this.setTypeAndData(world, i, j + l1, k, Blocks.LOG, 2);
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
