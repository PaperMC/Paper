package net.minecraft.server;

import java.util.Random;

import org.bukkit.BlockChangeDelegate; // CraftBukkit

public class WorldGenTaiga1 extends WorldGenerator implements BlockSapling.TreeGenerator { // CraftBukkit add interface

    public WorldGenTaiga1() {}

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start - Moved to generate
        return this.generate((BlockChangeDelegate) world, random, i, j, k);
    }

    public boolean generate(BlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l = random.nextInt(5) + 7;
        int i1 = l - random.nextInt(2) - 3;
        int j1 = l - i1;
        int k1 = 1 + random.nextInt(j1 + 1);
        boolean flag = true;

        if (j >= 1 && j + l + 1 <= 128) {
            int l1;
            int i2;
            int j2;
            int k2;
            int l2;

            for (l1 = j; l1 <= j + 1 + l && flag; ++l1) {
                boolean flag1 = true;

                if (l1 - j < i1) {
                    l2 = 0;
                } else {
                    l2 = k1;
                }

                for (i2 = i - l2; i2 <= i + l2 && flag; ++i2) {
                    for (j2 = k - l2; j2 <= k + l2 && flag; ++j2) {
                        if (l1 >= 0 && l1 < 128) {
                            k2 = world.getTypeId(i2, l1, j2);
                            if (k2 != 0 && k2 != Block.LEAVES.id) {
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
                l1 = world.getTypeId(i, j - 1, k);
                if ((l1 == Block.GRASS.id || l1 == Block.DIRT.id) && j < 128 - l - 1) {
                    this.setType(world, i, j - 1, k, Block.DIRT.id);
                    l2 = 0;

                    for (i2 = j + l; i2 >= j + i1; --i2) {
                        for (j2 = i - l2; j2 <= i + l2; ++j2) {
                            k2 = j2 - i;

                            for (int i3 = k - l2; i3 <= k + l2; ++i3) {
                                int j3 = i3 - k;

                                if ((Math.abs(k2) != l2 || Math.abs(j3) != l2 || l2 <= 0) && !Block.t[world.getTypeId(j2, i2, i3)]) {
                                    this.setTypeAndData(world, j2, i2, i3, Block.LEAVES.id, 1);
                                }
                            }
                        }

                        if (l2 >= 1 && i2 == j + i1 + 1) {
                            --l2;
                        } else if (l2 < k1) {
                            ++l2;
                        }
                    }

                    for (i2 = 0; i2 < l - 1; ++i2) {
                        j2 = world.getTypeId(i, j + i2, k);
                        if (j2 == 0 || j2 == Block.LEAVES.id) {
                            this.setTypeAndData(world, i, j + i2, k, Block.LOG.id, 1);
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
