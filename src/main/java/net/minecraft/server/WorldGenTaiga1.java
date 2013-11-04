package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.CraftBlockChangeDelegate; // CraftBukkit

public class WorldGenTaiga1 extends WorldGenTreeAbstract implements BlockSapling.TreeGenerator { // CraftBukkit - add interface

    public WorldGenTaiga1() {
        super(false);
    }

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start - Moved to generate
        return this.generate(new CraftBlockChangeDelegate((org.bukkit.BlockChangeDelegate) world), random, i, j, k);
    }

    public boolean generate(CraftBlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l = random.nextInt(5) + 7;
        int i1 = l - random.nextInt(2) - 3;
        int j1 = l - i1;
        int k1 = 1 + random.nextInt(j1 + 1);
        boolean flag = true;

        if (j >= 1 && j + l + 1 <= 256) {
            int l1;
            int i2;
            int j2;

            for (int k2 = j; k2 <= j + 1 + l && flag; ++k2) {
                boolean flag1 = true;

                if (k2 - j < i1) {
                    j2 = 0;
                } else {
                    j2 = k1;
                }

                for (l1 = i - j2; l1 <= i + j2 && flag; ++l1) {
                    for (i2 = k - j2; i2 <= k + j2 && flag; ++i2) {
                        if (k2 >= 0 && k2 < 256) {
                            Block block = world.getType(l1, k2, i2);

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

                if ((block1 == Blocks.GRASS || block1 == Blocks.DIRT) && j < 256 - l - 1) {
                    this.setType(world, i, j - 1, k, Blocks.DIRT);
                    j2 = 0;

                    for (l1 = j + l; l1 >= j + i1; --l1) {
                        for (i2 = i - j2; i2 <= i + j2; ++i2) {
                            int l2 = i2 - i;

                            for (int i3 = k - j2; i3 <= k + j2; ++i3) {
                                int j3 = i3 - k;

                                if ((Math.abs(l2) != j2 || Math.abs(j3) != j2 || j2 <= 0) && !world.getType(i2, l1, i3).j()) {
                                    this.setTypeAndData(world, i2, l1, i3, Blocks.LEAVES, 1);
                                }
                            }
                        }

                        if (j2 >= 1 && l1 == j + i1 + 1) {
                            --j2;
                        } else if (j2 < k1) {
                            ++j2;
                        }
                    }

                    for (l1 = 0; l1 < l - 1; ++l1) {
                        Block block2 = world.getType(i, j + l1, k);

                        if (block2.getMaterial() == Material.AIR || block2.getMaterial() == Material.LEAVES) {
                            this.setTypeAndData(world, i, j + l1, k, Blocks.LOG, 1);
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
