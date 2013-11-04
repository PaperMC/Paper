package net.minecraft.server;

import java.util.Random;

import org.bukkit.craftbukkit.CraftBlockChangeDelegate; // CraftBukkit

public class WorldGenAcaciaTree extends WorldGenTreeAbstract implements BlockSapling.TreeGenerator { // CraftBukkit - add interface

    public WorldGenAcaciaTree(boolean flag) {
        super(flag);
    }

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start - Moved to generate
        return this.generate(new CraftBlockChangeDelegate((org.bukkit.BlockChangeDelegate) world), random, i, j, k);
    }

    public boolean generate(CraftBlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l = random.nextInt(3) + random.nextInt(3) + 5;
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

                if ((block1 == Blocks.GRASS || block1 == Blocks.DIRT) && j < 256 - l - 1) {
                    this.setType(world, i, j - 1, k, Blocks.DIRT);
                    int l1 = random.nextInt(4);

                    i1 = l - random.nextInt(4) - 1;
                    j1 = 3 - random.nextInt(3);
                    int i2 = i;
                    int j2 = k;
                    int k2 = 0;

                    int l2;
                    int i3;

                    for (l2 = 0; l2 < l; ++l2) {
                        i3 = j + l2;
                        if (l2 >= i1 && j1 > 0) {
                            i2 += Direction.a[l1];
                            j2 += Direction.b[l1];
                            --j1;
                        }

                        Block block2 = world.getType(i2, i3, j2);

                        if (block2.getMaterial() == Material.AIR || block2.getMaterial() == Material.LEAVES) {
                            this.setTypeAndData(world, i2, i3, j2, Blocks.LOG2, 0);
                            k2 = i3;
                        }
                    }

                    for (l2 = -1; l2 <= 1; ++l2) {
                        for (i3 = -1; i3 <= 1; ++i3) {
                            this.a(world, i2 + l2, k2 + 1, j2 + i3);
                        }
                    }

                    this.a(world, i2 + 2, k2 + 1, j2);
                    this.a(world, i2 - 2, k2 + 1, j2);
                    this.a(world, i2, k2 + 1, j2 + 2);
                    this.a(world, i2, k2 + 1, j2 - 2);

                    for (l2 = -3; l2 <= 3; ++l2) {
                        for (i3 = -3; i3 <= 3; ++i3) {
                            if (Math.abs(l2) != 3 || Math.abs(i3) != 3) {
                                this.a(world, i2 + l2, k2, j2 + i3);
                            }
                        }
                    }

                    i2 = i;
                    j2 = k;
                    l2 = random.nextInt(4);
                    if (l2 != l1) {
                        i3 = i1 - random.nextInt(2) - 1;
                        int j3 = 1 + random.nextInt(3);

                        k2 = 0;

                        int k3;
                        int l3;

                        for (l3 = i3; l3 < l && j3 > 0; --j3) {
                            if (l3 >= 1) {
                                k3 = j + l3;
                                i2 += Direction.a[l2];
                                j2 += Direction.b[l2];
                                Block block3 = world.getType(i2, k3, j2);

                                if (block3.getMaterial() == Material.AIR || block3.getMaterial() == Material.LEAVES) {
                                    this.setTypeAndData(world, i2, k3, j2, Blocks.LOG2, 0);
                                    k2 = k3;
                                }
                            }

                            ++l3;
                        }

                        if (k2 > 0) {
                            for (l3 = -1; l3 <= 1; ++l3) {
                                for (k3 = -1; k3 <= 1; ++k3) {
                                    this.a(world, i2 + l3, k2 + 1, j2 + k3);
                                }
                            }

                            for (l3 = -2; l3 <= 2; ++l3) {
                                for (k3 = -2; k3 <= 2; ++k3) {
                                    if (Math.abs(l3) != 2 || Math.abs(k3) != 2) {
                                        this.a(world, i2 + l3, k2, j2 + k3);
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

    // CraftBukkit - Changed signature
    private void a(CraftBlockChangeDelegate world, int i, int j, int k) {
        Block block = world.getType(i, j, k);

        if (block.getMaterial() == Material.AIR || block.getMaterial() == Material.LEAVES) {
            this.setTypeAndData(world, i, j, k, Blocks.LEAVES2, 0);
        }
    }
}
