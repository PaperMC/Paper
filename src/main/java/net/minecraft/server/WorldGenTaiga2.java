package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.CraftBlockChangeDelegate; // CraftBukkit

public class WorldGenTaiga2 extends WorldGenTreeAbstract implements BlockSapling.TreeGenerator { // CraftBukkit - add interface

    public WorldGenTaiga2(boolean flag) {
        super(flag);
    }

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start - Moved to generate
        return this.generate(new CraftBlockChangeDelegate((org.bukkit.BlockChangeDelegate) world), random, i, j, k);
    }

    public boolean generate(CraftBlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l = random.nextInt(4) + 6;
        int i1 = 1 + random.nextInt(2);
        int j1 = l - i1;
        int k1 = 2 + random.nextInt(2);
        boolean flag = true;

        if (j >= 1 && j + l + 1 <= 256) {
            int l1;
            int i2;

            for (int j2 = j; j2 <= j + 1 + l && flag; ++j2) {
                boolean flag1 = true;

                if (j2 - j < i1) {
                    i2 = 0;
                } else {
                    i2 = k1;
                }

                for (l1 = i - i2; l1 <= i + i2 && flag; ++l1) {
                    for (int k2 = k - i2; k2 <= k + i2 && flag; ++k2) {
                        if (j2 >= 0 && j2 < 256) {
                            Block block = world.getType(l1, j2, k2);

                            if (block.getMaterial() != Material.AIR && block.getMaterial() != Material.LEAVES) {
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
                    i2 = random.nextInt(2);
                    l1 = 1;
                    byte b0 = 0;

                    int l2;
                    int i3;

                    for (i3 = 0; i3 <= j1; ++i3) {
                        l2 = j + l - i3;

                        for (int j3 = i - i2; j3 <= i + i2; ++j3) {
                            int k3 = j3 - i;

                            for (int l3 = k - i2; l3 <= k + i2; ++l3) {
                                int i4 = l3 - k;

                                if ((Math.abs(k3) != i2 || Math.abs(i4) != i2 || i2 <= 0) && !world.getType(j3, l2, l3).j()) {
                                    this.setTypeAndData(world, j3, l2, l3, Blocks.LEAVES, 1);
                                }
                            }
                        }

                        if (i2 >= l1) {
                            i2 = b0;
                            b0 = 1;
                            ++l1;
                            if (l1 > k1) {
                                l1 = k1;
                            }
                        } else {
                            ++i2;
                        }
                    }

                    i3 = random.nextInt(3);

                    for (l2 = 0; l2 < l - i3; ++l2) {
                        Block block2 = world.getType(i, j + l2, k);

                        if (block2.getMaterial() == Material.AIR || block2.getMaterial() == Material.LEAVES) {
                            this.setTypeAndData(world, i, j + l2, k, Blocks.LOG, 1);
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
