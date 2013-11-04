package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.CraftBlockChangeDelegate; // CraftBukkit

public class WorldGenTrees extends WorldGenTreeAbstract implements BlockSapling.TreeGenerator { // CraftBukkit - add interface

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
        return this.generate(new CraftBlockChangeDelegate((org.bukkit.BlockChangeDelegate) world), random, i, j, k);
    }

    public boolean generate(CraftBlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l = random.nextInt(3) + this.a;
        boolean flag = true;

        if (j >= 1 && j + l + 1 <= 256) {
            byte b0;
            int i1;
            Block block;

            for (int j1 = j; j1 <= j + 1 + l; ++j1) {
                b0 = 1;
                if (j1 == j) {
                    b0 = 0;
                }

                if (j1 >= j + 1 + l - 2) {
                    b0 = 2;
                }

                for (int k1 = i - b0; k1 <= i + b0 && flag; ++k1) {
                    for (i1 = k - b0; i1 <= k + b0 && flag; ++i1) {
                        if (j1 >= 0 && j1 < 256) {
                            block = world.getType(k1, j1, i1);
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
                    b0 = 3;
                    byte b1 = 0;

                    int l1;
                    int i2;
                    int j2;
                    int k2;

                    for (i1 = j - b0 + l; i1 <= j + l; ++i1) {
                        k2 = i1 - (j + l);
                        l1 = b1 + 1 - k2 / 2;

                        for (i2 = i - l1; i2 <= i + l1; ++i2) {
                            j2 = i2 - i;

                            for (int l2 = k - l1; l2 <= k + l1; ++l2) {
                                int i3 = l2 - k;

                                if (Math.abs(j2) != l1 || Math.abs(i3) != l1 || random.nextInt(2) != 0 && k2 != 0) {
                                    Block block2 = world.getType(i2, i1, l2);

                                    if (block2.getMaterial() == Material.AIR || block2.getMaterial() == Material.LEAVES) {
                                        this.setTypeAndData(world, i2, i1, l2, Blocks.LEAVES, this.d);
                                    }
                                }
                            }
                        }
                    }

                    for (i1 = 0; i1 < l; ++i1) {
                        block = world.getType(i, j + i1, k);
                        if (block.getMaterial() == Material.AIR || block.getMaterial() == Material.LEAVES) {
                            this.setTypeAndData(world, i, j + i1, k, Blocks.LOG, this.c);
                            if (this.b && i1 > 0) {
                                if (random.nextInt(3) > 0 && world.isEmpty(i - 1, j + i1, k)) {
                                    this.setTypeAndData(world, i - 1, j + i1, k, Blocks.VINE, 8);
                                }

                                if (random.nextInt(3) > 0 && world.isEmpty(i + 1, j + i1, k)) {
                                    this.setTypeAndData(world, i + 1, j + i1, k, Blocks.VINE, 2);
                                }

                                if (random.nextInt(3) > 0 && world.isEmpty(i, j + i1, k - 1)) {
                                    this.setTypeAndData(world, i, j + i1, k - 1, Blocks.VINE, 1);
                                }

                                if (random.nextInt(3) > 0 && world.isEmpty(i, j + i1, k + 1)) {
                                    this.setTypeAndData(world, i, j + i1, k + 1, Blocks.VINE, 4);
                                }
                            }
                        }
                    }

                    if (this.b) {
                        for (i1 = j - 3 + l; i1 <= j + l; ++i1) {
                            k2 = i1 - (j + l);
                            l1 = 2 - k2 / 2;

                            for (i2 = i - l1; i2 <= i + l1; ++i2) {
                                for (j2 = k - l1; j2 <= k + l1; ++j2) {
                                    if (world.getType(i2, i1, j2).getMaterial() == Material.LEAVES) {
                                        if (random.nextInt(4) == 0 && world.getType(i2 - 1, i1, j2).getMaterial() == Material.AIR) {
                                            this.a(world, i2 - 1, i1, j2, 8);
                                        }

                                        if (random.nextInt(4) == 0 && world.getType(i2 + 1, i1, j2).getMaterial() == Material.AIR) {
                                            this.a(world, i2 + 1, i1, j2, 2);
                                        }

                                        if (random.nextInt(4) == 0 && world.getType(i2, i1, j2 - 1).getMaterial() == Material.AIR) {
                                            this.a(world, i2, i1, j2 - 1, 1);
                                        }

                                        if (random.nextInt(4) == 0 && world.getType(i2, i1, j2 + 1).getMaterial() == Material.AIR) {
                                            this.a(world, i2, i1, j2 + 1, 4);
                                        }
                                    }
                                }
                            }
                        }

                        if (random.nextInt(5) == 0 && l > 5) {
                            for (i1 = 0; i1 < 2; ++i1) {
                                for (k2 = 0; k2 < 4; ++k2) {
                                    if (random.nextInt(4 - i1) == 0) {
                                        l1 = random.nextInt(3);
                                        this.setTypeAndData(world, i + Direction.a[Direction.f[k2]], j + l - 5 + i1, k + Direction.b[Direction.f[k2]], Blocks.COCOA, l1 << 2 | k2);
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
