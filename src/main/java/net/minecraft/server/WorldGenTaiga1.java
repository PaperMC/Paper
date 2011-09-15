package net.minecraft.server;

import java.util.Random;

import org.bukkit.BlockChangeDelegate; // CraftBukkit

public class WorldGenTaiga1 extends WorldGenerator {

    public WorldGenTaiga1() {}

    public boolean a(World world, Random random, int i, int j, int k) {
        // CraftBukkit start
        // sk: The idea is to have (our) WorldServer implement
        // BlockChangeDelegate and then we can implicitly cast World to
        // WorldServer (a safe cast, AFAIK) and no code will be broken. This
        // then allows plugins to catch manually-invoked generation events
        return this.generate((BlockChangeDelegate) world, random, i, j, k);
    }

    public boolean generate(BlockChangeDelegate world, Random random, int i, int j, int k) {
        // CraftBukkit end
        int l = random.nextInt(5) + 7;
        int i1 = l - random.nextInt(2) - 3;
        int j1 = l - i1;
        int k1 = 1 + random.nextInt(j1 + 1);
        boolean flag = true;

        if (j >= 1) {
            int l1 = j + l + 1;

            world.getClass();
            if (l1 <= 128) {
                int i2;
                int j2;
                int k2;
                int l2;
                int i3;

                for (i2 = j; i2 <= j + 1 + l && flag; ++i2) {
                    boolean flag1 = true;

                    if (i2 - j < i1) {
                        i3 = 0;
                    } else {
                        i3 = k1;
                    }

                    for (j2 = i - i3; j2 <= i + i3 && flag; ++j2) {
                        for (k2 = k - i3; k2 <= k + i3 && flag; ++k2) {
                            if (i2 >= 0) {
                                world.getClass();
                                if (i2 < 128) {
                                    l2 = world.getTypeId(j2, i2, k2);
                                    if (l2 != 0 && l2 != Block.LEAVES.id) {
                                        flag = false;
                                    }
                                    continue;
                                }
                            }

                            flag = false;
                        }
                    }
                }

                if (!flag) {
                    return false;
                }

                i2 = world.getTypeId(i, j - 1, k);
                if (i2 == Block.GRASS.id || i2 == Block.DIRT.id) {
                    world.getClass();
                    if (j < 128 - l - 1) {
                        world.setRawTypeId(i, j - 1, k, Block.DIRT.id);
                        i3 = 0;

                        for (j2 = j + l; j2 >= j + i1; --j2) {
                            for (k2 = i - i3; k2 <= i + i3; ++k2) {
                                l2 = k2 - i;

                                for (int j3 = k - i3; j3 <= k + i3; ++j3) {
                                    int k3 = j3 - k;

                                    if ((Math.abs(l2) != i3 || Math.abs(k3) != i3 || i3 <= 0) && !Block.o[world.getTypeId(k2, j2, j3)]) {
                                        world.setRawTypeIdAndData(k2, j2, j3, Block.LEAVES.id, 1);
                                    }
                                }
                            }

                            if (i3 >= 1 && j2 == j + i1 + 1) {
                                --i3;
                            } else if (i3 < k1) {
                                ++i3;
                            }
                        }

                        for (j2 = 0; j2 < l - 1; ++j2) {
                            k2 = world.getTypeId(i, j + j2, k);
                            if (k2 == 0 || k2 == Block.LEAVES.id) {
                                world.setRawTypeIdAndData(i, j + j2, k, Block.LOG.id, 1);
                            }
                        }

                        return true;
                    }
                }

                return false;
            }
        }

        return false;
    }
}
