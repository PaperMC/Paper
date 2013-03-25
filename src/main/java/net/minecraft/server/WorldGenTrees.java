package net.minecraft.server;

import java.util.Random;

import org.bukkit.BlockChangeDelegate; // CraftBukkit

public class WorldGenTrees extends WorldGenerator implements BlockSapling.TreeGenerator { // CraftBukkit add interface

    private final int a;
    private final boolean b;
    private final int c;
    private final int d;

    public WorldGenTrees(boolean flag) {
        this(flag, 4, 0, 0, false);
    }

    public WorldGenTrees(boolean flag, int i, int j, int k, boolean flag1) {
        super(flag);
        this.a = i;
        this.c = j;
        this.d = k;
        this.b = flag1;
    }

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start - Moved to generate
        return this.generate((BlockChangeDelegate) world, random, i, j, k);
    }

    public boolean generate(BlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l = random.nextInt(3) + this.a;
        boolean flag = true;

        if (j >= 1 && j + l + 1 <= 256) {
            int i1;
            byte b0;
            int j1;
            int k1;

            for (i1 = j; i1 <= j + 1 + l; ++i1) {
                b0 = 1;
                if (i1 == j) {
                    b0 = 0;
                }

                if (i1 >= j + 1 + l - 2) {
                    b0 = 2;
                }

                for (int l1 = i - b0; l1 <= i + b0 && flag; ++l1) {
                    for (j1 = k - b0; j1 <= k + b0 && flag; ++j1) {
                        if (i1 >= 0 && i1 < 256) {
                            k1 = world.getTypeId(l1, i1, j1);
                            if (k1 != 0 && k1 != Block.LEAVES.id && k1 != Block.GRASS.id && k1 != Block.DIRT.id && k1 != Block.LOG.id) {
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
                i1 = world.getTypeId(i, j - 1, k);
                if ((i1 == Block.GRASS.id || i1 == Block.DIRT.id) && j < 256 - l - 1) {
                    this.setType(world, i, j - 1, k, Block.DIRT.id);
                    b0 = 3;
                    byte b1 = 0;

                    int i2;
                    int j2;
                    int k2;

                    for (j1 = j - b0 + l; j1 <= j + l; ++j1) {
                        k1 = j1 - (j + l);
                        i2 = b1 + 1 - k1 / 2;

                        for (j2 = i - i2; j2 <= i + i2; ++j2) {
                            k2 = j2 - i;

                            for (int l2 = k - i2; l2 <= k + i2; ++l2) {
                                int i3 = l2 - k;

                                if (Math.abs(k2) != i2 || Math.abs(i3) != i2 || random.nextInt(2) != 0 && k1 != 0) {
                                    int j3 = world.getTypeId(j2, j1, l2);

                                    if (j3 == 0 || j3 == Block.LEAVES.id) {
                                        this.setTypeAndData(world, j2, j1, l2, Block.LEAVES.id, this.d);
                                    }
                                }
                            }
                        }
                    }

                    for (j1 = 0; j1 < l; ++j1) {
                        k1 = world.getTypeId(i, j + j1, k);
                        if (k1 == 0 || k1 == Block.LEAVES.id) {
                            this.setTypeAndData(world, i, j + j1, k, Block.LOG.id, this.c);
                            if (this.b && j1 > 0) {
                                if (random.nextInt(3) > 0 && world.isEmpty(i - 1, j + j1, k)) {
                                    this.setTypeAndData(world, i - 1, j + j1, k, Block.VINE.id, 8);
                                }

                                if (random.nextInt(3) > 0 && world.isEmpty(i + 1, j + j1, k)) {
                                    this.setTypeAndData(world, i + 1, j + j1, k, Block.VINE.id, 2);
                                }

                                if (random.nextInt(3) > 0 && world.isEmpty(i, j + j1, k - 1)) {
                                    this.setTypeAndData(world, i, j + j1, k - 1, Block.VINE.id, 1);
                                }

                                if (random.nextInt(3) > 0 && world.isEmpty(i, j + j1, k + 1)) {
                                    this.setTypeAndData(world, i, j + j1, k + 1, Block.VINE.id, 4);
                                }
                            }
                        }
                    }

                    if (this.b) {
                        for (j1 = j - 3 + l; j1 <= j + l; ++j1) {
                            k1 = j1 - (j + l);
                            i2 = 2 - k1 / 2;

                            for (j2 = i - i2; j2 <= i + i2; ++j2) {
                                for (k2 = k - i2; k2 <= k + i2; ++k2) {
                                    if (world.getTypeId(j2, j1, k2) == Block.LEAVES.id) {
                                        if (random.nextInt(4) == 0 && world.getTypeId(j2 - 1, j1, k2) == 0) {
                                            this.b(world, j2 - 1, j1, k2, 8);
                                        }

                                        if (random.nextInt(4) == 0 && world.getTypeId(j2 + 1, j1, k2) == 0) {
                                            this.b(world, j2 + 1, j1, k2, 2);
                                        }

                                        if (random.nextInt(4) == 0 && world.getTypeId(j2, j1, k2 - 1) == 0) {
                                            this.b(world, j2, j1, k2 - 1, 1);
                                        }

                                        if (random.nextInt(4) == 0 && world.getTypeId(j2, j1, k2 + 1) == 0) {
                                            this.b(world, j2, j1, k2 + 1, 4);
                                        }
                                    }
                                }
                            }
                        }

                        if (random.nextInt(5) == 0 && l > 5) {
                            for (j1 = 0; j1 < 2; ++j1) {
                                for (k1 = 0; k1 < 4; ++k1) {
                                    if (random.nextInt(4 - j1) == 0) {
                                        i2 = random.nextInt(3);
                                        this.setTypeAndData(world, i + Direction.a[Direction.f[k1]], j + l - 5 + j1, k + Direction.b[Direction.f[k1]], Block.COCOA.id, i2 << 2 | k1);
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

    // CraftBukkit - Changed world to BlockChangeDelegate
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
