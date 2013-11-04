package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.CraftBlockChangeDelegate; // CraftBukkit

public class WorldGenSwampTree extends WorldGenTreeAbstract implements BlockSapling.TreeGenerator { // CraftBukkit - add interface

    public WorldGenSwampTree() {
        super(false);
    }

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start - Moved to generate
        return this.generate(new CraftBlockChangeDelegate((org.bukkit.BlockChangeDelegate) world), random, i, j, k);
    }

    public boolean generate(CraftBlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l;

        for (l = random.nextInt(4) + 5; world.getType(i, j - 1, k).getMaterial() == Material.WATER; --j) {
            ;
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
                    b0 = 3;
                }

                for (i1 = i - b0; i1 <= i + b0 && flag; ++i1) {
                    for (j1 = k - b0; j1 <= k + b0 && flag; ++j1) {
                        if (k1 >= 0 && k1 < 256) {
                            Block block = world.getType(i1, k1, j1);

                            if (block.getMaterial() != Material.AIR && block.getMaterial() != Material.LEAVES) {
                                if (block != Blocks.STATIONARY_WATER && block != Blocks.WATER) {
                                    flag = false;
                                } else if (k1 > j) {
                                    flag = false;
                                }
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

                    int l1;
                    int i2;
                    int j2;

                    for (j2 = j - 3 + l; j2 <= j + l; ++j2) {
                        i1 = j2 - (j + l);
                        j1 = 2 - i1 / 2;

                        for (i2 = i - j1; i2 <= i + j1; ++i2) {
                            l1 = i2 - i;

                            for (int k2 = k - j1; k2 <= k + j1; ++k2) {
                                int l2 = k2 - k;

                                if ((Math.abs(l1) != j1 || Math.abs(l2) != j1 || random.nextInt(2) != 0 && i1 != 0) && !world.getType(i2, j2, k2).j()) {
                                    this.setType(world, i2, j2, k2, Blocks.LEAVES);
                                }
                            }
                        }
                    }

                    for (j2 = 0; j2 < l; ++j2) {
                        Block block2 = world.getType(i, j + j2, k);

                        if (block2.getMaterial() == Material.AIR || block2.getMaterial() == Material.LEAVES || block2 == Blocks.WATER || block2 == Blocks.STATIONARY_WATER) {
                            this.setType(world, i, j + j2, k, Blocks.LOG);
                        }
                    }

                    for (j2 = j - 3 + l; j2 <= j + l; ++j2) {
                        i1 = j2 - (j + l);
                        j1 = 2 - i1 / 2;

                        for (i2 = i - j1; i2 <= i + j1; ++i2) {
                            for (l1 = k - j1; l1 <= k + j1; ++l1) {
                                if (world.getType(i2, j2, l1).getMaterial() == Material.LEAVES) {
                                    if (random.nextInt(4) == 0 && world.getType(i2 - 1, j2, l1).getMaterial() == Material.AIR) {
                                        this.a(world, i2 - 1, j2, l1, 8);
                                    }

                                    if (random.nextInt(4) == 0 && world.getType(i2 + 1, j2, l1).getMaterial() == Material.AIR) {
                                        this.a(world, i2 + 1, j2, l1, 2);
                                    }

                                    if (random.nextInt(4) == 0 && world.getType(i2, j2, l1 - 1).getMaterial() == Material.AIR) {
                                        this.a(world, i2, j2, l1 - 1, 1);
                                    }

                                    if (random.nextInt(4) == 0 && world.getType(i2, j2, l1 + 1).getMaterial() == Material.AIR) {
                                        this.a(world, i2, j2, l1 + 1, 4);
                                    }
                                }
                            }
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

    // CraftBukkit - change signature
    private void a(CraftBlockChangeDelegate world, int i, int j, int k, int l) {
        this.setTypeAndData(world, i, j, k, Blocks.VINE, l);
        int i1 = 4;

        while (true) {
            --j;
            if (world.getType(i, j, k).getMaterial() != Material.AIR || i1 <= 0) {
                return;
            }

            this.setTypeAndData(world, i, j, k, Blocks.VINE, l);
            --i1;
        }
    }
}
