package net.minecraft.server;

import java.util.Random;

import org.bukkit.BlockChangeDelegate; // CraftBukkit

public class WorldGenSwampTree extends WorldGenerator implements BlockSapling.TreeGenerator { // CraftBukkit add interface

    public WorldGenSwampTree() {}

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start - Moved to generate
        return this.generate((BlockChangeDelegate) world, random, i, j, k);
    }

    public boolean generate(BlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l;

        for (l = random.nextInt(4) + 5; world.getTypeId(i, j - 1, k) != 0 && Block.byId[world.getTypeId(i, j - 1, k)].material == Material.WATER; --j) { // CraftBukkit - bypass world.getMaterial
            ;
        }

        boolean flag = true;

        if (j >= 1 && j + l + 1 <= 128) {
            int i1;
            int j1;
            int k1;
            int l1;

            for (i1 = j; i1 <= j + 1 + l; ++i1) {
                byte b0 = 1;

                if (i1 == j) {
                    b0 = 0;
                }

                if (i1 >= j + 1 + l - 2) {
                    b0 = 3;
                }

                for (j1 = i - b0; j1 <= i + b0 && flag; ++j1) {
                    for (k1 = k - b0; k1 <= k + b0 && flag; ++k1) {
                        if (i1 >= 0 && i1 < 128) {
                            l1 = world.getTypeId(j1, i1, k1);
                            if (l1 != 0 && l1 != Block.LEAVES.id) {
                                if (l1 != Block.STATIONARY_WATER.id && l1 != Block.WATER.id) {
                                    flag = false;
                                } else if (i1 > j) {
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
                i1 = world.getTypeId(i, j - 1, k);
                if ((i1 == Block.GRASS.id || i1 == Block.DIRT.id) && j < 128 - l - 1) {
                    this.setType(world, i, j - 1, k, Block.DIRT.id);

                    int i2;
                    int j2;

                    for (j2 = j - 3 + l; j2 <= j + l; ++j2) {
                        j1 = j2 - (j + l);
                        k1 = 2 - j1 / 2;

                        for (l1 = i - k1; l1 <= i + k1; ++l1) {
                            i2 = l1 - i;

                            for (int k2 = k - k1; k2 <= k + k1; ++k2) {
                                int l2 = k2 - k;

                                if ((Math.abs(i2) != k1 || Math.abs(l2) != k1 || random.nextInt(2) != 0 && j1 != 0) && !Block.t[world.getTypeId(l1, j2, k2)]) {
                                    this.setType(world, l1, j2, k2, Block.LEAVES.id);
                                }
                            }
                        }
                    }

                    for (j2 = 0; j2 < l; ++j2) {
                        j1 = world.getTypeId(i, j + j2, k);
                        if (j1 == 0 || j1 == Block.LEAVES.id || j1 == Block.WATER.id || j1 == Block.STATIONARY_WATER.id) {
                            this.setType(world, i, j + j2, k, Block.LOG.id);
                        }
                    }

                    for (j2 = j - 3 + l; j2 <= j + l; ++j2) {
                        j1 = j2 - (j + l);
                        k1 = 2 - j1 / 2;

                        for (l1 = i - k1; l1 <= i + k1; ++l1) {
                            for (i2 = k - k1; i2 <= k + k1; ++i2) {
                                if (world.getTypeId(l1, j2, i2) == Block.LEAVES.id) {
                                    if (random.nextInt(4) == 0 && world.getTypeId(l1 - 1, j2, i2) == 0) {
                                        this.b(world, l1 - 1, j2, i2, 8);
                                    }

                                    if (random.nextInt(4) == 0 && world.getTypeId(l1 + 1, j2, i2) == 0) {
                                        this.b(world, l1 + 1, j2, i2, 2);
                                    }

                                    if (random.nextInt(4) == 0 && world.getTypeId(l1, j2, i2 - 1) == 0) {
                                        this.b(world, l1, j2, i2 - 1, 1);
                                    }

                                    if (random.nextInt(4) == 0 && world.getTypeId(l1, j2, i2 + 1) == 0) {
                                        this.b(world, l1, j2, i2 + 1, 4);
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
    private void b(BlockChangeDelegate world, int i, int j, int k, int l) {
        this.setTypeAndData(world, i, j, k, Block.VINE.id, l);
        int i1 = 4;

        while (true) {
            --j;
            if (world.getTypeId(i, j, k) != 0 || i1 <= 0) {
                return;
            }

            this.setTypeAndData(world, i, j, k, Block.VINE.id, l);
            --i1;
        }
    }
}
